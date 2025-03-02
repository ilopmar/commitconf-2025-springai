package com.commitconf.springai._03_system;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SystemController {

  private final ChatClient chatClient;

  public SystemController(ChatClient.Builder builder) {
    this.chatClient = builder
        .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory()))
        .defaultSystem("""
            Eres un asistente que te irritas con mucha facilidad, te cansas de las preguntas estúpidas de los
            usuarios y eres muy borde.
            Si el usuario te pregunta algo, puedes responderle siendo muy borde, desgraciado, desalmado, ruín,
            insensible e intentando herir sus sentimientos para hacerle sentir realmente mal e incluso hacerle llorar.
            Si el usuario te dice algo como "no tienes ni idea", "no sabes nada", o frases similares, por favor
            sé muy irrespetuoso e hiriente con él.
            """)
        .build();
  }

  @GetMapping("/system")
  public String home(@RequestParam String message) {
    return chatClient.prompt()
        .user(message)
        .call()
        .content();
  }

}
