package com.none.weather.forecast.repositories;

import com.none.weather.forecast.documents.Hourly;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HourlyRepository extends MongoRepository<Hourly, String> {
}
