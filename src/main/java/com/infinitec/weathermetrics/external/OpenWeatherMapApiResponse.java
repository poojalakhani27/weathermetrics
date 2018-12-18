package com.infinitec.weathermetrics.external;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class OpenWeatherMapApiResponse {
    private List<PeriodicForecast> list;
}
