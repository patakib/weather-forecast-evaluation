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
public class JsonDtoDaily {
  @JsonAlias(value = "time")
  private String[] times = new String[3];
  @JsonAlias(value = "weathercode")
  private Integer[] weatherCode = new Integer[3];
  @JsonAlias(value = "temperature_2m_max")
  private Double[] tempMax = new Double[3];
  @JsonAlias(value = "temperature_2m_min")
  private Double[] tempMin = new Double[3];
  private String[] sunrise = new String[3];
  private String[] sunset = new String[3];
  @JsonAlias(value = "precipitation_sum")
  private Double[] precipSum = new Double[3];
  @JsonAlias(value = "snowfall_sum")
  private Double[] snowSum = new Double[3];
  @JsonAlias(value = "precipitation_hours")
  private Double[] precipHours = new Double[3];
}