package ru.practicum.ewmmain.service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdateEventUserRequest;
import ru.practicum.dto.location.LocationShortDto;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.ewmmain.enums.ParticipationRequestStatus;
import ru.practicum.ewmmain.enums.StateEvent;
import ru.practicum.ewmmain.exception.BadDataEventException;
import ru.practicum.ewmmain.exception.BadDateEventException;
import ru.practicum.ewmmain.exception.BadParticipantRequestException;
import ru.practicum.ewmmain.exception.BadStatusEventException;
import ru.practicum.ewmmain.mapper.CategoryMapper;
import ru.practicum.ewmmain.mapper.EventMapper;
import ru.practicum.ewmmain.mapper.LocationMapper;
import ru.practicum.ewmmain.mapper.RequestMapper;
import ru.practicum.ewmmain.mapper.UserMapper;
import ru.practicum.ewmmain.model.Category;
import ru.practicum.ewmmain.model.Event;
import ru.practicum.ewmmain.model.Location;
import ru.practicum.ewmmain.model.ParticipationRequest;
import ru.practicum.ewmmain.model.User;
import ru.practicum.ewmmain.repository.EventDao;
import ru.practicum.ewmmain.repository.LocationDao;
import ru.practicum.ewmmain.repository.RequestDao;
import ru.practicum.ewmmain.service.category.CategoryPublicService;
import ru.practicum.ewmmain.service.user.UserService;
import ru.practicum.util.DateFormatter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventPrivateServiceImpl implements EventPrivateService {
    private final EventDao eventDao;
    private final RequestDao requestDao;
    private final CategoryPublicService categoryPublicService;
    private final UserService userService;
    private final LocationDao locationDao;

    @Override
    public List<EventShortDto> getEvents(long userId, PageRequest pageRequest) {
        return eventDao.findAllByInitiatorIdOrderByEventDate(userId, pageRequest)
                .stream()
                .map(EventMapper::eventToEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto addEvent(long userId, NewEventDto newEventDto) {
        if (LocalDateTime.parse(
                newEventDto.getEventDate(), DateFormatter.FORMATTER).isBefore(LocalDateTime.now())) {
            throw new BadDateEventException("EventDate must be future");
        }

        CategoryDto category = categoryPublicService.getCategory(newEventDto.getCategory());

        LocationShortDto locationShortDto = newEventDto.getLocation();
        Location location = locationDao.getLocationByDistance(locationShortDto.getLat(), locationShortDto.getLon())
                .orElseThrow(() -> new NoSuchElementException("No suitable location found"));

        User user = UserMapper.userDtoToUser(userService.getUserDto(userId));
        Event event = EventMapper.newEventDtoToEvent(
                user, CategoryMapper.categoryDtoToCategory(category), newEventDto, location);

        return EventMapper.eventToEventFullDto(eventDao.save(event));
    }

    @Override
    public EventFullDto getEvent(long userId, long eventId) {
        userService.existsUserById(userId);
        return EventMapper.eventToEventFullDto(findById(eventId));
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(long userId, long eventId, UpdateEventUserRequest updateEventUserRequest) {
        userService.existsUserById(userId);
        Event event = findById(eventId);

        if (event.getState() == StateEvent.PUBLISHED) {
            throw new BadStatusEventException("Only pending or canceled events can be changed");
        } else if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadDataEventException("Event date less than two hours");
        }

        return EventMapper.eventToEventFullDto(
                eventDao.save(editEventWithRequest(event, updateEventUserRequest)));
    }

    @Override
    public List<ParticipationRequestDto> getEventParticipants(long userId, long eventId) {
        userService.existsUserById(userId);

        return requestDao.getParticipationRequest(userId, eventId).stream()
                .map(RequestMapper::requestToRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult changeRequestStatus(long userId,
                                                              long eventId,
                                                              EventRequestStatusUpdateRequest updateRequestStatus) {
        userService.existsUserById(userId);

        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();

        Event event = findById(eventId);
        if (event.getParticipantLimit().longValue() == event.getConfirmedRequests()) {
            throw new BadParticipantRequestException("limit on applications reached");
        } else if (event.getInitiator().getId() != eventId) {
            throw new BadParticipantRequestException("This is not your event");
        }

        long limit = event.getParticipantLimit() - event.getConfirmedRequests();

        List<ParticipationRequest> requests = requestDao.findAllByEventId(eventId);

        for (ParticipationRequest request : requests) {
            if (request.getStatus() != ParticipationRequestStatus.PENDING) {
                continue;
            }

            if (updateRequestStatus.getStatus().equals("CONFIRMED") && limit > 0
                && updateRequestStatus.getRequestIds().contains(request.getId())) {

                request.setStatus(ParticipationRequestStatus.CONFIRMED);
                result.getConfirmedRequests().add(RequestMapper.requestToRequestDto(request));
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                limit -= 1;
            } else {
                request.setStatus(ParticipationRequestStatus.REJECTED);
                result.getRejectedRequests().add(RequestMapper.requestToRequestDto(request));
            }
        }
        eventDao.save(event);
        requestDao.saveAll(requests);

        return result;
    }

    @Override
    public Event findById(long eventId) {
        return eventDao.findById(eventId).orElseThrow(
                () -> new NoSuchElementException("Event with ID=" + eventId + " was not found"));
    }

    @Override
    @Transactional
    public Event save(Event event) {
        return eventDao.save(event);
    }

    private Event editEventWithRequest(Event event, UpdateEventUserRequest request) {
        String annotation = (request.getAnnotation() == null) ? event.getAnnotation() : request.getAnnotation();
        String description = (request.getDescription() == null) ? event.getDescription() : request.getDescription();

        if (request.getEventDate() != null) {
            event.setEventDate(LocalDateTime.parse(request.getEventDate(), DateFormatter.FORMATTER));
            if (event.getEventDate().isBefore(LocalDateTime.now())) {
                throw new BadDateEventException("eventDate must be future");
            }
        }

        Location location = request.getLocation() == null
                ? event.getLocation()
                : locationDao.getLocationByDistance(request.getLocation().getLat(), request.getLocation().getLon())
                .orElseThrow(() -> new NoSuchElementException("No suitable location found"));

        boolean paid = request.getPaid() == null ? event.isPaid() : request.getPaid();

        Integer participantLimit = request.getParticipantLimit() == null
                ? event.getParticipantLimit()
                : request.getParticipantLimit();

        boolean requestModeration = request.getRequestModeration() == null
                ? event.isRequestModeration()
                : request.getRequestModeration();

        String title = request.getTitle() == null ? event.getTitle() : request.getTitle();

        Category category = request.getCategory() == null
                ? event.getCategory()
                : CategoryMapper.categoryDtoToCategory(
                categoryPublicService.getCategory(request.getCategory()));

        if (request.getStateAction() != null) {
            event.setState(request.getStateAction().equals("CANCEL_REVIEW")
                    ? StateEvent.CANCELED
                    : StateEvent.PENDING);
        }

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
