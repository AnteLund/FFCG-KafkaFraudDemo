package se.ffcg.fraud.model.events;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = CustomerCreatedEvent.class, name = "CustomerCreatedEvent"),
    @JsonSubTypes.Type(value = AddressSetEvent.class, name = "AddressSetEvent"),
    @JsonSubTypes.Type(value = CustomerMetaDataSetEvent.class, name = "CustomerMetaDataSetEvent"),
    @JsonSubTypes.Type(value = CustomerUpdatedEvent.class, name = "CustomerUpdatedEvent")
})
public abstract class CustomerEvent {
  String guid;
  String customerId;
  String timeStamp;

  public void setGuid(String guid) {
    this.guid = guid;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public String getGuid() {
    return guid;
  }

  public String getCustomerId() {
    return customerId;
  }

  public String getTimeStamp() {
    return timeStamp;
  }

  public void setTimeStamp(String timeStamp) {
    this.timeStamp = timeStamp;
  }
}
