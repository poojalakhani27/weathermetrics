CREATE TABLE weather_metrics (
    id IDENTITY PRIMARY KEY,
    city VARCHAR(50) NOT NULL,
    metric_date BIGINT NOT NULL,
    avg_temp_daily REAL NOT NULL,
    avg_temp_nightly REAL NOT NULL,
    avg_pressure REAL NOT NULL
);

CREATE INDEX idx_weather_metrics_city_date ON weather_metrics(city, metric_date);