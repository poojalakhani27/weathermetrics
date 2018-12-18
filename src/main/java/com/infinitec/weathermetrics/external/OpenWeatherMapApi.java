package com.infinitec.weathermetrics.external;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherMapApi {

    @GET("forecast?units=metric")
    Call<OpenWeatherMapApiResponse> getForecast(@Query("q") String cityName, @Query("APPID") String appId);
}
