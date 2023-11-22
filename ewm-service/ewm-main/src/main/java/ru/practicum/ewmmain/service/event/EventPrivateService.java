package ru.practicum.ewmmain.service.event;

import org.springframework.data.domain.PageRequest;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdateEventUserRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.ewmmain.model.Event;

import java.util.List;

public interface EventPrivateService {
    List<EventShortDto> getEvents(long userId, PageRequest pageRequest);

    EventFullDto addEvent(long userId, NewEventDto newEventDto);

    EventFullDto getEvent(long userId, long eventId);

    EventFullDto updateEvent(long userId, long eventId, UpdateEventUserRequest updateEventUserRequest);

    List<ParticipationRequestDto> getEventParticipants(long userId, long eventId);

    EventRequestStatusUpdateResult changeRequestStatus(long userId,
                                                       long eventId,
                                                       EventRequestStatusUpdateRequest updateRequestStatus);

    Event findById(long eventId);

    Event save(Event event);
}
