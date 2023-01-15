package com.none.weather.history.models.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "locations")
public class Location {
  @Id
  private Integer id;
  @Column(name = "city_name")
  private String cityName;
  private String longitude;
  private String latitude;
  @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
  @JsonManagedReference
  private Set<History> histories;

  public Location(Integer id, String latitude, String longitude, String cityName) {
    this.id = id;
    this.latitude = latitude;
    this.longitude = longitude;
    this.cityName = cityName;
  }

  @Getter
  @Setter
  public static class Builder {
    private Integer id;
    private String cityName;
    private String longitude;
    private String latitude;

    public Builder withId(Integer id) {
      this.id = id;
      return this;
    }

    public Builder withLatitude(String latitude) {
      this.latitude = latitude;
      return this;
    }

    public Builder withLongitude(String longitude) {
      this.longitude = longitude;
      return this;
    }

    public Builder withCityName(String cityName) {
      this.cityName = cityName;
      return this;
    }

    public Location build() {
      return new Location(id, latitude, longitude, cityName);
    }
  }
}
