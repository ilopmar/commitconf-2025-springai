package com.commitconf.springai._05_tools.weather;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
class WeatherController {

  private final ChatClient chatClient;

  WeatherController(ChatClient.Builder builder, WeatherConfigProperties weatherConfigProperties) {
    this.chatClient = builder
        .defaultSystem("Eres un asistente para responser preguntas sobre distintas ciudades en el mundo.")
        .defaultTools(new WeatherService(weatherConfigProperties))
        .build();
  }

  @GetMapping("/cities")
  public String chat(@RequestParam String message) {
    return chatClient.prompt()
        .user(message)
        .call()
        .content();
  }

}
