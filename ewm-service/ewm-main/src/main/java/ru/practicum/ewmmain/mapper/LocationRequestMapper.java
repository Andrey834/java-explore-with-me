package ru.practicum.ewmmain.mapper;

import ru.practicum.dto.location.NewLocationDto;
import ru.practicum.dto.request.LocationRequestDto;
import ru.practicum.ewmmain.model.LocationRequest;

public class LocationRequestMapper {

    public static LocationRequestDto locationRequestToLocationRequestDto(LocationRequest locationRequest) {
        NewLocationDto newLocationDto = NewLocationDto.builder()
                .description(locationRequest.getDescription())
                .lon(locationRequest.getLon())
                .lat(locationRequest.getLat())
                .radius(locationRequest.getRadius())
                .build();

        return LocationRequestDto.builder()
                .id(locationRequest.getId())
                .userId(locationRequest.getUserId())
                .location(newLocationDto)
                .status(locationRequest.getStatus().getValue())
                .build();
    }
}
