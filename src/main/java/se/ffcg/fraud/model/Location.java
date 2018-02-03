package se.ffcg.fraud.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Location {
  private Double lat;

  private Double lng;

  public void setLat(Double lat) {
    this.lat = lat;
  }

  @JsonProperty("lng")
  public void setLng(Double lng) {
    this.lng = lng;
  }

  public Double getLat() {
    return lat;
  }

  @JsonProperty("lon")
  public Double getLng() {
    return lng;
  }
}


