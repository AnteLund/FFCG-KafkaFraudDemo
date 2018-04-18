package se.ffcg.fraud.model.rest;

public class CreateMetadataRequest {
  String type;
  String value;
  String customerId;

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getCustomerId() {
    return customerId;
  }
}
