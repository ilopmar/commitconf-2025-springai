package com.commitconf.springai._05_tools.datetime;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
class DateTimeController {

  private final ChatClient chatClient;

  DateTimeController(ChatClient.Builder builder) {
    this.chatClient = builder
        .defaultTools(new DateTimeTools())
        .build();
  }

  @GetMapping("/date-time")
  public String chat(@RequestParam(value = "message", defaultValue = "¿Qué día es mañana?") String message) {
    return chatClient.prompt()
        .user(message)
        .call()
        .content();
  }

}
