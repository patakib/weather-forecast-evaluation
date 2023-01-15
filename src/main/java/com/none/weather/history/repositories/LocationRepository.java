package com.none.weather.history.repositories;

import com.none.weather.history.models.entities.Location;
import java.util.Set;
import org.springframework.data.repository.CrudRepository;

public interface LocationRepository extends CrudRepository<Location, Integer> {
  Set<Location> findAll();
}
