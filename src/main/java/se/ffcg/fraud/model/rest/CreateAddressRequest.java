package se.ffcg.fraud.model.rest;

public class CreateAddressRequest {
  private String customerId;
  private String addressRow;
  private String city;
  private String country;
  private String zipCode;

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public void setAddressRow(String addressRow) {
    this.addressRow = addressRow;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  public String getAddressRow() {
    return addressRow;
  }

  public String getCity() {
    return city;
  }

  public String getCountry() {
    return country;
  }

  public String getZipCode() {
    return zipCode;
  }

  public String getCustomerId() {
    return customerId;
  }
}
