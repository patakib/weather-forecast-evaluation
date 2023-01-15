package com.none.weather.history.services;

import com.none.weather.history.models.entities.History;
import com.none.weather.history.models.entities.Location;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;

public interface HistoryService {

  Optional<History> findLast();

  void updateHistory() throws IOException;

  URL urlBuilder(History history, Location location);

}
