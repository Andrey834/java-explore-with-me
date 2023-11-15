package ru.practicum.statgateway;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.statgateway.dto.EndpointHitDto;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class StatController {
    private final StatClient client;

    @GetMapping("/stats")
    @Cacheable(cacheNames = "viewStatList", key = "#start + '_' + #end + '_' + #unique + '_' + #uris")
    public ResponseEntity<Object> get(@RequestParam(name = "start") String start,
                                      @RequestParam(name = "end") String end,
                                      @RequestParam(name = "unique", required = false, defaultValue = "false") boolean unique,
                                      @RequestParam(name = "uris", required = false) String uris
    ) {
        return client.get(start, end, unique, uris);
    }

    @PostMapping("/hit")
    @CacheEvict(value = "viewStatList", allEntries = true)
    public ResponseEntity<Object> hit(
            @RequestBody EndpointHitDto endpointHitDto
    ) {
        return client.post(endpointHitDto);
    }
}
