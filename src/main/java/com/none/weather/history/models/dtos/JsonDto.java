package com.none.weather.history.models.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.type.ZonedDateTimeType;

@Getter
@Setter
@NoArgsConstructor
public class JsonDto {
  @JsonAlias(value = "time")
  Timestamp[] times = new Timestamp[168];
  @JsonAlias(value = "temperature_2m")
  Double[] temps = new Double[168];
  @JsonAlias(value = "relativehumidity_2m")
  Double[] humids = new Double[168];
  @JsonAlias(value = "precipitation")
  Double[] precips = new Double[168];
  @JsonAlias(value = "snowfall")
  Double[] snows = new Double[168];
  @JsonAlias(value = "cloudcover")
  Double[] clouds = new Double[168];
  @JsonAlias(value = "windspeed_10m")
  Double[] winds = new Double[168];
  @JsonAlias(value = "winddirection_10m")
  Double[] directions = new Double[168];

/*  public JsonDto(String[] times, Double[] temps, Double[] humids, Double[] precips,
                 Double[] snows, Double[] clouds, Double[] winds, Double[] directions) {
    this.times = convert(times);
    this.temps = temps;
    this.humids = humids;
    this.precips = precips;
    this.snows = snows;
    this.clouds = clouds;
    this.winds = winds;
    this.directions = directions;
  }*/

  private Timestamp[] convert(String[] array) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    return Arrays.stream(array)
        .map(formatter::parse)
        .map(LocalDateTime::from)
        .map(timestamp -> timestamp.atZone(ZoneId.systemDefault()))
        .map(zdt -> zdt.toInstant().getEpochSecond())
        .map(Timestamp::new)
        .toArray(Timestamp[]::new);
  }

}
