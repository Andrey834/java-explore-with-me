package ru.practicum.statserver.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.statserver.dto.EndpointHitDto;
import ru.practicum.statserver.model.ViewStats;
import ru.practicum.statserver.service.StatService;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class StatController {
    private final StatService service;

    @PostMapping("/hit")
    ResponseEntity<EndpointHitDto> save(@RequestBody EndpointHitDto endpointHitDto) {
        return ResponseEntity
                .status(201)
                .body(service.save(endpointHitDto));
    }

    @GetMapping("/stats")
    ResponseEntity<List<ViewStats>> get(@RequestParam(name = "start") String start,
                                        @RequestParam(name = "end") String end,
                                        @RequestParam(name = "uris", required = false) Set<String> uris,
                                        @RequestParam(
                                                name = "unique",
                                                required = false,
                                                defaultValue = "false") boolean unique) {
        return ResponseEntity.ok(service.get(start, end, unique, uris));
    }
}
