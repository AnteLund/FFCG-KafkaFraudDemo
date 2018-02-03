package se.ffcg.fraud.kafka.util;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import se.ffcg.fraud.model.Transaction;

import java.util.Map;

public class TransactionSerde implements Serde<Transaction> {

  static final ObjectMapper OBJECT_MAPPER;

  static {
    OBJECT_MAPPER = new ObjectMapper();
    OBJECT_MAPPER.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
    OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  private final Serializer<Transaction> serializer;
  private final Deserializer<Transaction> deserializer;

  public TransactionSerde() {
    this.serializer = new JsonSerializer<>();
    this.deserializer = new JsonDeserializer(Transaction.class);
  }

  @Override
  public void configure(Map<String, ?> configs, boolean isKey) {
    this.serializer.configure(configs, isKey);
    this.deserializer.configure(configs, isKey);
  }

  @Override
  public void close() {
    this.serializer.close();
    this.deserializer.close();

  }

  @Override
  public Serializer<Transaction> serializer() {
    return serializer;
  }

  @Override
  public Deserializer<Transaction> deserializer() {
    return deserializer;
  }
}
