package com.none.weather.utils;

import com.none.weather.history.models.entities.History;
import com.none.weather.history.models.entities.Location;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Url {
  private final String baseUrl;
  private final java.util.Map<String, String> params;
  private final Utils utils = new Utils();

  public Url(String baseUrl) {
    this.baseUrl = baseUrl;
    params = new HashMap<>();
  }

  public Url addParam(String key, String value) {
    params.put(key, value);
    return this;
  }

  public URL build() throws MalformedURLException {
    StringBuilder queryString = new StringBuilder(baseUrl);
    for (Map.Entry<String, String> entry : params.entrySet()) {
      if (queryString.length() > 0) {
        queryString.append("&");
      }
      queryString.append(entry.getKey());
      queryString.append("=");
      queryString.append(entry.getValue());
    }
    return new URL(queryString.toString());
  }

}




