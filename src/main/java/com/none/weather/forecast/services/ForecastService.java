package com.none.weather.forecast.services;

import java.io.IOException;

public interface ForecastService {
  void updateHourly() throws IOException;

  void updateDaily() throws IOException;
}
