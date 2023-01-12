package com.none.weather.forecast.controllers;

import com.none.weather.forecast.services.ForecastService;
import java.io.IOException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ForecastController {
  private final ForecastService forecastService;

  public ForecastController(ForecastService forecastService) {
    this.forecastService = forecastService;
  }

  @PostMapping("/fetchH")
  public ResponseEntity<String> fetchHourly() throws IOException {
    forecastService.updateHourly();
    return ResponseEntity.status(200).body("");
  }

  @PostMapping("/fetchD")
  public ResponseEntity<String> fetchDaily() throws IOException {
    forecastService.updateDaily();
    return ResponseEntity.status(200).body("");
  }
}
