package ru.practicum.statserver.service;

import ru.practicum.statserver.dto.EndpointHitDto;
import ru.practicum.statserver.model.ViewStats;

import java.util.List;
import java.util.Set;

public interface StatService {
    EndpointHitDto save(EndpointHitDto endpointHitDto);

    List<ViewStats> get(String start, String end, boolean unique, Set<String> uris);
}
