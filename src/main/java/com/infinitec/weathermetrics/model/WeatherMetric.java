package com.infinitec.weathermetrics.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "weather_metrics")
public class WeatherMetric {
    @Id
    @GeneratedValue
    private Long id;
    private String city;
    private Long metricDate;
    private Float avgTempDaily;
    private Float avgTempNightly;
    private Float avgPressure;

    public WeatherMetric() {
    }

    public WeatherMetric(String city, Long metricDate, Float avgTempDaily, Float avgTempNightly, Float avgPressure) {
        this.city = city;
        this.metricDate = metricDate;
        this.avgTempDaily = avgTempDaily;
        this.avgTempNightly = avgTempNightly;
        this.avgPressure = avgPressure;
    }

    public Long getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public Long getMetricDate() {
        return metricDate;
    }

    public Float getAvgTempDaily() {
        return avgTempDaily;
    }

    public Float getAvgTempNightly() {
        return avgTempNightly;
    }

    public Float getAvgPressure() {
        return avgPressure;
    }
}