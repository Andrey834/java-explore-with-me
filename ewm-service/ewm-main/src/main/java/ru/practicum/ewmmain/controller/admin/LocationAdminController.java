package ru.practicum.ewmmain.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import ru.practicum.ewmmain.service.location.LocationAdminService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/location")
public class LocationAdminController {
    private final LocationAdminService service;

    @GetMapping
    public ResponseEntity<List<LocationFullDto>> getAll(HttpServletRequest request) {

        log.info("Admin {} Locations from ip address: {}", request.getMethod(), request.getRemoteAddr());

        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{locId}")
    public ResponseEntity<LocationFullDto> get(@PathVariable(name = "locId") long locId,
                                               HttpServletRequest request) {

        log.info("Admin {} Location №{} from ip address: {}", request.getMethod(), locId, request.getRemoteAddr());

        return ResponseEntity.ok(service.get(locId));
    }

    @PostMapping
    public ResponseEntity<LocationFullDto> add(@RequestBody NewLocationDto newLocationDto,
                                               HttpServletRequest request) {

        log.info("Admin {} Location from ip address: {}", request.getMethod(), request.getRemoteAddr());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.add(newLocationDto));
    }

    @PostMapping("/address")
    public ResponseEntity<LocationFullDto> addLocationWithAddress(@RequestParam(name = "address") String address,
                                                                  @RequestParam(name = "radius") int radius,
                                                                  HttpServletRequest request) {

        LocationFullDto locationFullDto = service.add(address, radius);

        log.info("Admin {} {} from ip address: {}", request.getMethod(), locationFullDto, request.getRemoteAddr());

        return ResponseEntity.status(HttpStatus.CREATED).body(locationFullDto);
    }

    @PatchMapping("/{locId}")
    public ResponseEntity<LocationFullDto> update(@PathVariable(name = "locId") long locId,
                                                  @RequestBody NewLocationDto newLocationDto,
                                                  HttpServletRequest request) {

        log.info("Admin {} Location №{} from ip address: {}", request.getMethod(), locId, request.getRemoteAddr());

        return ResponseEntity.ok(service.update(locId, newLocationDto));
    }

    @DeleteMapping("/{locId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(name = "locId") long locId, HttpServletRequest request) {

        log.info("Admin {} Location №{} from ip address: {}", request.getMethod(), locId, request.getRemoteAddr());

        service.delete(locId);
    }
}
