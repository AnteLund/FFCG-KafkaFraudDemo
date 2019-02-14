package se.ffcg.fraud.model.rest;

public class CustomerUpdateRequest {
  String socialSecurityNumber;

  public String getSocialSecurityNumber() {
    return socialSecurityNumber;
  }

  public void setSocialSecurityNumber(String socialSecurityNumber) {
    this.socialSecurityNumber = socialSecurityNumber;
  }
}
