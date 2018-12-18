package com.infinitec.weathermetrics;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infinitec.weathermetrics.external.OpenWeatherMapApi;
import com.infinitec.weathermetrics.external.OpenWeatherMapApiResponse;
import com.infinitec.weathermetrics.model.WeatherMetric;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import retrofit2.Call;
import retrofit2.Response;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class WeathermetricsIntegrationTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationContext;
    @MockBean
    private OpenWeatherMapApi openWeatherMapApi;

    private String validCityName = "Hurzuf,UA";
    private String InvalidCityName = "Invalid";

    @Autowired
    private Environment env;

    @Before
    public void setUp() throws Exception {
        OpenWeatherMapApiResponse response = TestFixtures.openWeatherMapApiResponse();
        Call mockCall = mock(Call.class);
        when(openWeatherMapApi.getForecast(eq(validCityName), anyString())).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(Response.success(response));
    }


    @Test
    public void shouldReturnMetricsFromExternalAPIForTheFirstTimeAndStore() throws Exception {
        String responseBody = mvc.perform(get("/data")
                .contentType(MediaType.APPLICATION_JSON)
                .param("CITYNAME", validCityName))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        List<WeatherMetric> actual = objectMapper.readValue(responseBody, new TypeReference<List<WeatherMetric>>() {
        });

        assertEquals(3, actual.size());

        WeatherMetric firstDayMetrics = actual.get(0);
        assertEquals(validCityName, firstDayMetrics.getCity());
        assertEquals(2.5F, firstDayMetrics.getAvgTempDaily().doubleValue(), 0.0F);
        assertEquals(4.5F, firstDayMetrics.getAvgTempNightly().doubleValue(), 0.0F);
        assertEquals(4.5F, firstDayMetrics.getAvgPressure().doubleValue(), 0.0F);

        WeatherMetric secondDayMetrics = actual.get(1);
        assertEquals(validCityName, secondDayMetrics.getCity());
        assertEquals(3.5F, secondDayMetrics.getAvgTempDaily().doubleValue(), 0.0F);
        assertEquals(5.5F, secondDayMetrics.getAvgTempNightly().doubleValue(), 0.0F);
        assertEquals(4.5F, secondDayMetrics.getAvgPressure().doubleValue(), 0.0F);

        WeatherMetric thirdDayMetrics = actual.get(2);
        assertEquals(validCityName, thirdDayMetrics.getCity());
        assertEquals(4.5F, thirdDayMetrics.getAvgTempDaily().doubleValue(), 0.0F);
        assertEquals(6.5F, thirdDayMetrics.getAvgTempNightly().doubleValue(), 0.0F);
        assertEquals(4.5F, thirdDayMetrics.getAvgPressure().doubleValue(), 0.0F);

    }

    @Test
    public void shouldReturnBadRequestWhenCitynameInvald() throws Exception {
        String responseBody = mvc.perform(get("/data")
                .contentType(MediaType.APPLICATION_JSON)
                .param("CITYNAME", InvalidCityName))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        assertEquals("City name is invalid. Please refer to http://bulk.openweathermap.org/sample/city.list.json.gz for valid city names", responseBody);

    }

    @Test
    public void shouldReturnMetricsFromLocalStoreOnConsecutiveRequests() throws Exception {
        //First request
        mvc.perform(get("/data")
                .contentType(MediaType.APPLICATION_JSON)
                .param("CITYNAME", validCityName))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();


        //Second request
        String responseBody = mvc.perform(get("/data")
                .contentType(MediaType.APPLICATION_JSON)
                .param("CITYNAME", validCityName))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //Verify not fetched from external API
        verifyNoMoreInteractions(openWeatherMapApi);


        ObjectMapper objectMapper = new ObjectMapper();
        List<WeatherMetric> actual = objectMapper.readValue(responseBody, new TypeReference<List<WeatherMetric>>() {
        });

        assertEquals(3, actual.size());

        WeatherMetric firstDayMetrics = actual.get(0);
        assertEquals(validCityName, firstDayMetrics.getCity());
        assertEquals(2.5F, firstDayMetrics.getAvgTempDaily().doubleValue(), 0.0F);
        assertEquals(4.5F, firstDayMetrics.getAvgTempNightly().doubleValue(), 0.0F);
        assertEquals(4.5F, firstDayMetrics.getAvgPressure().doubleValue(), 0.0F);

        WeatherMetric secondDayMetrics = actual.get(1);
        assertEquals(validCityName, secondDayMetrics.getCity());
        assertEquals(3.5F, secondDayMetrics.getAvgTempDaily().doubleValue(), 0.0F);
        assertEquals(5.5F, secondDayMetrics.getAvgTempNightly().doubleValue(), 0.0F);
        assertEquals(4.5F, secondDayMetrics.getAvgPressure().doubleValue(), 0.0F);

        WeatherMetric thirdDayMetrics = actual.get(2);
        assertEquals(validCityName, thirdDayMetrics.getCity());
        assertEquals(4.5F, thirdDayMetrics.getAvgTempDaily().doubleValue(), 0.0F);
        assertEquals(6.5F, thirdDayMetrics.getAvgTempNightly().doubleValue(), 0.0F);
        assertEquals(4.5F, thirdDayMetrics.getAvgPressure().doubleValue(), 0.0F);

    }

}