package com.none.weather.forecast.documents.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JsonDtoHourly {
  @JsonAlias(value = "time")
  private String[] times = new String[72];
  @JsonAlias(value = "temperature_2m")
  private Double[] temps = new Double[72];
  @JsonAlias(value = "relativehumidity_2m")
  private Double[] humids = new Double[72];
  @JsonAlias(value = "precipitation")
  private Double[] precips = new Double[72];
  @JsonAlias(value = "snowfall")
  private Double[] snows = new Double[72];
  @JsonAlias(value = "cloudcover")
  private Double[] clouds = new Double[72];
  @JsonAlias(value = "windspeed_10m")
  private Double[] winds = new Double[72];
  @JsonAlias(value = "winddirection_10m")
  private Double[] directions = new Double[72];

}
