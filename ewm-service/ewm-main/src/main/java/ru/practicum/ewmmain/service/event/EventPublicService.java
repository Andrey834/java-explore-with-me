package ru.practicum.ewmmain.service.event;

import org.springframework.data.domain.PageRequest;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.ewmmain.enums.SortEvents;
import ru.practicum.ewmmain.util.ParamEvents;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventPublicService {
    List<EventShortDto> getEventsPublicWithParam(ParamEvents paramEvents,
                                                 PageRequest pageRequest,
                                                 HttpServletRequest request);

    EventFullDto getEvent(long eventId, HttpServletRequest request);
}
