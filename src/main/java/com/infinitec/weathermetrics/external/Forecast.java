package com.infinitec.weathermetrics.external;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Forecast {
    private Float temp;
    private Float pressure;
}
