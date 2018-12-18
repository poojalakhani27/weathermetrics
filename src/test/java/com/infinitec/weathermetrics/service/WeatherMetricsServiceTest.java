package com.infinitec.weathermetrics.service;

import com.infinitec.weathermetrics.TestFixtures;
import com.infinitec.weathermetrics.external.OpenWeatherMapApi;
import com.infinitec.weathermetrics.external.OpenWeatherMapApiResponse;
import com.infinitec.weathermetrics.model.WeatherMetric;
import com.infinitec.weathermetrics.repository.WeatherMetricRepository;
import com.infinitec.weathermetrics.utils.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class WeatherMetricsServiceTest {
    @Mock
    private OpenWeatherMapApi openWeatherMapApi;
    @Mock
    private WeatherMetricRepository repository;

    private WeatherMetricsService underTest;
    @Before
    public void setUp() throws Exception {
        underTest = new WeatherMetricsService(repository, openWeatherMapApi);
    }

    @Test
    public void shouldGetMetricsFromAPIWhenNotFoundInDatastore() throws IOException {
        String cityName = "Hurzuf,UA";
        when(repository.findByCityAndMetricDate(eq(cityName), anyLong())).thenReturn(Optional.empty());
        Call mockCall = mock(Call.class);
        when(openWeatherMapApi.getForecast(cityName, null)).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(Response.success(TestFixtures.openWeatherMapApiResponse()));

        List<WeatherMetric> actual = underTest.getMetrics(cityName);

        assertEquals(3, actual.size());

        WeatherMetric firstDayMetrics = actual.get(0);
        assertEquals(cityName, firstDayMetrics.getCity());
        assertEquals(2.5F, firstDayMetrics.getAvgTempDaily().doubleValue(), 0.0F);
        assertEquals(4.5F, firstDayMetrics.getAvgTempNightly().doubleValue(), 0.0F);
        assertEquals(4.5F, firstDayMetrics.getAvgPressure().doubleValue(), 0.0F);

        WeatherMetric secondDayMetrics = actual.get(1);
        assertEquals(cityName, secondDayMetrics.getCity());
        assertEquals(3.5F, secondDayMetrics.getAvgTempDaily().doubleValue(), 0.0F);
        assertEquals(5.5F, secondDayMetrics.getAvgTempNightly().doubleValue(), 0.0F);
        assertEquals(4.5F, secondDayMetrics.getAvgPressure().doubleValue(), 0.0F);

        WeatherMetric thirdDayMetrics = actual.get(2);
        assertEquals(cityName, thirdDayMetrics.getCity());
        assertEquals(4.5F, thirdDayMetrics.getAvgTempDaily().doubleValue(), 0.0F);
        assertEquals(6.5F, thirdDayMetrics.getAvgTempNightly().doubleValue(), 0.0F);
        assertEquals(4.5F, thirdDayMetrics.getAvgPressure().doubleValue(), 0.0F);

        verify(repository).save(firstDayMetrics);
        verify(repository).save(secondDayMetrics);
        verify(repository).save(thirdDayMetrics);
    }

    @Test
    public void shouldNotGetMetricsFromAPIWhenFoundInDatastore() throws IOException {
        String cityName = "Hurzuf,UA";
        when(repository.findByCityAndMetricDate(eq(cityName), anyLong())).thenReturn(Optional.of(new WeatherMetric()));

        List<WeatherMetric> actual = underTest.getMetrics(cityName);

        //Verify no external API invocation
        verify(repository, times(0)).save(any(WeatherMetric.class));
        verify(openWeatherMapApi, times(0)).getForecast(eq(cityName), anyString());
    }
}