package com.commitconf.springai._07_multimodal;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImageDetection {

  private final ChatClient chatClient;

  @Value("classpath:/images/IMG20240420124327.jpg")
  Resource image;

  public ImageDetection(ChatClient.Builder builder) {
    this.chatClient = builder.build();
  }

  @GetMapping("/image-to-text")
  public String image(@RequestParam String message) {
    // Por favor, ¿me puedes describir la imagen?
    return chatClient.prompt()
        .user(u -> u
            .text(message)
            .media(MimeTypeUtils.IMAGE_JPEG, image)
        )
        .call()
        .content();
  }

}
