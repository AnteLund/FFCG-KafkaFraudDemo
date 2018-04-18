package se.ffcg.fraud.kafka;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import se.ffcg.fraud.model.events.CustomerEvent;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {
  @Bean
  public ProducerFactory<String, CustomerEvent> producerFactory() {
    Map<String, Object> producerProps = new HashMap<>();
    producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    DefaultKafkaProducerFactory<String, CustomerEvent> producerFactory =
        new DefaultKafkaProducerFactory<>(producerProps);
    return producerFactory;
  }

  @Bean
  public KafkaTemplate<String, CustomerEvent> kafkaTemplate(ProducerFactory producerFactory) {
    KafkaTemplate kafkaTemplate =  new KafkaTemplate<>(producerFactory);
    return kafkaTemplate;
  }
}
