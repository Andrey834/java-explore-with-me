package ru.practicum.ewmmain.mapper;

import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.ewmmain.enums.StateEvent;
import ru.practicum.ewmmain.model.Category;
import ru.practicum.ewmmain.model.Event;
import ru.practicum.ewmmain.model.Location;
import ru.practicum.ewmmain.model.User;
import ru.practicum.util.DateFormatter;

import java.time.LocalDateTime;

public class EventMapper {
    public static EventShortDto eventToEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.categoryToCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate().format(DateFormatter.FORMATTER))
                .initiator(UserMapper.userToUserShortDto(event.getInitiator()))
                .paid(event.isPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static EventFullDto eventToEventFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.categoryToCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn().format(DateFormatter.FORMATTER))
                .description(event.getDescription())
                .eventDate(event.getEventDate().format(DateFormatter.FORMATTER))
                .initiator(UserMapper.userToUserShortDto(event.getInitiator()))
                .location(LocationMapper.locationToLocationDto(event.getLocation()))
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn() == null ? null : event.getPublishedOn()
                        .format(DateFormatter.FORMATTER))
                .requestModeration(event.isRequestModeration())
                .state(event.getState().getValue())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static Event newEventDtoToEvent(User user, Category category, NewEventDto newEventDto, Location location) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(category)
                .description(newEventDto.getDescription())
                .eventDate(LocalDateTime.parse(newEventDto.getEventDate(), DateFormatter.FORMATTER))
                .location(location)
                .initiator(user)
                .paid(newEventDto.isPaid())
                .state(StateEvent.PENDING)
                .confirmedRequests(0L)
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.isRequestModeration())
                .title(newEventDto.getTitle())
                .views(0L)
                .build();
    }
}
