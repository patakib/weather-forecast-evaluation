package com.none.weather.forecast.documents;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Metrics {
  private String time;
  private Double temp;
  private Double humid;
  private Double precip;
  private Double snow;
  private Double cloud;
  private Double windSpeed;
  private Double windDir;

  @Getter
  @Setter
  public static class Builder {
    private String time;
    private Double temp;
    private Double humid;
    private Double precip;
    private Double snow;
    private Double cloud;
    private Double windSpeed;
    private Double windDir;

    public Builder withTime(String time) {
      this.time = time;
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

    public Metrics build() {
      return new Metrics(time, temp, humid, precip, snow, cloud, windSpeed, windDir);
    }
  }
}

