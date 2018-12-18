package com.infinitec.weathermetrics.repository;


import com.infinitec.weathermetrics.model.WeatherMetric;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface WeatherMetricRepository extends CrudRepository<WeatherMetric, Long> {
    Optional<WeatherMetric> findByCityAndMetricDate(String city, Long nextNthDay);
}
