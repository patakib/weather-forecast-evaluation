package com.none.weather.forecast.documents;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@Document(collection = "forecast_hourly")
public class Hourly {
  @Id
  private String id;
  private String requestDate;
  private String targetDate;
  private List<Metrics> metrics;
  private ObjectId location;

  public Hourly() {
    this.metrics = new ArrayList<>();
  }

  @Getter
  @Setter
  public static class Builder {
    private String id;
    private String requestDate;
    private String targetDate;
    private List<Metrics> metrics;
    private ObjectId location;

    public Builder withId(String id) {
      this.id = id;
      return this;
    }

    public Builder withRequestDate(String requestDate) {
      this.requestDate = requestDate;
      return this;
    }

    public Builder withTargetDate(String targetDate) {
      this.targetDate = targetDate;
      return this;
    }

    public Builder withMetrics(List<Metrics> metrics) {
      this.metrics = new ArrayList<>();
      this.setMetrics(metrics);
      return this;
    }

    public Builder withLocationId(String location) {
      this.location = new ObjectId(location);
      return this;
    }

    public Hourly build() {
      return new Hourly(id, requestDate, targetDate, metrics, location);
    }
  }
}
