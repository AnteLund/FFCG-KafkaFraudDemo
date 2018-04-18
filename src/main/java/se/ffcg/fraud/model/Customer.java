package se.ffcg.fraud.model;

import java.util.ArrayList;
import java.util.List;

public class Customer {
  String customerId;
  String created;
  String firstName;
  String lastName;

  Address address;

  List<CustomerMetaData> customerMetaDataList = new ArrayList<>();

  public List<CustomerMetaData> getCustomerMetaDataList() {
    return customerMetaDataList;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public String getSocialSecurityNumber() {
    return socialSecurityNumber;
  }

  public void setSocialSecurityNumber(String socialSecurityNumber) {
    this.socialSecurityNumber = socialSecurityNumber;
  }

  String socialSecurityNumber;

  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

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

  public void addMetaData(CustomerMetaData customerMetaData) {
    customerMetaDataList.add(customerMetaData);
  }

  public String getCreated() {
    return created;
  }

  public void setCreated(String created) {
    this.created = created;
  }
}
