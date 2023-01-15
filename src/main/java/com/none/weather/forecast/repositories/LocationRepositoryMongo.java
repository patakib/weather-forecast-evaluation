package com.none.weather.forecast.repositories;

import com.none.weather.forecast.documents.Location;
import java.util.List;
import java.util.Set;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LocationRepositoryMongo extends MongoRepository<Location, String> {

  @Override
  List<Location> findAll();
}
