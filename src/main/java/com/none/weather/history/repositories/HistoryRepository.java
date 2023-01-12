package com.none.weather.history.repositories;

import com.none.weather.history.models.entities.History;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History, Integer> {

  Optional<History> findFirstByOrderByIdDesc();

}
