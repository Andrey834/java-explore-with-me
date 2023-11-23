package ru.practicum.ewmmain.service.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.ewmmain.enums.ParticipationRequestStatus;
import ru.practicum.ewmmain.enums.StateEvent;
import ru.practicum.ewmmain.exception.BadRequestEventException;
import ru.practicum.ewmmain.mapper.RequestMapper;
import ru.practicum.ewmmain.mapper.UserMapper;
import ru.practicum.ewmmain.model.Event;
import ru.practicum.ewmmain.model.ParticipationRequest;
import ru.practicum.ewmmain.model.User;
import ru.practicum.ewmmain.repository.RequestDao;
import ru.practicum.ewmmain.service.event.EventPrivateService;
import ru.practicum.ewmmain.service.user.UserService;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestPrivateServiceImpl implements RequestPrivateService {
    private final RequestDao requestDao;
    private final UserService userService;
    private final EventPrivateService eventPrivateService;

    @Override
    public List<ParticipationRequestDto> getUserRequests(long userId) {
        userService.existsUserById(userId);
        return RequestMapper.requestsDtoToRequests(requestDao.findAllByRequesterId(userId));
    }

    @Override
    @Transactional
    public ParticipationRequestDto addParticipationRequest(long userId, long eventId) {
        User user = UserMapper.userDtoToUser(userService.getUserDto(userId));
        Event event = eventPrivateService.findById(eventId);

        if (event.getInitiator().getId() == userId) {
            throw new BadRequestEventException("User with ID=" + userId + " is the initiator");
        } else if (event.getState() != StateEvent.PUBLISHED) {
            throw new BadRequestEventException("Event with ID=" + eventId + " not published");
        } else if (requestDao.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new BadRequestEventException("Repeated request is prohibited");
        } else if (event.getParticipantLimit() > 0 &&
                   event.getParticipantLimit() == event.getConfirmedRequests().longValue()) {
            throw new BadRequestEventException("Participation limit has been reached");
        }

        ParticipationRequestStatus status;
        if (!event.isRequestModeration() || event.getParticipantLimit() == 0) {
            status = ParticipationRequestStatus.CONFIRMED;
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventPrivateService.save(event);
        } else {
            status = ParticipationRequestStatus.PENDING;
        }

        ParticipationRequest request = requestDao.save(ParticipationRequest.builder()
                .requester(user)
                .event(event)
                .status(status)
                .build());

        return RequestMapper.requestToRequestDto(request);
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelRequest(long userId, long requestId) {
        userService.existsUserById(userId);
        ParticipationRequest request = requestDao.findByRequesterIdAndId(userId, requestId).orElseThrow(
                () -> new NoSuchElementException("Request with ID=" + requestId + " was not found"));

        ParticipationRequest cancelRequest = request.toBuilder()
                .status(ParticipationRequestStatus.CANCELED)
                .build();

        return RequestMapper.requestToRequestDto(requestDao.save(cancelRequest));
    }
}
