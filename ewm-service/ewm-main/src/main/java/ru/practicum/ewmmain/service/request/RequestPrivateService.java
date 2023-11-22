package ru.practicum.ewmmain.service.request;

import ru.practicum.dto.request.ParticipationRequestDto;

import java.util.List;

public interface RequestPrivateService {

    List<ParticipationRequestDto> getUserRequests(long userId);

    ParticipationRequestDto addParticipationRequest(long userId, long eventId);

    ParticipationRequestDto cancelRequest(long userId, long requestId);
}
