package se.ffcg.fraud.kafka.streams;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.errors.LogAndContinueExceptionHandler;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import se.ffcg.fraud.GoogleGeoService;
import se.ffcg.fraud.kafka.util.TransactionSerde;
import se.ffcg.fraud.model.Transaction;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@EnableKafkaStreams
public class FraudStream {

  @Autowired
  GoogleGeoService googleGeoService;

  @Value("${kafka.bootstrap.servers}")
  String kafkaBootstrapServers;

  @Value("${raw.transaction}")
  @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
  public StreamsConfig kStreamsConfigs() {

    Map<String, Object> props = new HashMap<>();
    props.put(StreamsConfig.APPLICATION_ID_CONFIG, "testStreams");
    props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
    props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, TransactionSerde.class);
    props.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, WallclockTimestampExtractor.class.getName());
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers);
    props.put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG, LogAndContinueExceptionHandler.class);
    return new StreamsConfig(props);
  }

  @Bean
  public KStream<String, Transaction> kStream(StreamsBuilder kStreamBuilder) {
    KStream<String, Transaction> stream = kStreamBuilder.stream("raw.transactions");
    stream.print(Printed.toSysOut());
    stream.mapValues(transaction -> calculateRiskScore(transaction)).mapValues(transaction -> addGeoLocation(transaction))
        .to("transactions");
    return stream;
  }

  private Transaction addGeoLocation(Transaction transaction) {
    transaction.setLocation(googleGeoService.getLocation(transaction.getZipCode(), transaction.getCity(), transaction.getCountry()));
    return transaction;
  }

  private Transaction calculateRiskScore(Transaction transaction) {
    int riskScore = 0;
    if(Integer.parseInt(transaction.getAmount()) > 1000 && Integer.parseInt(transaction.getAmount()) < 10000 ) {
      riskScore += 25;
    } else if (Integer.parseInt(transaction.getAmount()) > 10000) {
      riskScore += 50;
    }

    if(transaction.getCountry().equals("Brazil")) {
      riskScore += 100;
    } else if (transaction.getCountry().equals("Denmark")) {
      riskScore += 25;
    } else if (!transaction.getCountry().equals("Sweden")) {
      riskScore += 10;
    }

    transaction.setRiskScore(String.valueOf(riskScore));
    return transaction;
  }
}
