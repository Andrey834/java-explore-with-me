package ru.practicum.ewmmain.service.event;

import org.springframework.data.domain.PageRequest;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.ewmmain.enums.SortEvents;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventPublicService {
    List<EventShortDto> getEventsPublicWithParam(String text,
                                                 List<Long> categories,
                                                 Boolean paid,
                                                 LocalDateTime rangeStart,
                                                 LocalDateTime rangeEnd,
                                                 Boolean onlyAvailable,
                                                 SortEvents sortEvents,
                                                 PageRequest pageRequest,
                                                 HttpServletRequest request);

    EventFullDto getEvent(long eventId, HttpServletRequest request);
}
