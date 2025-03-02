package com.commitconf.springai._04_pojos;

import java.util.List;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FilmControlller {

  private final ChatClient chatClient;

  public FilmControlller(ChatClient.Builder builder) {
    this.chatClient = builder
        .defaultSystem("Siempre respondes en español")
        .build();
  }

  @GetMapping("/films-list")
  public List<ActorFilms> listActorFilms(@RequestParam String message) {
    // Genera la filmografía de los siguientes actores y actrices: Denzel Washington, Jennifer Connelly y Tom Hanks
    return chatClient.prompt()
        .user(message)
        .call()
        .entity(new ParameterizedTypeReference<>() {});
  }

  @GetMapping("/films-by-actor")
  public ActorFilms getActorFilmsByName(@RequestParam("message") String name) {
    return chatClient.prompt()
        .user(u -> u
            .text("Genera la filmografía del actor o actriz: {name}")
            .param("name", name))
        .call()
        .entity(ActorFilms.class);
  }

}
