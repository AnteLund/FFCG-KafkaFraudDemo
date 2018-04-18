
package se.ffcg.fraud.kafka.streams;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.errors.InvalidStateStoreException;
import org.apache.kafka.streams.errors.LogAndContinueExceptionHandler;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.Serialized;
import org.apache.kafka.streams.processor.TopologyBuilder;
import org.apache.kafka.streams.state.QueryableStoreType;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import se.ffcg.fraud.kafka.CustomerEventMerger;
import se.ffcg.fraud.model.Customer;
import se.ffcg.fraud.model.events.CustomerCreatedEvent;
import se.ffcg.fraud.model.events.CustomerEvent;
import se.ffcg.fraud.model.events.CustomerUpdatedEvent;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Configuration
@RestController
public class CustomerEventStreamProcessor {

  private static final String TOPIC_EVENTS = "kafka.summit.events";

  private static final String CUSTOMER_AGG_STATE_TOPIC = "customer-agg-topic";
  private static final String CUSTOMER_AGG_STORE = "customer-agg-store";

  Logger log = LoggerFactory.getLogger(CustomerEventStreamProcessor.class);

  Serde<CustomerEvent> eventSerde;
  Serde<Customer> customerSerde;

  ReadOnlyKeyValueStore<String, Customer> custView;
  ReadOnlyKeyValueStore<String, Long> ssnKimIdMapView;

  CustomerEventMerger customerEventMerger = new CustomerEventMerger();

  public CustomerEventStreamProcessor() {
    log.info("Starting stream processor");

    //SerDes's
    eventSerde = new JsonSerde<>(CustomerEvent.class);
    customerSerde = new JsonSerde<>(Customer.class);


    Properties props = new Properties();
    props.put(StreamsConfig.APPLICATION_ID_CONFIG, "event-processor-app");
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, JsonSerde.class);
    props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 5000);
    props.put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG, LogAndContinueExceptionHandler.class);
    // Denna behöver vi för att undvika duplikat konsumering
    props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
    KStreamBuilder builder = new KStreamBuilder();

    //Stream CustomerEvent's from Kafka topic
    KStream<String, CustomerEvent> eventStream = builder.
        stream(Serdes.String(), eventSerde, TOPIC_EVENTS);
    eventStream.print(Printed.toSysOut());

    //Aggregate customer snapshot from CustomerEvent
    eventStream
        //ReKey to CustomerId
        .selectKey((eventKey, eventValue) -> {
          return eventValue.getCustomerId();
        })

        //Group by Key (CustomerId)
        .groupByKey(Serialized.with(Serdes.String(), eventSerde))

        //Aggregate customer from event
        .aggregate(
            () -> new Customer(),
            (kim, event, currentCustomer) ->
                customerEventMerger.merge(currentCustomer, event),
            customerSerde,
            CUSTOMER_AGG_STORE)

        //Stream to aggregate-topic.
        .toStream()
        .to(CUSTOMER_AGG_STATE_TOPIC, Produced.with(Serdes.String(), customerSerde));

    //Starta processen
    startProcess(builder, props);
  }

  private String getPersonalIdFromEvent(CustomerEvent eventValue) {
    if (eventValue instanceof CustomerCreatedEvent) {
      CustomerCreatedEvent event = (CustomerCreatedEvent) eventValue;
      return event.getSocialSecurityNumber();
    }
    if (eventValue instanceof CustomerUpdatedEvent) {
      CustomerUpdatedEvent event = (CustomerUpdatedEvent) eventValue;
      return event.getSocialSecurityNumber();
    }
    return null;
  }

  // Example: Wait until the store of type T is queryable.  When it is, return a reference to the store.
  private void startProcess(TopologyBuilder builder, Properties props) {
    KafkaStreams streams = new KafkaStreams(builder, props);
    streams.cleanUp();
    streams.setUncaughtExceptionHandler((thread, exception) ->
        {
          log.warn("Got exception", exception);
          streams.close(3, TimeUnit.SECONDS);
//

        }
    );
    Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    streams.start();

    log.info("Setting up state stores");
    try {
      custView =
          waitUntilStoreIsQueryable(CUSTOMER_AGG_STORE, QueryableStoreTypes.keyValueStore(), streams);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    log.warn("Setting up state stores - DONE");
  }


  private static <T> T waitUntilStoreIsQueryable(final String storeName,
                                                final QueryableStoreType<T> queryableStoreType,
                                                final KafkaStreams streams) throws InterruptedException {
    while (true) {
      try {
        return streams.store(storeName, queryableStoreType);
      } catch (InvalidStateStoreException ignored) {
        // store not yet ready for querying
        Thread.sleep(100);
      }
    }
  }

  @RequestMapping(value = "/customer/{kimId}", produces = "application/json", method = RequestMethod.GET)
  public Customer getCustomer(@PathVariable(value="kimId") String kimId) {
    return custView.get(kimId);
  }
}
