package ru.practicum.ewmmain.controller.privats;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdateEventUserRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.ewmmain.service.event.EventPrivateService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/events")
public class EventPrivateController {
    private final EventPrivateService eventPrivateService;

    @GetMapping()
    public ResponseEntity<List<EventShortDto>> getEvents(
            @PathVariable(name = "userId") long userId,
            @RequestParam(name = "from", required = false, defaultValue = "0") @Min(1) Integer from,
            @RequestParam(name = "size", required = false, defaultValue = "10") @Min(1) @Max(100) Integer size,
            HttpServletRequest request) {

        log.info("User {} {} Events from ip address: {}", userId, request.getMethod(), request.getRemoteAddr());

        PageRequest pageRequest = PageRequest.of(from, size, Sort.by("id"));
        return ResponseEntity
                .status(200)
                .body(eventPrivateService.getEvents(userId, pageRequest));
    }

    @PostMapping
    public ResponseEntity<EventFullDto> addEvent(
            @PathVariable(name = "userId") long userId,
            @Valid @RequestBody NewEventDto newEventDto,
            HttpServletRequest request) {

        log.info("User {} {} Event from ip address: {}", userId, request.getMethod(), request.getRemoteAddr());

        return ResponseEntity
                .status(201)
                .body(eventPrivateService.addEvent(userId, newEventDto));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getEvent(
            @PathVariable(name = "userId") long userId,
            @PathVariable(name = "eventId") long eventId,
            HttpServletRequest request) {

        log.info("User {} {} Event №{} from ip address: {}",
                userId, request.getMethod(), eventId, request.getRemoteAddr());

        return ResponseEntity.ok(eventPrivateService.getEvent(userId, eventId));
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateEvent(
            @PathVariable(name = "userId") long userId,
            @PathVariable(name = "eventId") long eventId,
            @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest,
            HttpServletRequest request) {

        log.info("User {} {} Event №{} from ip address: {}",
                userId, request.getMethod(), eventId, request.getRemoteAddr());

        return ResponseEntity.ok(eventPrivateService.updateEvent(userId, eventId, updateEventUserRequest));
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getEventParticipants(
            @PathVariable(name = "userId") long userId,
            @PathVariable(name = "eventId") long eventId,
            HttpServletRequest request) {

        log.info("User {} {} Requests for Event №{} from ip address: {}",
                userId, request.getMethod(), eventId, request.getRemoteAddr());

        return ResponseEntity.ok(eventPrivateService.getEventParticipants(userId, eventId));
    }

    @PatchMapping("/{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResult> changeRequestStatus(
            @PathVariable(name = "userId") long userId,
            @PathVariable(name = "eventId") long eventId,
            @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest,
            HttpServletRequest request) {

        log.info("User {} {} Request for Event №{} from ip address: {}",
                userId, request.getMethod(), eventId, request.getRemoteAddr());

        return ResponseEntity.ok(eventPrivateService.changeRequestStatus(
                userId, eventId, eventRequestStatusUpdateRequest));
    }
}

