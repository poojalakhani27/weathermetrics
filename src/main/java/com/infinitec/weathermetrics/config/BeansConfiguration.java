package com.infinitec.weathermetrics.config;

import com.infinitec.weathermetrics.external.OpenWeatherMapApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Configuration
public class BeansConfiguration {

    private Retrofit retrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    @Bean
    public OpenWeatherMapApi openWeatherMapApi() {
        Retrofit retrofit = retrofit();
        return retrofit.create(OpenWeatherMapApi.class);
    }

}
