package se.ffcg.fraud.rest;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.ffcg.fraud.model.events.AddressSetEvent;
import se.ffcg.fraud.model.events.CustomerCreatedEvent;
import se.ffcg.fraud.model.events.CustomerEvent;
import se.ffcg.fraud.model.events.CustomerMetaDataSetEvent;
import se.ffcg.fraud.model.events.CustomerUpdatedEvent;
import se.ffcg.fraud.model.rest.CreateAddressRequest;
import se.ffcg.fraud.model.rest.CreateCustomerRequest;
import se.ffcg.fraud.model.rest.CreateMetadataRequest;
import se.ffcg.fraud.model.rest.CustomerUpdateRequest;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping(value = "/customer")
public class CustomerController {

  @Autowired
  KafkaTemplate<String, CustomerEvent> kafkaTemplate;

  private static final String KAFKA_SUMMIT_EVENT_TOPIC = "kafka.summit.events";

  @RequestMapping(value = "create-customer", method = POST)
  public String createCustomer(@RequestBody CreateCustomerRequest request) {
    CustomerCreatedEvent customerCreatedEvent = new CustomerCreatedEvent();
    customerCreatedEvent.setFirstName(request.getFirstName());
    customerCreatedEvent.setLastName(request.getLastName());
    customerCreatedEvent.setSocialSecurityNumber(request.getSocialSecurityNumber());
    UUID customerId = UUID.randomUUID();
    customerCreatedEvent.setCustomerId(customerId.toString());
    customerCreatedEvent.setGuid(UUID.randomUUID().toString());
    customerCreatedEvent.setTimeStamp(LocalDateTime.now().toString());
    kafkaTemplate.send(KAFKA_SUMMIT_EVENT_TOPIC, customerCreatedEvent.getGuid(), customerCreatedEvent);
    return customerId.toString();
  }

  @RequestMapping(value = "{customer-id}/create-metadata", method = POST)
  public void createMetadata(@PathVariable("customer-id") String customerId,@RequestBody CreateMetadataRequest request) {
    CustomerMetaDataSetEvent customerMetaDataSetEvent = new CustomerMetaDataSetEvent();
    customerMetaDataSetEvent.setType(request.getType());
    customerMetaDataSetEvent.setValue(request.getValue());
    customerMetaDataSetEvent.setCustomerId(customerId);
    customerMetaDataSetEvent.setGuid(UUID.randomUUID().toString());
    customerMetaDataSetEvent.setTimeStamp(LocalDateTime.now().toString());
    kafkaTemplate.send(KAFKA_SUMMIT_EVENT_TOPIC, customerMetaDataSetEvent.getGuid(), customerMetaDataSetEvent);
  }

  @RequestMapping(value = "{customer-id}/update-customer", method = POST)
  public void customerUpdate(@PathVariable("customer-id") String customerId, @RequestBody CustomerUpdateRequest request) {
    CustomerUpdatedEvent customerUpdatedEvent = new CustomerUpdatedEvent();
    customerUpdatedEvent.setSocialSecurityNumber(request.getSocialSecurityNumber());
    customerUpdatedEvent.setCustomerId(customerId);
    customerUpdatedEvent.setGuid(UUID.randomUUID().toString());
    customerUpdatedEvent.setTimeStamp(LocalDateTime.now().toString());
    kafkaTemplate.send(KAFKA_SUMMIT_EVENT_TOPIC, customerUpdatedEvent.getGuid(), customerUpdatedEvent);
  }

  @RequestMapping(value = "set-address", method = POST)
  public void setAddress(@RequestBody CreateAddressRequest request) {
    AddressSetEvent addressSetEvent = new AddressSetEvent();
    addressSetEvent.setAddressRow(request.getAddressRow());
    addressSetEvent.setCity(request.getCity());
    addressSetEvent.setCountry(request.getCountry());
    addressSetEvent.setZipCode(request.getZipCode());
    addressSetEvent.setCustomerId(request.getCustomerId());
    addressSetEvent.setGuid(UUID.randomUUID().toString());
    addressSetEvent.setTimeStamp(LocalDateTime.now().toString());
    kafkaTemplate.send(KAFKA_SUMMIT_EVENT_TOPIC, addressSetEvent.getGuid(), addressSetEvent);
  }
}
