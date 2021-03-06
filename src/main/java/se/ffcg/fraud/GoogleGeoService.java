package se.ffcg.fraud;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import se.ffcg.fraud.model.AddressResponse;
import se.ffcg.fraud.model.Location;

@Service
public class GoogleGeoService {

  private final RestTemplate restTemplate;

  public GoogleGeoService(RestTemplateBuilder restTemplateBuilder) {
    this.restTemplate = restTemplateBuilder.build();
  }

  public Location getLocation(String zipCode, String city, String country) {
    AddressResponse addressResponse = this.restTemplate.getForObject(buildUrl(zipCode, city, country), AddressResponse.class);
    if(addressResponse.getResults().isEmpty()) {
      return Location.MISSING_LOCATION;
    }
    return addressResponse.getResults().get(0).getGeometry().getLocation();
  }

  private String buildUrl(String zipCode, String city, String country) {
    String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + zipCode + "+" + city + "+" + country + "&key=AIzaSyCoKV4b3rj348XX-nD3wIddNH4hp0rqLsU";
    System.out.println(url);
    return url;
  }
}
