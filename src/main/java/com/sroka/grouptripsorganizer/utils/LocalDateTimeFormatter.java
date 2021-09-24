package com.sroka.grouptripsorganizer.utils;

import java.time.LocalDateTime;

import static java.time.format.DateTimeFormatter.ofPattern;

public class LocalDateTimeFormatter {
    public static String format(LocalDateTime localDateTime) {
        return localDateTime.format(ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
