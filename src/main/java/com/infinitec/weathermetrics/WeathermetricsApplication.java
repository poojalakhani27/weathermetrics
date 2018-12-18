package com.infinitec.weathermetrics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WeathermetricsApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeathermetricsApplication.class, args);
    }

}

