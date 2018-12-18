package com.infinitec.weathermetrics.service;

import com.infinitec.weathermetrics.external.OpenWeatherMapApi;
import com.infinitec.weathermetrics.external.OpenWeatherMapApiResponse;
import com.infinitec.weathermetrics.external.PeriodicForecast;
import com.infinitec.weathermetrics.model.WeatherMetric;
import com.infinitec.weathermetrics.repository.WeatherMetricRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

import static com.infinitec.weathermetrics.utils.DateUtils.*;
import static java.util.stream.Collectors.toList;

@Service
public class WeatherMetricsService {

    private WeatherMetricRepository weatherMetricRepository;
    private OpenWeatherMapApi openWeatherMapApi;

    @Value("${openweathermap.apikey}")
    private String apiKey;

    @Autowired
    public WeatherMetricsService(WeatherMetricRepository weatherMetricRepository, OpenWeatherMapApi openWeatherMapApi) {
        this.weatherMetricRepository = weatherMetricRepository;
        this.openWeatherMapApi = openWeatherMapApi;
    }

    public List<WeatherMetric> getMetrics(String city) {
        IntStream nextThreeDays = IntStream.rangeClosed(1, 3);
        return nextThreeDays
                .mapToObj(n -> {
                    long midnightOfNthDay = midnightOfNthDay(n).getTime() / 1000;
                    return weatherMetricRepository.findByCityAndMetricDate(city, midnightOfNthDay)
                            .orElseGet(() -> fetchAndStore(city, n));
                }).collect(toList());
    }

    private WeatherMetric fetchAndStore(String city, int n) {
        OpenWeatherMapApiResponse response = fetch(city);
        WeatherMetric weatherMetric = toWeatherMetric(city, n, response);
        weatherMetricRepository.save(weatherMetric);
        return weatherMetric;
    }

    private WeatherMetric toWeatherMetric(String city, int n, OpenWeatherMapApiResponse response) {
        Double dailyAverage = averageDailyTemperature(response.getList(), n);
        Double nightlyAverage = averageNightlyTemperature(response.getList(), n);
        Double avgPressure = averagePressure(response.getList(), n);
        long midnightOfNthDay = midnightOfNthDay(n).getTime() / 1000;
        return new WeatherMetric(city, midnightOfNthDay, dailyAverage.floatValue(), nightlyAverage.floatValue(), avgPressure.floatValue());
    }

    private Double averagePressure(List<PeriodicForecast> list, int n) {
        return list.stream()
                .mapToDouble(i -> i.getMain().getPressure())
                .average().getAsDouble();
    }

    private Double averageNightlyTemperature(List<PeriodicForecast> list, int n) {
        return list.stream()
                .filter(i -> isDuringNightTime(i, n))
                .mapToDouble(i -> i.getMain().getTemp())
                .average().getAsDouble();
    }

    private Double averageDailyTemperature(List<PeriodicForecast> list, int n) {
        return list.stream()
                .filter(i -> isDuringDayTime(i, n))
                .mapToDouble(i -> i.getMain().getTemp())
                .average().getAsDouble();
    }

    private boolean isDuringDayTime(PeriodicForecast i, int n) {
        Date startOfNthDay = startOfNthDay(n);
        Date endOfNthDay = endOfNthDay(n);
        return i.getDt() >= startOfNthDay.getTime() / 1000 && i.getDt() < endOfNthDay.getTime() / 1000;
    }

    private boolean isDuringNightTime(PeriodicForecast i, int n) {
        Date endOfDay = endOfNthDay(n);
        Date startOfNextDay = startOfNthDay(n + 1);
        return i.getDt() >= endOfDay.getTime() / 1000 && i.getDt() < startOfNextDay.getTime() / 1000;
    }


    private OpenWeatherMapApiResponse fetch(String city) {
        try {
            Call<OpenWeatherMapApiResponse> responseCall = openWeatherMapApi.getForecast(city, apiKey);
            if (responseCall == null)
                throw new IllegalArgumentException("City name is invalid");
            return responseCall.execute().body();
        } catch (IOException e) {
            throw new RuntimeException("Exception connecting to Open Weather Map Service", e);
        }
    }


}
