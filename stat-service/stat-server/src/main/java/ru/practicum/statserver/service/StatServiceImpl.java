package ru.practicum.statserver.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.QueryException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.statserver.mapper.EndpointHitMapper;
import ru.practicum.statserver.model.EndpointHit;
import ru.practicum.statserver.repository.StatRepository;
import ru.practicum.statserver.dto.EndpointHitDto;
import ru.practicum.statserver.model.ViewStats;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {
    private final StatRepository repository;

    @Transactional
    @Override
    public EndpointHitDto save(EndpointHitDto endpointHitDto) {
        EndpointHit newHit = repository.save(EndpointHitMapper.toEndpointHit(endpointHitDto));
        return EndpointHitMapper.toEndpointHitDto(newHit);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ViewStats> get(String start, String end, boolean isUnique, Set<String> uris) {
        LocalDateTime decodeStart = decodeDate(start);
        LocalDateTime decodeEnd = decodeDate(end);

        if(decodeStart.isAfter(decodeEnd)) {
            throw new QueryException(decodeStart + " not valid for start date");
        }

        if (isUnique && uris != null && !uris.isEmpty()) return repository.getHitsWithUrisAndUnique(decodeStart, decodeEnd, uris);
        if (isUnique) return repository.getHitsWithUnique(decodeStart, decodeEnd);
        if (uris != null && !uris.isEmpty()) return repository.getHitsWithUris(decodeStart, decodeEnd, uris);

        return repository.getHits(decodeStart, decodeEnd);
    }

    private LocalDateTime decodeDate(String date) {
        return LocalDateTime.parse(
                URLDecoder.decode(date, StandardCharsets.UTF_8),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
