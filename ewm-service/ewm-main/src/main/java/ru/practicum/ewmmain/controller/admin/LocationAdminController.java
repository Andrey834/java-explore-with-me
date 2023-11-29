package ru.practicum.ewmmain.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.location.LocationFullDto;
import ru.practicum.dto.location.NewLocationDto;
import ru.practicum.dto.request.LocationRequestDto;
import ru.practicum.dto.request.LocationRequestsResult;
import ru.practicum.ewmmain.service.location.LocationAdminService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/location")
public class LocationAdminController {
    private final LocationAdminService locationAdminService;

    @PostMapping
    public ResponseEntity<LocationFullDto> addLocation(@RequestBody NewLocationDto newLocationDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(locationAdminService.add(newLocationDto));
    }

    @PatchMapping("/{locId}")
    public ResponseEntity<LocationFullDto> updateLocation(@PathVariable(name = "locId") long locId,
                                                           @RequestBody NewLocationDto newLocationDto) {
        return ResponseEntity.ok(locationAdminService.update(locId, newLocationDto));
    }

    @GetMapping("/{locId}")
    public ResponseEntity<LocationFullDto> getLocation(@PathVariable(name = "locId") long locId) {
        System.out.println(locationAdminService.get(locId));
        return ResponseEntity.ok(locationAdminService.get(locId));
    }

    @GetMapping
    public ResponseEntity<List<LocationFullDto>> getAllLocation() {
        return ResponseEntity.ok(locationAdminService.getAll());
    }

    @DeleteMapping("/{locId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLocation(@PathVariable(name = "locId") long locId) {
        locationAdminService.delete(locId);
    }


    @GetMapping("/request")
    public ResponseEntity<List<LocationRequestDto>> getUsersLocation() {
        return ResponseEntity.ok(locationAdminService.getUsersLocation());
    }

    @PatchMapping("/request")
    public ResponseEntity<LocationRequestsResult> approvedUsersLocations(
            @RequestParam(name = "approved", defaultValue = "") List<Long> approvedIds,
            @RequestParam(name = "reject", defaultValue = "") List<Long> rejectIds) {

        return ResponseEntity.ok(locationAdminService.approvedUsersLocations(approvedIds, rejectIds));
    }
}
