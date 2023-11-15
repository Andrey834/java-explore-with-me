package ru.practicum.ewmmain.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.dto.stat.EndpointHitDto;
import ru.practicum.dto.stat.ViewStats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatClient {
    @Value("${stat-client-url}")
    private String statUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    public void sendHit(String app, String uri, String ip) {
        HttpEntity<EndpointHitDto> request = new HttpEntity<EndpointHitDto>(
                EndpointHitDto.builder().app(app).uri(uri).ip(ip).build());

        restTemplate.postForObject(statUrl + "/hit", request, String.class);
    }

    public List<ViewStats> getStat(String uri) {
        final String url = "/stats?uris={uris}&unique=true&start={start}&end={end}";
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("uris", uri);
        parameters.put("start", "1970-01-01 12:00:00");
        parameters.put("end", "2300-01-01 12:00:00");

        ViewStats[] stats =
                restTemplate.getForObject(statUrl + url, ViewStats[].class, parameters);

        if (stats == null) return new ArrayList<>();
        return Arrays.asList(stats);
    }
}
