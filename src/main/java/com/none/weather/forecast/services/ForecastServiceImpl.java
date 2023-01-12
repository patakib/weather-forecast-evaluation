package com.none.weather.forecast.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.none.weather.forecast.documents.Hourly;
import com.none.weather.forecast.documents.Location;
import com.none.weather.forecast.documents.Metrics;
import com.none.weather.forecast.documents.dtos.JsonDto;
import com.none.weather.forecast.repositories.HourlyRepository;
import com.none.weather.forecast.repositories.LocationRepositoryMongo;
import com.none.weather.utils.Url;
import com.none.weather.utils.Utils;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.var;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ForecastServiceImpl implements ForecastService {

  private final HourlyRepository hourlyRepository;
  private final LocationRepositoryMongo locationRepository;
  private final Utils utils = new Utils();

  @Autowired
  public ForecastServiceImpl(HourlyRepository hourlyRepository,
                             LocationRepositoryMongo locationRepository) {
    this.hourlyRepository = hourlyRepository;
    this.locationRepository = locationRepository;
  }

  @Override
  public void updateDaily() {
  }

  @Override
  public void updateHourly() throws IOException {

    List<Location> locations = locationRepository.findAll();

    ObjectMapper mapper = new ObjectMapper();
    locations.stream().forEach(location -> {
      try {
        JsonNode incoming = sendRequest(location).findPath("hourly");
        JsonDto jsonDto = mapper.readValue(incoming.toString(), JsonDto.class);
        writeHourly(jsonDto, Optional.ofNullable(location));
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    });
  }


  public URL urlBuilder(Location location) {
    return Optional.ofNullable(location).filter(l -> l.getLatitude() != null
        && l.getLongitude() != null).map(l -> {
      try {
        Url url = new Url("https://api.open-meteo.com/v1/forecast?");
        return url
            .addParam("latitude", String.valueOf(l.getLatitude()))
            .addParam("longitude", String.valueOf(l.getLongitude()))
            .addParam("start_date", utils.addDaysForRequest(Timestamp.from(Instant.now()), 1))
            .addParam("end_date", utils.addDaysForRequest(Timestamp.from(Instant.now()), 3))
            .addParam("timezone", "Europe%2FBerlin")
            .addParam("hourly", "temperature_2m,relativehumidity_2m,precipitation,snowfall,"
                + "cloudcover,windspeed_10m,winddirection_10m")
            .build();
      } catch (MalformedURLException e) {
        throw new RuntimeException(e);
      }
    }).orElseThrow(() -> new IllegalArgumentException("Location data missing"));
  }


  private List<Hourly> buildHourlies(JsonDto jsonDto, Location loc) {
    List<Hourly> hourlies = new ArrayList<>();
    Timestamp requestDate = Timestamp.from(Instant.now());
    List<Metrics> day1Metrics = new ArrayList<>();
    List<Metrics> day2Metrics = new ArrayList<>();
    List<Metrics> day3Metrics = new ArrayList<>();


    Stream.of(jsonDto.getTimes())
        .forEach(time -> {
          int i = ArrayUtils.indexOf(jsonDto.getTimes(), time);
          Metrics metrics = createMetrics(jsonDto, i);

          LocalDate date = LocalDateTime.parse(time).toLocalDate();
          LocalDate startDate = requestDate.toLocalDateTime().toLocalDate();

          if (date.isEqual(startDate.plusDays(1))) {
            day1Metrics.add(metrics);
          } else if (date.isEqual(startDate.plusDays(2))) {
            day2Metrics.add(metrics);
          } else if (date.isEqual(startDate.plusDays(3))) {
            day3Metrics.add(metrics);
          }
        });
    hourlies.add(new Hourly.Builder()
        .withId(null)
        .withRequestDate(utils.addDaysForRequest(requestDate, 0))
        .withTargetDate(utils.addDaysForRequest(requestDate, 1))
        .withMetrics(day1Metrics)
        .withLocation(loc)
        .build());
    hourlies.add(new Hourly.Builder()
        .withId(null)
        .withRequestDate(utils.addDaysForRequest(requestDate, 0))
        .withTargetDate(utils.addDaysForRequest(requestDate, 2))
        .withMetrics(day2Metrics)
        .withLocation(loc)
        .build());
    hourlies.add(new Hourly.Builder()
        .withId(null)
        .withRequestDate(utils.addDaysForRequest(requestDate, 0))
        .withTargetDate(utils.addDaysForRequest(requestDate, 3))
        .withMetrics(day3Metrics)
        .withLocation(loc)
        .build());
    return hourlies;
  }

  private Metrics createMetrics(JsonDto jsonDto, int i) {
    String timestamp = jsonDto.getTimes()[i];
    Double temp = jsonDto.getTemps()[i];
    Double humid = jsonDto.getHumids()[i];
    Double precip = jsonDto.getPrecips()[i];
    Double snow = jsonDto.getSnows()[i];
    Double cloud = jsonDto.getClouds()[i];
    Double windSpeed = jsonDto.getWinds()[i];
    Double windDir = jsonDto.getDirections()[i];

    return new Metrics.Builder()
        .withTime(timestamp)
        .withTemp(temp)
        .withHumid(humid)
        .withPrecip(precip)
        .withSnow(snow)
        .withCloud(cloud)
        .withWindSpeed(windSpeed)
        .withWindDir(windDir)
        .build();
  }


  private JsonNode get(URL url) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(url, new TypeReference<JsonNode>() {
    });
  }

  private JsonNode sendRequest(Location location) throws IOException {
    var url = urlBuilder(location);
    var jsonNode = get(url);
    return jsonNode;
  }

  private void writeHourly(JsonDto jsonDto, Optional<Location> location) {

    Location loc = location.orElse(null);
    List<Hourly> hourlies = buildHourlies(jsonDto, loc);

    for (Hourly h : hourlies
    ) {
      hourlyRepository.save(h);
    }

  }
}
