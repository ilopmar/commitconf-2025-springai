package com.commitconf.springai._08_imagegeneration;

import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.ai.openai.api.OpenAiImageApi;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class ImageController {

  private final ImageModel imageModel;

  ImageController(ImageModel imageModel) {
    this.imageModel = imageModel;
  }

  @GetMapping("/text-to-image")
  String imageProviderOptions(String message) {
    var imageResponse = imageModel.call(
        new ImagePrompt(message, OpenAiImageOptions.builder()
            .withQuality("standard")
            .withN(1)
            .withHeight(1024)
            .withWidth(1024)
            .withModel(OpenAiImageApi.ImageModel.DALL_E_3.getValue())
            .withResponseFormat("url")
            .build()
        )
    );

    return imageResponse.getResult().getOutput().getUrl();
  }

}
