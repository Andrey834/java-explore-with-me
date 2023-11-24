package ru.practicum.ewmmain.controller.privats;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.ewmmain.service.request.RequestPrivateService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/requests")
public class RequestPrivateController {
    private final RequestPrivateService requestPrivateService;

    @GetMapping
    public ResponseEntity<List<ParticipationRequestDto>> addParticipationRequest(
            @PathVariable(value = "userId") long userId) {
        return ResponseEntity.ok(requestPrivateService.getUserRequests(userId));
    }

    @PostMapping
    public ResponseEntity<ParticipationRequestDto> getUserRequests(@PathVariable(value = "userId") long userId,
                                                                   @RequestParam(name = "eventId") long eventId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(requestPrivateService.addParticipationRequest(userId, eventId));
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> cancelRequest(@PathVariable(value = "userId") long userId,
                                                                 @PathVariable(value = "requestId") long requestId) {
        return ResponseEntity.ok(requestPrivateService.cancelRequest(userId, requestId));
    }

}
