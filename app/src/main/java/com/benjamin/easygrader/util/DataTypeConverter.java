package com.benjamin.easygrader.util;

import androidx.room.TypeConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DataTypeConverter {

  private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

  @TypeConverter
  public static LocalDateTime toLocalDateTime(String value) {
    return value == null ? null : LocalDateTime.parse(value, formatter);
  }

  @TypeConverter
  public static String fromLocalDateTime(LocalDateTime dateTime) {
    return dateTime == null ? null : dateTime.format(formatter);
  }
}
