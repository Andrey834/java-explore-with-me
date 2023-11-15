package ru.practicum.ewmmain.mapper;

import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.ewmmain.enums.ParticipationRequestStatus;
import ru.practicum.ewmmain.model.Event;
import ru.practicum.ewmmain.model.ParticipationRequest;
import ru.practicum.ewmmain.model.User;
import ru.practicum.util.DateFormatter;

import java.util.List;
import java.util.stream.Collectors;

public class RequestMapper {
    public static ParticipationRequest requestDtoToRequest(ParticipationRequestDto requestDto, Event event, User user) {
        return ParticipationRequest.builder()
                .event(event)
                .requester(user)
                .status(ParticipationRequestStatus.valueOf(requestDto.getStatus()))
                .build();
    }

    public static ParticipationRequestDto requestToRequestDto(ParticipationRequest request) {
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .created(request.getCreated().format(DateFormatter.FORMATTER))
                .status(request.getStatus().getValue())
                .build();
    }

    public static List<ParticipationRequestDto> requestsDtoToRequests(List<ParticipationRequest> requests) {
        return requests.stream()
                .map(RequestMapper::requestToRequestDto)
                .collect(Collectors.toList());

    }
}
