package se.ffcg.fraud.model.events;

public class CustomerUpdatedEvent extends CustomerEvent {
  String socialSecurityNumber;

  public String getSocialSecurityNumber() {
    return socialSecurityNumber;
  }

  public void setSocialSecurityNumber(String socialSecurityNumber) {
    this.socialSecurityNumber = socialSecurityNumber;
  }
}
