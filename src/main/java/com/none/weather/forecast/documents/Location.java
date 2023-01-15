package com.none.weather.forecast.documents;

import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "locations")
public class Location {
  @Id
  private String id;
  private String cityName;
  private String longitude;
  private String latitude;
}
