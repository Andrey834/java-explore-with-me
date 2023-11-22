package ru.practicum.ewmmain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ParticipationRequestStatus {
    PENDING("PENDING"),
    CONFIRMED("CONFIRMED"),
    CANCELED("CANCELED"),
    REJECTED("REJECTED");

    private final String value;
}
