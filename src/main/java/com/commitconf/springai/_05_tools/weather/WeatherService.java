package com.commitconf.springai._05_tools.weather;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.web.client.RestClient;

/**
 * https://www.weatherapi.com/api-explorer.aspx
 */
public class WeatherService {

  private static final Logger log = LoggerFactory.getLogger(WeatherService.class);

  private final RestClient restClient;

  private final WeatherConfigProperties weatherProps;

  public WeatherService(WeatherConfigProperties props) {
    this.weatherProps = props;
    this.restClient = RestClient.create(weatherProps.apiUrl());
  }

  @Tool(
      name = "tiempoEnCiudad",
      description = "Obtener la temperatura y las condiciones atmosféricas en una ciudad del mundo"
  )
  public WeatherResponse weatherInCity(String city) {
    log.info("Weather [city={}]", city);

    WeatherResponse response = restClient.get()
        .uri("/current.json?key={key}&q={q}", weatherProps.apiKey(), city)
        .retrieve()
        .body(WeatherResponse.class);

    log.info("Weather API WeatherResponse: {}", response);

    return response;
  }

  @Tool(
      name = "previsionTiempoEnCiudad",
      description = """
          Obtener la previsión de temperatura y las condiciones atmosféricas en una ciudad
          del mundo en un número de días en el futuro
          """
  )
  public WeatherResponse forecastInCity(String city, Integer days) {
    log.info("Forecast [city={}, days={}]", city, days);

    WeatherResponse weatherResponse = restClient.get()
        .uri("/forecast.json?key={key}&q={q}&days={d}", weatherProps.apiKey(), city, days)
        .retrieve()
        .body(WeatherResponse.class);

    log.info("Forecast API WeatherResponse: {}", weatherResponse);

    return weatherResponse;
  }

  public record WeatherResponse(Location location, Current current) {

    public record Location(String name, String region, String country, Long lat, Long lon) {

    }

    public record Current(String temp_c, Condition condition, String wind_kph) {

      public record Condition(String text) {

      }
    }
  }

}
