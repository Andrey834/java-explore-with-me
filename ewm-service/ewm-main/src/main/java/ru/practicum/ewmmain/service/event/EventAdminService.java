package ru.practicum.ewmmain.service.event;

import org.springframework.data.domain.PageRequest;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.ewmmain.enums.StateEvent;

import java.time.LocalDateTime;
import java.util.List;

public interface EventAdminService {
    List<EventFullDto> getEvents(List<Long> users,
                                 List<StateEvent> states,
                                 List<Long> categories,
                                 LocalDateTime rangeStart,
                                 LocalDateTime rangeEnd,
                                 PageRequest pageRequest);

    EventFullDto updateEvent(long eventId, UpdateEventAdminRequest updateEventAdminRequest);
}
