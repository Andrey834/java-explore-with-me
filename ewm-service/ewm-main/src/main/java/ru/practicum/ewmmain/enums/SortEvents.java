package ru.practicum.ewmmain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SortEvents {
    EVENT_DATE("EVENT_DATE"),
    VIEWS("VIEWS");

    private final String value;
}
