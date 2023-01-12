package com.none.weather.forecast.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.none.weather.forecast.documents.Daily;
import com.none.weather.forecast.documents.ExpandedMetrics;
import com.none.weather.forecast.documents.Hourly;
import com.none.weather.forecast.documents.Location;
import com.none.weather.forecast.documents.Metrics;
import com.none.weather.forecast.documents.dtos.JsonDtoDaily;
import com.none.weather.forecast.documents.dtos.JsonDtoHourly;
import com.none.weather.forecast.repositories.DailyRepository;
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
  private final DailyRepository dailyRepository;
  private final LocationRepositoryMongo locationRepository;
  private final Utils utils = new Utils();

  @Autowired
  public ForecastServiceImpl(HourlyRepository hourlyRepository,
                             DailyRepository dailyRepository,
                             LocationRepositoryMongo locationRepository) {
    this.hourlyRepository = hourlyRepository;
    this.dailyRepository = dailyRepository;
    this.locationRepository = locationRepository;
  }

  @Override
  public void updateDaily() {
    List<Location> locations = locationRepository.findAll();
    ObjectMapper mapper = new ObjectMapper();
    locations.stream().forEach(location -> {
      try {
        JsonNode incoming = sendRequest(location, 'd').findPath("daily");
        JsonDtoDaily jsonDtoDaily = mapper.readValue(incoming.toString(),JsonDtoDaily.class);
        writeDaily(jsonDtoDaily, Optional.ofNullable(location));
        System.out.println("sdfgsdgs");
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    });
  }

  private void writeDaily(JsonDtoDaily jsonDtoDaily, Optional<Location> location) {
    Timestamp requestDate = Timestamp.from(Instant.now());
    Location loc = location.orElse(null);
    Stream.of(jsonDtoDaily.getTimes())
        .forEach(time -> {
          int i = ArrayUtils.indexOf(jsonDtoDaily.getTimes(), time);

            dailyRepository.save(
                new Daily.Builder()
                    .withId(null)
                    .withRequestDate(utils.addDaysForRequest(requestDate, 0))
                    .withTargetDate(jsonDtoDaily.getTimes()[i])
                    .withMetrics( new ExpandedMetrics.Builder()
                        .withWeatherCode(jsonDtoDaily.getWeatherCode()[i])
                        .withTempMax(jsonDtoDaily.getTempMax()[i])
                        .withTempMin(jsonDtoDaily.getTempMin()[i])
                        .withSunrise(jsonDtoDaily.getSunrise()[i])
                        .withSunset(jsonDtoDaily.getSunset()[i])
                        .withPrecipSum(jsonDtoDaily.getPrecipSum()[i])
                        .withSnowSum(jsonDtoDaily.getSnowSum()[i])
                        .withPrecipHours(jsonDtoDaily.getPrecipHours()[i])
                        .build())
                    .withLocation(loc.getId())
                    .build());
            });
  }

  @Override
  public void updateHourly() throws IOException {

    List<Location> locations = locationRepository.findAll();

    ObjectMapper mapper = new ObjectMapper();
    locations.stream().forEach(location -> {
      try {
        JsonNode incoming = sendRequest(location, 'h').findPath("hourly");
        JsonDtoHourly jsonDtoHourly = mapper.readValue(incoming.toString(), JsonDtoHourly.class);
        writeHourly(jsonDtoHourly, Optional.ofNullable(location));
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    });
  }

  public URL UrlBuilder(Location location, Character type) {
    return Optional.ofNullable(location).filter(l -> l.getLatitude() != null
        && l.getLongitude() != null).map(l -> {
      try {
        Url url = new Url("https://api.open-meteo.com/v1/forecast?");
        if (type.equals('h')) {
          return url
              .addParam("latitude", String.valueOf(l.getLatitude()))
              .addParam("longitude", String.valueOf(l.getLongitude()))
              .addParam("start_date", utils.addDaysForRequest(Timestamp.from(Instant.now()), 1))
              .addParam("end_date", utils.addDaysForRequest(Timestamp.from(Instant.now()), 3))
              .addParam("timezone", "Europe%2FBerlin")
              .addParam("hourly", "temperature_2m,relativehumidity_2m,precipitation,snowfall,"
                  + "cloudcover,windspeed_10m,winddirection_10m")
              .build();
        } else if (type.equals('d')) {
        return url
            .addParam("latitude", String.valueOf(l.getLatitude()))
            .addParam("longitude", String.valueOf(l.getLongitude()))
            .addParam("start_date", utils.addDaysForRequest(Timestamp.from(Instant.now()), 4))
            .addParam("end_date", utils.addDaysForRequest(Timestamp.from(Instant.now()), 6))
            .addParam("timezone", "Europe%2FBerlin")
            .addParam("daily", "weathercode,temperature_2m_max,temperature_2m_min,sunrise," +
                "sunset,precipitation_sum,snowfall_sum,precipitation_hours")
            .build();
        } else throw new IllegalArgumentException("Type argument invalid");
      } catch (MalformedURLException e) {
        throw new RuntimeException(e);
      }
    }).orElseThrow(() -> new IllegalArgumentException("Location data missing"));
  }

  private List<Hourly> buildHourlies(JsonDtoHourly jsonDtoHourly, Location loc) {
    List<Hourly> hourlies = new ArrayList<>();
    Timestamp requestDate = Timestamp.from(Instant.now());
    List<Metrics> day1Metrics = new ArrayList<>();
    List<Metrics> day2Metrics = new ArrayList<>();
    List<Metrics> day3Metrics = new ArrayList<>();


    Stream.of(jsonDtoHourly.getTimes())
        .forEach(time -> {
          int i = ArrayUtils.indexOf(jsonDtoHourly.getTimes(), time);
          Metrics metrics = createMetrics(jsonDtoHourly, i);

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
        .withLocationId(loc.getId())
        .build());
    hourlies.add(new Hourly.Builder()
        .withId(null)
        .withRequestDate(utils.addDaysForRequest(requestDate, 0))
        .withTargetDate(utils.addDaysForRequest(requestDate, 2))
        .withMetrics(day2Metrics)
        .withLocationId(loc.getId())
        .build());
    hourlies.add(new Hourly.Builder()
        .withId(null)
        .withRequestDate(utils.addDaysForRequest(requestDate, 0))
        .withTargetDate(utils.addDaysForRequest(requestDate, 3))
        .withMetrics(day3Metrics)
        .withLocationId(loc.getId())
        .build());
    return hourlies;
  }

  private Metrics createMetrics(JsonDtoHourly jsonDtoHourly, int i) {
    String timestamp = jsonDtoHourly.getTimes()[i];
    Double temp = jsonDtoHourly.getTemps()[i];
    Double humid = jsonDtoHourly.getHumids()[i];
    Double precip = jsonDtoHourly.getPrecips()[i];
    Double snow = jsonDtoHourly.getSnows()[i];
    Double cloud = jsonDtoHourly.getClouds()[i];
    Double windSpeed = jsonDtoHourly.getWinds()[i];
    Double windDir = jsonDtoHourly.getDirections()[i];

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

  private JsonNode sendRequest(Location location, Character type) throws IOException {
    if (type.equals('h')) {
      URL url = UrlBuilder(location,'h');
      var jsonNode = get(url);
      return jsonNode;
    } else if (type.equals('d')) {
      URL url = UrlBuilder(location,'d');
      var jsonNode = get(url);
      return jsonNode;
    } else throw new IllegalArgumentException("Type argument invalid");
  }

  private void writeHourly(JsonDtoHourly jsonDtoHourly, Optional<Location> location) {

    Location loc = location.orElse(null);
    List<Hourly> hourlies = buildHourlies(jsonDtoHourly, loc);

    for (Hourly h : hourlies
    ) {
      hourlyRepository.save(h);
    }

  }
}
