package com.commitconf.springai._05_tools.datetime;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.TimeZone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.context.i18n.LocaleContextHolder;

class DateTimeToolsTest {

  private DateTimeTools dateTimeTools;

  @BeforeEach
  void setUp() {
    dateTimeTools = new DateTimeTools();
  }

  @DisplayName("When calling getCurrentDateTime then it returns a non null value")
  @Test
  void returnedDateTimeIsNotNull() {
    String result = dateTimeTools.getCurrentDateTime();

    assertThat(result).isNotNull();
  }

  @DisplayName("When calling getCurrentDateTime then it returns the current date and time in the default time zone")
  @ParameterizedTest
  @ValueSource(strings = {"Europe/Madrid", "America/New_York", "Asia/Tokyo"})
  void getCurrentDateTime_withDifferentTimeZones(String timeZoneId) {
    TimeZone timeZone = TimeZone.getTimeZone(timeZoneId);
    LocaleContextHolder.setTimeZone(timeZone);

    String result = dateTimeTools.getCurrentDateTime();

    assertThat(result).contains(timeZone.toZoneId().toString());
  }

}
