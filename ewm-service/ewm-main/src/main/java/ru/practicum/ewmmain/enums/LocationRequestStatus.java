package ru.practicum.ewmmain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LocationRequestStatus {
    PENDING("PENDING"),
    ADDED("ADDED"),
    CANCELLED("CANCELLED");

    private final String value;
}
