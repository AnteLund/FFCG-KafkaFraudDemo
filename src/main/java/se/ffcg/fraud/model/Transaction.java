package se.ffcg.fraud.model;

public class Transaction {
  private String id;
  private String timestamp;
  private String storeName;
  private String merchantCode;
  private String merchantId;
  private String zipCode;
  private String city;
  private String country;
  private String amount;
  private String riskScore;
  private Location location;

  public String getId() {
    return id;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public void setStoreName(String storeName) {
    this.storeName = storeName;
  }

  public void setMerchantCode(String merchantCode) {
    this.merchantCode = merchantCode;
  }

  public void setMerchantId(String merchantId) {
    this.merchantId = merchantId;
  }

  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }

  public String getStoreName() {
    return storeName;
  }

  public String getMerchantCode() {
    return merchantCode;
  }

  public String getMerchantId() {
    return merchantId;
  }

  public String getZipCode() {
    return zipCode;
  }

  public String getCity() {
    return city;
  }

  public String getCountry() {
    return country;
  }

  public String getAmount() {
    return amount;
  }


  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public String getRiskScore() {
    return riskScore;
  }

  public void setRiskScore(String riskScore) {
    this.riskScore = riskScore;
  }

}
