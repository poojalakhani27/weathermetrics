package com.infinitec.weathermetrics;

import com.infinitec.weathermetrics.external.Forecast;
import com.infinitec.weathermetrics.external.OpenWeatherMapApiResponse;
import com.infinitec.weathermetrics.external.PeriodicForecast;
import com.infinitec.weathermetrics.utils.DateUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TestFixtures {
    public static OpenWeatherMapApiResponse openWeatherMapApiResponse() {
        IntStream nextThreeDays = IntStream.rangeClosed(1, 3);
        List<PeriodicForecast> periodicForecasts = nextThreeDays
                .mapToObj(i -> forcastDataForNthDay(i))
                .flatMap(i -> i.stream()).collect(Collectors.toList());
        OpenWeatherMapApiResponse response = OpenWeatherMapApiResponse.builder().list(periodicForecasts).build();
        return response;
    }
    private static List<PeriodicForecast> forcastDataForNthDay(int n) {
        Forecast d1 = Forecast.builder().temp(1.0F + n).pressure(1.0F + n).build();
        Forecast d2 = Forecast.builder().temp(2.0F + n).pressure(2.0F + n).build();
        Forecast n1 = Forecast.builder().temp(3.0F + n).pressure(3.0F + n).build();
        Forecast n2 = Forecast.builder().temp(4.0F + n).pressure(4.0F + n).build();
        PeriodicForecast pfd1 = PeriodicForecast.builder()
                .dt(DateUtils.getNextNthDayWithTime(n,13, 1, 1).getTime() / 1000)
                .main(d1)
                .build();

        PeriodicForecast pfd2 = PeriodicForecast.builder()
                .dt(DateUtils.getNextNthDayWithTime(n, 14, 1, 1).getTime() / 1000)
                .main(d2)
                .build();

        PeriodicForecast pfn1 = PeriodicForecast.builder()
                .dt(DateUtils.getNextNthDayWithTime(n+1, 1, 1, 1).getTime() / 1000)
                .main(n1)
                .build();

        PeriodicForecast pfn2 = PeriodicForecast.builder()
                .dt(DateUtils.getNextNthDayWithTime(n+1, 2, 1, 1).getTime() / 1000)
                .main(n2)
                .build();

        return Stream.of(pfd1, pfd2, pfn1, pfn2).collect(Collectors.toList());
    }
}
