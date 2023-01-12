package com.none.weather.forecast.repositories;

import com.none.weather.forecast.documents.Daily;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DailyRepository extends MongoRepository<Daily, String> {
}
