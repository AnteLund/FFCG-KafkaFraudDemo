package se.ffcg.fraud.model.events;

public class AddressSetEvent extends CustomerEvent {
  String addressRow;
  String zipCode;
  String city;
  String country;

  public String getAddressRow() {
    return addressRow;
  }

  public void setAddressRow(String addressRow) {
    this.addressRow = addressRow;
  }

  public String getZipCode() {
    return zipCode;
  }

  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }
}
