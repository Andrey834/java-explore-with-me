package ru.practicum.ewmmain.service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.dto.location.LocationDto;
import ru.practicum.ewmmain.client.RestClient;
import ru.practicum.ewmmain.enums.StateAction;
import ru.practicum.ewmmain.enums.StateEvent;
import ru.practicum.ewmmain.exception.BadDataEventException;
import ru.practicum.ewmmain.exception.BadDateEventException;
import ru.practicum.ewmmain.exception.BadStatusEventException;
import ru.practicum.ewmmain.mapper.CategoryMapper;
import ru.practicum.ewmmain.mapper.EventMapper;
import ru.practicum.ewmmain.mapper.LocationMapper;
import ru.practicum.ewmmain.model.Category;
import ru.practicum.ewmmain.model.Event;
import ru.practicum.ewmmain.model.Location;
import ru.practicum.ewmmain.repository.CategoryDao;
import ru.practicum.ewmmain.repository.EventDao;
import ru.practicum.util.DateFormatter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventAdminServiceImpl implements EventAdminService {
    private final EventDao eventDao;
    private final CategoryDao categoryDao;
    private final RestClient client;

    @Override
    public List<EventFullDto> getEvents(List<Long> users,
                                        List<StateEvent> states,
                                        List<Long> categories,
                                        String address,
                                        LocalDateTime rangeStart,
                                        LocalDateTime rangeEnd,
                                        PageRequest pageRequest) {
        Float lat = null;
        Float lon = null;
        if (address != null) {
            LocationDto locationDto = client.getLocationFromAddress(address);
            lat = locationDto.getLat();
            lon = locationDto.getLon();
        }

        return eventDao.getEventsAdminWithParam(users, states, categories, lat, lon, rangeStart, rangeEnd, pageRequest)
                .stream()
                .map(EventMapper::eventToEventFullDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = eventDao.findById(eventId)
                .orElseThrow(() -> new NoSuchElementException("Event with ID=" + eventId + " not found"));

        if (updateEventAdminRequest.getStateAction() != null) {
            StateEvent stateEvent = event.getState();

            if (stateEvent == StateEvent.PUBLISHED || stateEvent == StateEvent.CANCELED) {
                throw new BadStatusEventException("Event has already been cancelled or published");
            }

            if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
                throw new BadDataEventException("Bad data for update Event with ID=" + eventId);
            }
        }

        Event updateEvent = eventDao.save(editEventWithRequest(event, updateEventAdminRequest));
        return EventMapper.eventToEventFullDto(updateEvent);
    }

    private Event editEventWithRequest(Event event, UpdateEventAdminRequest request) {
        String annotation = request.getAnnotation() == null ? event.getAnnotation() : request.getAnnotation();
        String description = request.getDescription() == null ? event.getDescription() : request.getDescription();

        if (request.getEventDate() != null) {
            event.setEventDate(LocalDateTime.parse(request.getEventDate(), DateFormatter.FORMATTER));
            if (event.getEventDate().isBefore(LocalDateTime.now())) {
                throw new BadDateEventException("eventDate must be future");
            }
        }

        Location location = request.getLocation() == null
                ? event.getLocation()
                : LocationMapper.locationDtoToLocation(request.getLocation());

        boolean paid = request.getPaid() == null ? event.isPaid() : request.getPaid();

        if (request.getStateAction() != null) {
            if (StateAction.valueOf(request.getStateAction()) == StateAction.PUBLISH_EVENT) {
                event.setState(StateEvent.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            } else if (StateAction.valueOf(request.getStateAction()) == StateAction.REJECT_EVENT) {
                event.setState(StateEvent.CANCELED);
            }
        }

        Integer participantLimit = request.getParticipantLimit() == null
                ? event.getParticipantLimit()
                : request.getParticipantLimit();

        boolean requestModeration = request.getRequestModeration() == null
                ? event.isRequestModeration()
                : request.getRequestModeration();

        String title = request.getTitle() == null ? event.getTitle() : request.getTitle();

        Category category = request.getCategory() == null
                ? event.getCategory()
                : CategoryMapper.categoryDtoToCategory(categoryDao.getCategoryDto(request.getCategory()));

        return event.toBuilder()
                .annotation(annotation)
                .description(description)
                .location(location)
                .paid(paid)
                .participantLimit(participantLimit)
                .requestModeration(requestModeration)
                .title(title)
                .category(category)
                .build();
    }
}
