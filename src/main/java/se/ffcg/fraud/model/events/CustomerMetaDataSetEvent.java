package se.ffcg.fraud.model.events;

import org.springframework.data.annotation.TypeAlias;

@TypeAlias("CustomerMetaDataSetEvent")
public class CustomerMetaDataSetEvent extends CustomerEvent {
  String type;
  String value;

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
}
