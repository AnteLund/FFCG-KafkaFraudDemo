package se.ffcg.fraud.kafka.util;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.errors.DeserializationExceptionHandler;
import org.apache.kafka.streams.processor.ProcessorContext;

import java.util.Map;

public class KafkaSerdeExceptionHandler implements DeserializationExceptionHandler {
  @Override
  public DeserializationHandlerResponse handle(ProcessorContext context, ConsumerRecord<byte[], byte[]> record, Exception exception) {
    return null;
  }

  @Override
  public void configure(Map<String, ?> map) {

  }
}
