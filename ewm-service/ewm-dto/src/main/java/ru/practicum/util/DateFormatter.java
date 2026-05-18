package ru.practicum.util;

import java.time.format.DateTimeFormatter;

public class DateFormatter {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private DateFormatter() {
    }
}
