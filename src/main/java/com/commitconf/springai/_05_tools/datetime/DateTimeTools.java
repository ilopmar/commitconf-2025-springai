package com.commitconf.springai._05_tools.datetime;

import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.context.i18n.LocaleContextHolder;

public class DateTimeTools {

  private static final Logger log = LoggerFactory.getLogger(DateTimeTools.class);

  @Tool(
      name = "diaActual",
      description = "Obtener la fecha y hora en la zona horaria del usuario"
  )
  public String getCurrentDateTime() {
    String datetime = LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toString();
    log.info("Current DateTime. [datetime={}]", datetime);

    return datetime;
  }

}
