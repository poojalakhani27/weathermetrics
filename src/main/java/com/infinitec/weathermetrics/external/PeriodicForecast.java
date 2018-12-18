package com.infinitec.weathermetrics.external;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PeriodicForecast {
    private Long dt;
    private Forecast main;

}
