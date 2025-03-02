package com.commitconf.springai;

import com.commitconf.springai._05_tools.weather.WeatherConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(WeatherConfigProperties.class)
public class SpringaiApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringaiApplication.class, args);
  }

}
