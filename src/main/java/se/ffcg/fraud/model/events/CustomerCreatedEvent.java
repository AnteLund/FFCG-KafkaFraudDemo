package se.ffcg.fraud.model.events;

import org.springframework.data.annotation.TypeAlias;

@TypeAlias("CustomerCreatedEvent")
public class CustomerCreatedEvent extends CustomerEvent {
  String firstName;
  String lastName;
  String socialSecurityNumber;

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getSocialSecurityNumber() {
    return socialSecurityNumber;
  }

  public void setSocialSecurityNumber(String socialSecurityNumber) {
    this.socialSecurityNumber = socialSecurityNumber;
  }
}
