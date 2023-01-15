package com.none.weather.forecast.documents;

import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@Document(collection = "forecast_daily")
public class Daily {
  @Id
  private String id;
  private String requestDate;
  private String targetDate;
  private ExpandedMetrics metrics;
  private ObjectId location;

  @Getter
  @Setter
  public static class Builder {
    private String id;
    private String requestDate;
    private String targetDate;
    private ExpandedMetrics metrics;
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

    public Builder withMetrics(ExpandedMetrics metrics) {
      this.metrics = metrics;
      return this;
    }

    public Builder withLocation(String location) {
      this.location = new ObjectId(location);
      return this;
    }
    public Daily build() {
      return new Daily(id, requestDate, targetDate, metrics, location);
    }
  }
}
