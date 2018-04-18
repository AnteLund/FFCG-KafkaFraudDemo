package se.ffcg.fraud.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.ffcg.fraud.model.Address;
import se.ffcg.fraud.model.Customer;
import se.ffcg.fraud.model.CustomerMetaData;
import se.ffcg.fraud.model.events.AddressSetEvent;
import se.ffcg.fraud.model.events.CustomerCreatedEvent;
import se.ffcg.fraud.model.events.CustomerEvent;
import se.ffcg.fraud.model.events.CustomerMetaDataSetEvent;
import se.ffcg.fraud.model.events.CustomerUpdatedEvent;

public final class CustomerEventMerger {

  Logger log = LoggerFactory.getLogger(CustomerEventMerger.class);

  public Customer merge(Customer current, CustomerEvent event) {
    if (event.getCustomerId() == null) {
      log.error("Event {} didn't have any ID", event);
      return current;
    }

    // Nya kunder - TODO måste lägga till prospect
    if (event instanceof CustomerCreatedEvent) {
      //Betyder ingen entry i tabellen
      if (current.getCustomerId() != null) {
        log.warn("Customer {} already created, skipping PhysicalPersonCreatedEvent {}",
            current.getCustomerId(), event.getGuid());
        return current;
      }
      current = createPhysicalPerson((CustomerCreatedEvent) event);
      logMerging("Created customer: {} - {}", current.getCustomerId(), "created");
      return current;
    }


    /*
    checkArgument(current.getCustomerId().equals(event.getKimCustomerId()),
        String.format("Not the same customer, table: %d, event: %d", current.getKimCustomerId(),
            event.getKimCustomerId()));
     */

    if (event instanceof AddressSetEvent) {
      logMerging("Merging {} with {}", current.getCustomerId(), event.getClass().getSimpleName());
      current = merge(current, (AddressSetEvent) event);
    } else if (event instanceof CustomerMetaDataSetEvent) {
      logMerging("Merging {} with {}", current.getCustomerId(), event.getClass().getSimpleName());
      current = merge(current, (CustomerMetaDataSetEvent) event);
    } else if (event instanceof CustomerUpdatedEvent) {
      logMerging("Merging {} with {}", current.getCustomerId(), event.getClass().getSimpleName());
      current = merge(current, (CustomerUpdatedEvent) event);
    } else {
      log.info("No merge-logic, skipping {}", event.getClass().getSimpleName());
    }

    return current;
  }

  private void logMerging(String mess, String customerId, String className) {
    log.info(mess, customerId, className);
  }

  private Customer merge(Customer current, CustomerUpdatedEvent event) {
    // Här är vi bara intresserade av externa IDn. Bygg ut i riktig impl.
    //current.setExternalCustomerIds(map(event.getExternalCustomerIds()));
    current.setSocialSecurityNumber(event.getSocialSecurityNumber());
    return current;
  }


  private Customer merge(Customer current, AddressSetEvent event) {
    Address address = new Address();
    address.setAddress(event.getAddressRow());
    address.setZipCode(event.getZipCode());
    address.setCity(event.getCity());
    address.setCountry(event.getCountry());
    address.setUpdated(event.getTimeStamp());
    if(current.getAddress() == null) {
      address.setCreated(event.getTimeStamp());
    } else {
      address.setCreated(current.getAddress().getCreated());
    }
    current.setAddress(address);
    return current;
  }

  private Customer merge(Customer current, CustomerMetaDataSetEvent event) {
    // Sätt ny
    //Bug här, createdAt blir now() och inte det som står i eventet...
    //currentMetadataId + 1 är kod för att tracka antalet gånger denna blir anropad, aggregera upp id
    //current.addMetaData(event.getMetaDataType(), event.getValue(), currentMetadataId + 1);
    CustomerMetaData customerMetaData = new CustomerMetaData();
    customerMetaData.setType(event.getType());
    customerMetaData.setValue(event.getValue());
    customerMetaData.setId(event.getGuid());
    current.addMetaData(customerMetaData);

    return current;
  }

  private Customer createPhysicalPerson(CustomerCreatedEvent event) {
    Customer customer = new Customer();
    customer.setCustomerId(event.getCustomerId());

    //mappar endast delar som kan finnas i CustomerCreatedEvent
    customer.setFirstName(event.getFirstName());
    customer.setLastName(event.getLastName());
    customer.setSocialSecurityNumber(event.getSocialSecurityNumber());
    return customer;
  }
}
