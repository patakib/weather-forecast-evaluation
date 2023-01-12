package com.none.weather.forecast.documents;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExpandedMetrics {

  private Integer weatherCode;
  private Double tempMax;
  private Double tempMin;
  private String sunrise;
  private String sunset;
  private Double precipSum;
  private Double snowSum;
  private Double precipHours;

  @Getter
  @Setter
  public static class Builder {
    private Integer weatherCode;
    private Double tempMax;
    private Double tempMin;
    private String sunrise;
    private String sunset;
    private Double precipSum;
    private Double snowSum;
    private Double precipHours;


    public Builder withWeatherCode(Integer weatherCode) {
      this.weatherCode = weatherCode;
      return this;
    }

    public Builder withTempMax(Double tempMax) {
      this.tempMax = tempMax;
      return this;
    }

    public Builder withTempMin(Double tempMin) {
      this.tempMin = tempMin;
      return this;
    }

    public Builder withSunrise(String sunrise) {
      this.sunrise = sunrise;
      return this;
    }

    public Builder withSunset(String sunset) {
      this.sunset = sunset;
      return this;
    }

    public Builder withPrecipSum(Double precipSum) {
      this.precipSum = precipSum;
      return this;
    }

    public Builder withSnowSum(Double snowSum) {
      this.snowSum = snowSum;
      return this;
    }

    public Builder withPrecipHours(Double precipHours) {
      this.precipHours = precipHours;
      return this;
    }

    public ExpandedMetrics build() {
      return new ExpandedMetrics(weatherCode, tempMax, tempMin, sunrise, sunset,
          precipSum, snowSum, precipHours);
    }
  }
}
