package ru.practicum.ewmmain.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.ewmmain.enums.StateEvent;
import ru.practicum.ewmmain.service.event.EventAdminService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/events")
public class EventAdminController {
    private final EventAdminService eventAdminService;

    @GetMapping()
    public ResponseEntity<List<EventFullDto>> getEvents(
            @RequestParam(name = "users", required = false) List<Long> users,
            @RequestParam(name = "states", required = false) List<StateEvent> states,
            @RequestParam(name = "categories", required = false) List<Long> categories,
            @RequestParam(name = "rangeStart", required = false, defaultValue = "1980-01-01 12:00:00")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(name = "rangeEnd", required = false, defaultValue = "2300-01-01 12:00:00")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(name = "address", required = false) String address,
            @RequestParam(name = "from", required = false, defaultValue = "0") @Min(1) Integer from,
            @RequestParam(name = "size", required = false, defaultValue = "10") @Min(1) @Max(100) Integer size,
            HttpServletRequest request) {

        log.info("Admin {} Events from ip address: {}", request.getMethod(), request.getRemoteAddr());

        return ResponseEntity.ok(eventAdminService.getEvents(
                users, states, categories, address, rangeStart, rangeEnd, PageRequest.of(from, size)));
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateEvent(
            @PathVariable(name = "eventId") long eventId,
            @Valid @RequestBody UpdateEventAdminRequest updateEventAdminRequest,
            HttpServletRequest request) {

        log.info("Admin {} Event №{} from ip address: {}", request.getMethod(), eventId, request.getRemoteAddr());

        return ResponseEntity.ok(eventAdminService.updateEvent(eventId, updateEventAdminRequest));
    }
}

