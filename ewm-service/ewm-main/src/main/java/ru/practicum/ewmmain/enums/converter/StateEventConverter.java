package ru.practicum.ewmmain.enums.converter;

import org.springframework.core.convert.converter.Converter;
import ru.practicum.ewmmain.enums.StateEvent;


public class StateEventConverter implements Converter<String, StateEvent> {
    @Override
    public StateEvent convert(String source) {
        try {
            return StateEvent.valueOf(source);
        } catch (Exception e) {
            return null;
        }
    }
}
