package ru.practicum.ewmmain.mapper;

import ru.practicum.dto.location.LocationDto;
import ru.practicum.dto.location.LocationFullDto;
import ru.practicum.dto.location.NewLocationDto;
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

    public static LocationFullDto locationToLocationFullDto(Location location) {
        return LocationFullDto.builder()
                .id(location.getId())
                .lon(location.getLon())
                .lat(location.getLat())
                .radius(location.getRadius())
                .build();
    }

    public static Location newLocationDtoToLocation(NewLocationDto newLocationDto) {
        return Location.builder()
                .lon(newLocationDto.getLon())
                .lat(newLocationDto.getLat())
                .radius(newLocationDto.getRadius())
                .build();
    }

    public static NewLocationDto locationDtoToNewLocationDto(LocationDto locationDto, int radius) {
        return NewLocationDto.builder()
                .lon(locationDto.getLon())
                .lat(locationDto.getLat())
                .radius(radius)
                .build();
    }
}
