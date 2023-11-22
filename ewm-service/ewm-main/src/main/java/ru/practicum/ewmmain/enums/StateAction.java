package ru.practicum.ewmmain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StateAction {
    PUBLISH_EVENT("PUBLISH_EVENT"),
    REJECT_EVENT("REJECT_EVENT");

    private final String value;
}
