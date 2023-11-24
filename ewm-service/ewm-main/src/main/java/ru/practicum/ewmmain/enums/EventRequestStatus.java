package ru.practicum.ewmmain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EventRequestStatus {
    CONFIRMED("CONFIRMED"),
    REJECTED("REJECTED");

    private final String value;
}

