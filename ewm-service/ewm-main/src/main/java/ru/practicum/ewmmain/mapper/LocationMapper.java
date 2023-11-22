package ru.practicum.ewmmain.mapper;

import ru.practicum.dto.event.LocationDto;
import ru.practicum.ewmmain.model.Location;

public class LocationMapper {
    public static Location locationDtoToLocation(LocationDto locationDto) {
        return Location.builder()
                .lon(locationDto.getLon())
                .lat(locationDto.getLat())
                .build();
    }

    public static LocationDto locationToLocationDto(Location location) {
        return LocationDto.builder()
                .lon(location.getLon())
                .lat(location.getLat())
                .build();
    }
}
