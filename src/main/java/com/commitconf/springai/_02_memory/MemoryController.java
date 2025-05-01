package com.commitconf.springai._02_memory;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemoryController {

  private final ChatClient chatClient;

  public MemoryController(ChatClient.Builder builder) {
    this.chatClient = builder
        .defaultAdvisors(MessageChatMemoryAdvisor
            .builder(MessageWindowChatMemory.builder().build())
            .build())
        .build();
  }

  @GetMapping("/chat-memory")
  public String chatMemory(@RequestParam String message) {
    return chatClient.prompt()
        .user(message)
        .call()
        .content();
  }

}
