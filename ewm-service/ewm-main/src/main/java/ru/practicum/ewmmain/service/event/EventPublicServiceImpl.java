package ru.practicum.ewmmain.service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.stat.ViewStats;
import ru.practicum.ewmmain.client.StatClient;
import ru.practicum.ewmmain.enums.SortEvents;
import ru.practicum.ewmmain.enums.StateEvent;
import ru.practicum.ewmmain.exception.BadParamForEventSearchException;
import ru.practicum.ewmmain.mapper.EventMapper;
import ru.practicum.ewmmain.model.Event;
import ru.practicum.ewmmain.repository.EventDao;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventPublicServiceImpl implements EventPublicService {
    private final EventDao eventDao;
    private final StatClient client;

    @Override
    public List<EventShortDto> getEventsPublicWithParam(String text,
                                                        List<Long> categories,
                                                        Boolean paid,
                                                        LocalDateTime rangeStart,
                                                        LocalDateTime rangeEnd,
                                                        Boolean onlyAvailable,
                                                        SortEvents sortEvents,
                                                        PageRequest pageRequest,
                                                        HttpServletRequest request) {

        if (rangeEnd.isBefore(rangeStart)) throw new BadParamForEventSearchException(rangeEnd + " before start");

        List<EventShortDto> events = eventDao.getEventsPublicWithParam(
                        text, paid, categories, onlyAvailable, rangeStart, rangeEnd, pageRequest).stream()
                .map(EventMapper::eventToEventShortDto)
                .collect(Collectors.toList());

        if (sortEvents == SortEvents.VIEWS) {
            events.sort(Comparator.comparing(EventShortDto::getViews));
        }


        client.sendHit("EWM", request.getRequestURI(), request.getRemoteAddr());
        return events;
    }

    @Override
    @Transactional
    public EventFullDto getEvent(long eventId, HttpServletRequest request) {
        Event event = eventDao.findById(eventId).orElseThrow(
                () -> new NoSuchElementException("Event with ID=" + eventId + " was not found"));

        if (event.getState() != StateEvent.PUBLISHED) {
            throw new NoSuchElementException("Event with ID=" + eventId + " was not found");
        }

        {
            client.sendHit("EWM", request.getRequestURI(), request.getRemoteAddr());
            List<ViewStats> stats = client.getStat(request.getRequestURI());
            event.setViews(stats.get(0).getHits());
            eventDao.save(event);
        }
        return EventMapper.eventToEventFullDto(event);
    }
}
