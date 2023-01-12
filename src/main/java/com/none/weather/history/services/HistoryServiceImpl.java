package com.none.weather.history.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.none.weather.history.models.dtos.JsonDto;
import com.none.weather.history.models.entities.History;
import com.none.weather.history.models.entities.Location;
import com.none.weather.history.repositories.HistoryRepository;
import com.none.weather.history.repositories.LocationRepository;
import com.none.weather.utils.Url;
import com.none.weather.utils.Utils;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;
import lombok.var;
import org.springframework.stereotype.Service;

@Service
public class HistoryServiceImpl implements HistoryService {
  private final HistoryRepository historyRepository;
  private final LocationRepository locationRepository;
  private final Utils utils = new Utils();

  public HistoryServiceImpl(HistoryRepository historyRepository,
                            LocationRepository locationRepository) {
    this.historyRepository = historyRepository;
    this.locationRepository = locationRepository;
  }

  @Override
  public Optional<History> findLast() {
    return historyRepository.findFirstByOrderByIdDesc();
  }

  @Override
  public void updateHistory() throws IOException {
    History anchor =
        findLast().orElseThrow(() -> new NoSuchElementException("Couldn't find last entry"));

    Set<Location> locations = locationRepository.findAll();

    ObjectMapper mapper = new ObjectMapper();
    locations.stream().forEach(location -> {
      findLast().ifPresent(history -> {
        try {
          JsonNode incoming = sendRequest(anchor, location).findPath("hourly");
          JsonDto jsonDto = mapper.treeToValue(incoming, JsonDto.class);
          writeHistory(jsonDto, history, Optional.ofNullable(location));
        } catch (IOException e) {
          throw new UncheckedIOException(e);
        }
      });
    });
  }

  private void writeHistory(JsonDto jsonDto, History oldHistory, Optional<Location> location) {
    IntStream.range(0, jsonDto.getTimes().length).forEach(i -> {
      History history = new History.Builder()
          .withId(oldHistory.getId() + (long) (i + 1))
          .withDateTime(Timestamp.from(jsonDto.getTimes()[i].toInstant()))
          .withTemp(jsonDto.getTemps()[i])
          .withHumid(jsonDto.getHumids()[i])
          .withPrecip(jsonDto.getPrecips()[i])
          .withSnow(jsonDto.getSnows()[i])
          .withCloud(jsonDto.getClouds()[i])
          .withWindSpeed(jsonDto.getWinds()[i])
          .withWindDir(jsonDto.getDirections()[i])
          .withLocation(location.orElse(null))
          .build();
      historyRepository.save(history);
    });
  }


  @Override
  public URL urlBuilder(History anchor, Location location) {
    return Optional.ofNullable(anchor).filter(a -> a.getDateTime() != null)
        .flatMap(a -> Optional.ofNullable(location).filter(l -> l.getLatitude() != null
            && l.getLongitude() != null).map(l -> {
          try {
            Url url = new Url("https://archive-api.open-meteo.com/v1/era5?");
            return url
                .addParam("latitude", String.valueOf(l.getLatitude()))
                .addParam("longitude", String.valueOf(l.getLongitude()))
                .addParam("start_date", utils.addDaysForRequest(anchor, 1))
                .addParam("end_date", utils.addDaysForRequest(anchor, 7))
                .addParam("hourly", "temperature_2m,relativehumidity_2m,precipitation,snowfall,"
                    + "cloudcover,windspeed_10m,winddirection_10m")
                .build();
          } catch (MalformedURLException e) {
            throw new RuntimeException(e);
          }
        })).orElseThrow(() -> new IllegalArgumentException("Date or location data missing"));
  }

  private JsonNode get(URL url) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(url, new TypeReference<JsonNode>() {
    });
  }

  private JsonNode sendRequest(History anchor, Location location) throws IOException {
    var url = urlBuilder(anchor, location);
    var jsonNode = get(url);
    return jsonNode;
  }

}
