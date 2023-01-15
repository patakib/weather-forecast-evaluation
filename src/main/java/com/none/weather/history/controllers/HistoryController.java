package com.none.weather.history.controllers;

import com.none.weather.history.services.HistoryService;
import java.io.IOException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HistoryController {
  private final HistoryService historyService;

  public HistoryController(HistoryService historyService) {
    this.historyService = historyService;
  }

  @GetMapping("/")
  public String renderPlaceholderH() {
    return "index";
  }

  @PostMapping("/fetch")
  public ResponseEntity<String> fetchHistory() throws IOException {
    historyService.updateHistory();
    return ResponseEntity.status(200).body("");
  }
}
