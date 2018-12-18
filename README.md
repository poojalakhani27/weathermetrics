# Weather metrics
Weather metrics from Open Weather Maps API.


## Design approach
1. Given the application is a read heavy application, it is better to store the aggregated metrics than to calculate those on the fly per request. Two design choices were considered :
    1. **A scheduled job to aggregate metrics for a day and store it in the database**. This approach is very good when there is extremely diverse read rate and latency cannot be tolerated. This choice was ruled out for implementation because as I do not know the request diversity and it will over optimisation to pre-load all the data for all the cities if the requests are not so diverse.
    1. **Reactively store the metrics on first request for a city and date**. This approach was adopted to load the store reactively to avoid premature optimization by bloating up the local data store. On the down side, only the first request will face latency.
1. Datastore is in-memory H2. However a Redis cache can be fronted.
1. Project Lombok is used for making data classes concise.
1. A Test Driven Development approach was taken
1. Validation for the input and Integration tests are wriiten.

## Building and running the application 
1. mvn clean install
1. cd /target
1. java -jar weathermetrics-0.0.1-SNAPSHOT-spring-boot.jar
1. make resquest to http://localhost:8081/data?CITYNAME={cityname}

## Future Enhancements
1. Since the metrics don't change for a city for a day, it is better to **cache** those by LRU eviction policy.
1. A **circuit breaker** could be implemented wrapping the calls to Open Weather Maps API.
1. Currently, the validation of city is done against Open Weather Maps API. This can optimized to validate locally against a pre poplulated lists of cities.
