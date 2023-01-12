package com.none.weather.history.models.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.sql.Timestamp;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "weather_data")
public class History {
  @Id
  private Long id;
  @Column(name = "datetime")
  private Timestamp dateTime;
  private Double temperature;
  @Column(name = "relative_humidity")
  private Double relativeHumidity;
  private Double precipitation;
  private Double snowfall;
  @Column(name = "cloudcover_total")
  private Double cloudCoverTotal;
  @Column(name = "wind_speed_10m")
  private Double windSpeed10m;
  @Column(name = "wind_direction_10m")
  private Double windDirection10m;
  @ManyToOne(cascade = CascadeType.ALL)
  @JsonBackReference()
  @JoinColumn(name = "city_id")
  private Location location;


  @Getter
  @Setter
  public static class Builder {
    private Long id;
    private Timestamp dateTime;
    private Double temp;
    private Double humid;
    private Double precip;
    private Double snow;
    private Double cloud;
    private Double windSpeed;
    private Double windDir;
    private Location location;

    public Builder withId(Long id) {
      this.id = id;
      return this;
    }

    public Builder withDateTime(Timestamp dateTime) {
      this.dateTime = dateTime;
      return this;
    }

    public Builder withTemp(Double temp) {
      this.temp = temp;
      return this;
    }

    public Builder withHumid(Double humid) {
      this.humid = humid;
      return this;
    }

    public Builder withPrecip(Double precip) {
      this.precip = precip;
      return this;
    }

    public Builder withSnow(Double snow) {
      this.snow = snow;
      return this;
    }

    public Builder withCloud(Double cloud) {
      this.cloud = cloud;
      return this;
    }

    public Builder withWindSpeed(Double windSpeed) {
      this.windSpeed = windSpeed;
      return this;
    }

    public Builder withWindDir(Double windDir) {
      this.windDir = windDir;
      return this;
    }

    public Builder withLocation(Location location) {
      this.location = location;
      return this;
    }

    public History build() {
      return new History(id, dateTime, temp, humid, precip, snow, cloud, windSpeed, windDir,
          location);
    }
  }


}
