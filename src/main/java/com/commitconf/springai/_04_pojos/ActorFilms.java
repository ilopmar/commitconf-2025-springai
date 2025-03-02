package com.commitconf.springai._04_pojos;

import java.math.BigDecimal;
import java.util.List;

public record ActorFilms(String actor, List<Movie> movies) {

  public record Movie(String name, String movieAbstract, Integer yearReleased, BigDecimal rating) {
  }

}
