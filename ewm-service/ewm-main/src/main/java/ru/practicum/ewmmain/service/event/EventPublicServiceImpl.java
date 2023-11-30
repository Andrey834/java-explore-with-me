package ru.practicum.ewmmain.service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.location.LocationDto;
import ru.practicum.dto.stat.ViewStats;
import ru.practicum.ewmmain.client.RestClient;
import ru.practicum.ewmmain.enums.SortEvents;
import ru.practicum.ewmmain.enums.StateEvent;
import ru.practicum.ewmmain.exception.BadParamForEventSearchException;
import ru.practicum.ewmmain.mapper.EventMapper;
import ru.practicum.ewmmain.model.Event;
import ru.practicum.ewmmain.repository.EventDao;
import ru.practicum.ewmmain.util.ParamEvents;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventPublicServiceImpl implements EventPublicService {
    private final EventDao eventDao;
    private final RestClient client;

    @Override
    public List<EventShortDto> getEventsPublicWithParam(ParamEvents paramEvents,
                                                        PageRequest pageRequest,
                                                        HttpServletRequest request) {

        if (paramEvents.getRangeEnd().isBefore(paramEvents.getRangeStart())) {
            throw new BadParamForEventSearchException(paramEvents.getRangeEnd() + " before start");
        }

        Float lat = null;
        Float lon = null;
        if (paramEvents.getAddress() != null) {
            LocationDto locationDto = client.getLocationFromAddress(paramEvents.getAddress());
            lat = locationDto.getLat();
            lon = locationDto.getLon();
        }

        List<EventShortDto> events = eventDao.getEventsPublicWithParam(
                        paramEvents.getText(),
                        paramEvents.getPaid(),
                        paramEvents.getCategories(),
                        paramEvents.getOnlyAvailable(),
                        lat,
                        lon,
                        paramEvents.getRangeStart(),
                        paramEvents.getRangeEnd(),
                        pageRequest)
                .stream()
                .map(EventMapper::eventToEventShortDto)
                .collect(Collectors.toList());

        client.sendHit("EWM", request.getRequestURI(), request.getRemoteAddr());

        if(!events.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            events.forEach(event -> sb.append(request.getRequestURI()).append("/").append(event.getId()).append(","));
            sb.deleteCharAt(sb.length() - 1);
            List<ViewStats> stats = client.getStat(sb.toString());

            Map<Long, Long> mapStats = new HashMap<>();
            stats.forEach(stat -> {
                Long id = (long) stat.getUri().split("/", -1).length - 1;
                Long hits = stat.getHits();
                mapStats.put(id, hits);
            });

            events.forEach(event -> event.setViews(mapStats.getOrDefault(event.getId(), 0L)));
        }

        if (paramEvents.getSortEvents() == SortEvents.VIEWS) {
            events.sort(Comparator.comparing(EventShortDto::getViews));
        }

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

        client.sendHit("EWM", request.getRequestURI(), request.getRemoteAddr());
        List<ViewStats> stats = client.getStat(request.getRequestURI());


        long hit = stats.stream()
                .filter(stat -> stat.getUri().equals(request.getRequestURI()))
                .map(ViewStats::getHits)
                .findFirst().orElse(0L);

        event.setViews(hit);

        return EventMapper.eventToEventFullDto(event);
    }
}
