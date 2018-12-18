package com.infinitec.weathermetrics.controller;

import com.infinitec.weathermetrics.model.WeatherMetric;
import com.infinitec.weathermetrics.service.WeatherMetricsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/data")
public class WeatherMetricsController {

    @Autowired
    private WeatherMetricsService weatherMetricsService;

    @GetMapping
    public List<WeatherMetric> getWeatherMetrics(@RequestParam("CITYNAME") String cityname) {
        List<WeatherMetric> metrics = weatherMetricsService.getMetrics(cityname);
        return metrics;

    }

    @ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity handleUserNotFoundException() {
        return new ResponseEntity<>("City name is invalid. Please refer to http://bulk.openweathermap.org/sample/city.list.json.gz for valid city names", HttpStatus.BAD_REQUEST);
    }

}
