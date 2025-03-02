package com.commitconf.springai._06_rag;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NobelPrizeController {

  private final ChatClient chatClient;

  public NobelPrizeController(ChatClient.Builder builder, VectorStore vectorStore) {
    this.chatClient = builder
        .defaultAdvisors(new QuestionAnswerAdvisor(vectorStore))
        .build();
  }

  @GetMapping("/nobel")
  String rag(@RequestParam String message) {
    return chatClient.prompt()
        .user(message)
        .call()
        .content();
  }

}
