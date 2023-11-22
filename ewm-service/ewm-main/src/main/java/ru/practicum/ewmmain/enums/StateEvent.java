package ru.practicum.ewmmain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StateEvent {
    PENDING("PENDING"),
    PUBLISHED("PUBLISHED"),
    CANCELED("CANCELED");

    private final String value;
}
