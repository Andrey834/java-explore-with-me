package ru.practicum.ewmmain.controller.privats;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.location.LocationShortDto;
import ru.practicum.dto.location.NewLocationDto;
import ru.practicum.dto.request.LocationRequestDto;
import ru.practicum.ewmmain.service.location.LocationPrivateService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/location")
public class LocationPrivateController {
    private final LocationPrivateService locationPrivateService;

    @PostMapping("/request")
    public ResponseEntity<LocationRequestDto> addLocationRequest(@PathVariable(name = "userId") long userId,
                                                                 @RequestBody NewLocationDto newLocationDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(locationPrivateService.addLocationRequest(userId, newLocationDto));
    }

    @GetMapping("/request/{reqId}")
    public ResponseEntity<LocationRequestDto> getLocationRequest(@PathVariable(name = "userId") long userId,
                                                                 @PathVariable(name = "reqId") long reqId) {
        return ResponseEntity.ok(locationPrivateService.getLocationRequest(userId, reqId));
    }

    @GetMapping
    public ResponseEntity<List<LocationShortDto>> getAllLocations(@PathVariable(name = "userId") long userId) {
        return ResponseEntity.ok(locationPrivateService.getAllLocations(userId));
    }
}
