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
import ru.practicum.ewmmain.util.ParamEvents;

import javax.servlet.http.HttpServletRequest;
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
    public List<EventShortDto> getEventsPublicWithParam(ParamEvents paramEvents,
                                                        PageRequest pageRequest,
                                                        HttpServletRequest request) {

        if (paramEvents.getRangeEnd().isBefore(paramEvents.getRangeStart())) {
            throw new BadParamForEventSearchException(paramEvents.getRangeEnd() + " before start");
        }

        List<EventShortDto> events = eventDao.getEventsPublicWithParam(
                        paramEvents.getText(),
                        paramEvents.getPaid(),
                        paramEvents.getCategories(),
                        paramEvents.getOnlyAvailable(),
                        paramEvents.getRangeStart(),
                        paramEvents.getRangeEnd(),
                        pageRequest)
                .stream()
                .map(EventMapper::eventToEventShortDto)
                .collect(Collectors.toList());

        if (paramEvents.getSortEvents() == SortEvents.VIEWS) {
            events.sort(Comparator.comparing(EventShortDto::getViews));
        }

        StringBuilder sb = new StringBuilder();
        events.forEach(req -> sb.append("/events/").append(req.getId()).append(","));
        sb.delete(sb.length() - 1, sb.length());
        List<ViewStats> stats = client.getStat(sb.toString());

        events.forEach(event -> event.setViews(stats.stream()
                .filter(stat -> stat.getUri().equals("/events/" + event.getId()))
                .map(ViewStats::getHits)
                .findFirst()
                .orElse(0L)));

        client.sendHit("EWM", request.getRequestURI(), request.getRemoteAddr());
        return events;
    }

    @Override
    @Transactional
    public EventFullDto getEvent(long eventId, HttpServletRequest request) {
        Event event = eventDao.findByIdAndState(eventId, StateEvent.PUBLISHED).orElseThrow(
                () -> new NoSuchElementException("Event with ID=" + eventId + " was not found"));

        client.sendHit("EWM", request.getRequestURI(), request.getRemoteAddr());
        List<ViewStats> stats = client.getStat(request.getRequestURI());
        event.setViews(stats.get(0).getHits());
        eventDao.save(event);

        return EventMapper.eventToEventFullDto(event);
    }
}
