package com.commitconf.springai._01_chat;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
class ChatController {

  private final ChatClient chatClient;

  ChatController(ChatClient.Builder builder) {
    this.chatClient = builder.build();
  }

  @GetMapping("/chat-response")
  public ChatResponse chatResponse(@RequestParam(value = "message",
      defaultValue = "Cuéntame un chiste sobre informáticos que acuden a una conferencia") String message) {
    return chatClient.prompt()
        .user(message)
        .call()
        .chatResponse();
  }

  @GetMapping("/chat")
  public String chat(@RequestParam(value = "message",
      defaultValue = "Cuéntame un chiste sobre informáticos que acuden a una conferencia") String message) {
    return chatClient.prompt()
        .user(message)
        .call()
        .content();
  }

  @GetMapping("/chat-stream")
  public Flux<String> chatStream(
      @RequestParam(value = "message", defaultValue = "Explícame en 5 párrafos lo que sepas sobre el lenguaje Java") String message) {
    return chatClient.prompt()
        .user(message)
        .stream()
        .content();
  }

}
