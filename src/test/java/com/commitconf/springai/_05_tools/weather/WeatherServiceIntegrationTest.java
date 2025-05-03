package com.commitconf.springai._05_tools.weather;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.model.JsonBody.json;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.Header;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
class WeatherServiceIntegrationTest {

  private static final String API_KEY = "API_KEY";

  private static final String CITY = "Madrid";

  private static final int DAYS = 3;

  private static final int MOCKSERVER_PORT = 1080;

  @Container
  private static final GenericContainer<?> mockServerContainer = new GenericContainer<>(
      DockerImageName.parse("mockserver/mockserver:5.15.0"))
      .withExposedPorts(MOCKSERVER_PORT);

  private static MockServerClient mockServerClient;

  private WeatherService weatherService;

  @BeforeAll
  static void setupMockServer() {
    mockServerClient = new MockServerClient(
        mockServerContainer.getHost(),
        mockServerContainer.getMappedPort(MOCKSERVER_PORT)
    );
  }

  @AfterAll
  static void stopMockServer() {
    if (mockServerClient != null) {
      mockServerClient.close();
    }
  }

  @BeforeEach
  void init() {
    WeatherConfigProperties props = new WeatherConfigProperties(
        API_KEY,
        "http://%s:%s".formatted(mockServerContainer.getHost(), mockServerContainer.getMappedPort(MOCKSERVER_PORT))
    );
    weatherService = new WeatherService(props);
    mockServerClient.reset();
  }

  @DisplayName("When requesting the current weather for a city then it returns it")
  @Test
  void weatherInCity() {
    setupMockForCurrentWeather();

    WeatherService.WeatherResponse response = weatherService.weatherInCity(CITY);

    assertThat(response).isNotNull();
    assertThat(response.location().name()).isEqualTo("Madrid");
    assertThat(response.location().country()).isEqualTo("Spain");
    assertThat(response.current().temp_c()).isEqualTo("12.1");
    assertThat(response.current().condition().text()).isEqualTo("Patchy rain nearby");
  }

  @DisplayName("When requesting the forecast for a city then it returns it")
  @Test
  void forecastInCity() {
    setupMockForForecast();

    WeatherService.WeatherResponse response = weatherService.forecastInCity(CITY, DAYS);

    assertThat(response).isNotNull();
    assertThat(response.location().name()).isEqualTo("Madrid");
    assertThat(response.location().country()).isEqualTo("Spain");
    assertThat(response.current().temp_c()).isEqualTo("14.2");
    assertThat(response.current().condition().text()).isEqualTo("Patchy rain nearby");
  }

  private void setupMockForCurrentWeather() {
    mockServerClient
        .when(HttpRequest.request()
            .withMethod("GET")
            .withPath("/current.json")
            .withQueryStringParameter("key", API_KEY)
            .withQueryStringParameter("q", CITY)
        )
        .respond(HttpResponse.response()
            .withStatusCode(200)
            .withHeaders(new Header("Content-Type", "application/json; charset=utf-8"))
            .withBody(json("""
                {
                  "location": {
                    "name": "Madrid",
                    "region": "Madrid",
                    "country": "Spain",
                    "lat": 40.4,
                    "lon": -3.6833,
                    "tz_id": "Europe/Madrid",
                    "localtime_epoch": 1746308965,
                    "localtime": "2025-05-03 23:49"
                  },
                  "current": {
                    "last_updated_epoch": 1746308700,
                    "last_updated": "2025-05-03 23:45",
                    "temp_c": 12.1,
                    "temp_f": 53.8,
                    "is_day": 0,
                    "condition": {
                      "text": "Patchy rain nearby",
                      "icon": "//cdn.weatherapi.com/weather/64x64/night/176.png",
                      "code": 1063
                    },
                    "wind_mph": 7.2,
                    "wind_kph": 11.5,
                    "wind_degree": 225,
                    "wind_dir": "SW",
                    "pressure_mb": 1015,
                    "pressure_in": 29.97,
                    "precip_mm": 0.07,
                    "precip_in": 0,
                    "humidity": 88,
                    "cloud": 0,
                    "feelslike_c": 10.9,
                    "feelslike_f": 51.7,
                    "windchill_c": 10.9,
                    "windchill_f": 51.7,
                    "heatindex_c": 12.1,
                    "heatindex_f": 53.8,
                    "dewpoint_c": 8.6,
                    "dewpoint_f": 47.5,
                    "vis_km": 10,
                    "vis_miles": 6,
                    "uv": 0,
                    "gust_mph": 12.9,
                    "gust_kph": 20.8
                  }
                }
                """)
            )
        );
  }

  private void setupMockForForecast() {
    mockServerClient
        .when(HttpRequest.request()
            .withMethod("GET")
            .withPath("/forecast.json")
            .withQueryStringParameter("key", API_KEY)
            .withQueryStringParameter("q", CITY)
            .withQueryStringParameter("days", String.valueOf(DAYS))
        )
        .respond(HttpResponse.response()
            .withStatusCode(200)
            .withHeaders(new Header("Content-Type", "application/json; charset=utf-8"))
            .withBody(json("""
                {
                   "location": {
                     "name": "Madrid",
                     "region": "Madrid",
                     "country": "Spain",
                     "lat": 40.4,
                     "lon": -3.6833,
                     "tz_id": "Europe/Madrid",
                     "localtime_epoch": 1746308700,
                     "localtime": "2025-05-03 23:45"
                   },
                   "current": {
                     "last_updated_epoch": 1746307800,
                     "last_updated": "2025-05-03 23:30",
                     "temp_c": 14.2,
                     "temp_f": 57.6,
                     "is_day": 0,
                     "condition": {
                       "text": "Patchy rain nearby",
                       "icon": "//cdn.weatherapi.com/weather/64x64/night/176.png",
                       "code": 1063
                     },
                     "wind_mph": 7.2,
                     "wind_kph": 11.5,
                     "wind_degree": 225,
                     "wind_dir": "SW",
                     "pressure_mb": 1015,
                     "pressure_in": 29.97,
                     "precip_mm": 0.07,
                     "precip_in": 0,
                     "humidity": 88,
                     "cloud": 0,
                     "feelslike_c": 13.5,
                     "feelslike_f": 56.2,
                     "windchill_c": 10.9,
                     "windchill_f": 51.7,
                     "heatindex_c": 12.1,
                     "heatindex_f": 53.8,
                     "dewpoint_c": 8.6,
                     "dewpoint_f": 47.5,
                     "vis_km": 10,
                     "vis_miles": 6,
                     "uv": 0,
                     "gust_mph": 12.9,
                     "gust_kph": 20.8
                   },
                   "forecast": {
                     "forecastday": [
                       {
                         "date": "2025-05-03",
                         "date_epoch": 1746230400,
                         "day": {
                           "maxtemp_c": 19.3,
                           "maxtemp_f": 66.8,
                           "mintemp_c": 10,
                           "mintemp_f": 50.1,
                           "avgtemp_c": 14.4,
                           "avgtemp_f": 57.9,
                           "maxwind_mph": 11.2,
                           "maxwind_kph": 18,
                           "totalprecip_mm": 0.46,
                           "totalprecip_in": 0.02,
                           "totalsnow_cm": 0,
                           "avgvis_km": 9.9,
                           "avgvis_miles": 6,
                           "avghumidity": 68,
                           "daily_will_it_rain": 1,
                           "daily_chance_of_rain": 83,
                           "daily_will_it_snow": 0,
                           "daily_chance_of_snow": 0,
                           "condition": {
                             "text": "Patchy rain nearby",
                             "icon": "//cdn.weatherapi.com/weather/64x64/day/176.png",
                             "code": 1063
                           },
                           "uv": 1.8
                         }
                       },
                       {
                         "date": "2025-05-04",
                         "date_epoch": 1746316800,
                         "day": {
                           "maxtemp_c": 16.2,
                           "maxtemp_f": 61.1,
                           "mintemp_c": 9.8,
                           "mintemp_f": 49.6,
                           "avgtemp_c": 12.5,
                           "avgtemp_f": 54.5,
                           "maxwind_mph": 20.8,
                           "maxwind_kph": 33.5,
                           "totalprecip_mm": 1.56,
                           "totalprecip_in": 0.06,
                           "totalsnow_cm": 0,
                           "avgvis_km": 10,
                           "avgvis_miles": 6,
                           "avghumidity": 75,
                           "daily_will_it_rain": 1,
                           "daily_chance_of_rain": 87,
                           "daily_will_it_snow": 0,
                           "daily_chance_of_snow": 0,
                           "condition": {
                             "text": "Patchy rain nearby",
                             "icon": "//cdn.weatherapi.com/weather/64x64/day/176.png",
                             "code": 1063
                           },
                           "uv": 1.6
                         }
                       },
                       {
                         "date": "2025-05-05",
                         "date_epoch": 1746403200,
                         "day": {
                           "maxtemp_c": 18.2,
                           "maxtemp_f": 64.7,
                           "mintemp_c": 9.3,
                           "mintemp_f": 48.7,
                           "avgtemp_c": 13.7,
                           "avgtemp_f": 56.7,
                           "maxwind_mph": 12.5,
                           "maxwind_kph": 20.2,
                           "totalprecip_mm": 6.31,
                           "totalprecip_in": 0.25,
                           "totalsnow_cm": 0,
                           "avgvis_km": 9.8,
                           "avgvis_miles": 6,
                           "avghumidity": 70,
                           "daily_will_it_rain": 1,
                           "daily_chance_of_rain": 81,
                           "daily_will_it_snow": 0,
                           "daily_chance_of_snow": 0,
                           "condition": {
                             "text": "Moderate rain",
                             "icon": "//cdn.weatherapi.com/weather/64x64/day/302.png",
                             "code": 1189
                           },
                           "uv": 1.6
                         }
                       }
                     ]
                   }
                 }
                """)
            )
        );
  }
}
