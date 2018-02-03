package se.ffcg.fraud.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Location {
  public static final Location MISSING_LOCATION = createMissingLocation();

  private static Location createMissingLocation() {
    Location location = new Location();
    location.setLat(0d);
    location.setLng(0d);
    return location;
  }

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


