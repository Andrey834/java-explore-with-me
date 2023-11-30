package ru.practicum.ewmmain.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.dto.location.LocationDto;
import ru.practicum.dto.stat.EndpointHitDto;
import ru.practicum.dto.stat.ViewStats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestClient {
    @Value("${stat-client-url}")
    private String statUrl;
    @Value("${map-yandex-key}")
    private String apiKey;
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

    public LocationDto getLocationFromAddress(String address) {
        final String url = "https://geocode-maps.yandex.ru/1.x/?apikey={apikey}&geocode={geocode}&format=json";
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("apikey", apiKey);
        parameters.put("geocode", address);

        String stats = restTemplate.getForObject(url, String.class, parameters);

        if (stats != null) {
            StringBuilder sb = new StringBuilder(stats);
            int firstIndex = sb.lastIndexOf(("\"Point\":{\"pos\":\"")) + "\"Point\":{\"pos\":\"".length();
            int lastIndex = sb.lastIndexOf(("\"}}}]}}}"));

            List<Float> loc = Arrays.stream(sb.substring(firstIndex, lastIndex).split(" "))
                    .map(Float::valueOf)
                    .collect(Collectors.toList());

            return LocationDto.builder().lon(loc.get(0)).lat(loc.get(1)).build();
        }
        return null;
    }
}
