package se.ffcg.fraud.model;

public class CustomerMetaData {
  String id;
  String type;
  String value;

  public String getId() {
    return id;
  }

  public String getValue() {
    return value;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getType() {
    return type;
  }
}
