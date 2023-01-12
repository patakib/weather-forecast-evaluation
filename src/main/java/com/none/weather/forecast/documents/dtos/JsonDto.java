package com.none.weather.forecast.documents.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JsonDto {
  @JsonAlias(value = "time")
  String[] times = new String[72];
  @JsonAlias(value = "temperature_2m")
  Double[] temps = new Double[72];
  @JsonAlias(value = "relativehumidity_2m")
  Double[] humids = new Double[72];
  @JsonAlias(value = "precipitation")
  Double[] precips = new Double[72];
  @JsonAlias(value = "snowfall")
  Double[] snows = new Double[72];
  @JsonAlias(value = "cloudcover")
  Double[] clouds = new Double[72];
  @JsonAlias(value = "windspeed_10m")
  Double[] winds = new Double[72];
  @JsonAlias(value = "winddirection_10m")
  Double[] directions = new Double[72];


  /*public JsonDto(String[] times, Double[] temps, Double[] humids, Double[] precips,
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

  /*private Timestamp[] convert(String[] array) {
    ZoneId timeZone = ZoneId.of("CET");
    DateTimeFormatter formatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm").withZone(timeZone);
    return Arrays.stream(array)
        .map(formatter::parse)
        .map(ZonedDateTime::from)
        .map(zdt -> zdt.toInstant().getEpochSecond())
        .map(Timestamp::new)
        .toArray(Timestamp[]::new);
  }*/
/*  private Timestamp[] convert(String[] array) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    ZoneId timeZone = ZoneId.of("Europe/Berlin");
    return Arrays.stream(array)
        .map(s -> LocalDateTime.parse(s, formatter))
        .map(ldt -> ZonedDateTime.of(ldt, timeZone))
        .map(zdt -> zdt.toInstant().getEpochSecond())
        .map(Timestamp::new)
        .toArray(Timestamp[]::new);
  }*/

}
