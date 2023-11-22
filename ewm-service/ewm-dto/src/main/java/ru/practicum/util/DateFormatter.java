package ru.practicum.util;

import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class DateFormatter {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
}
