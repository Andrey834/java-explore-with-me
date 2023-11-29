package ru.practicum.ewmmain.mapper;

import org.apache.commons.lang3.StringUtils;
import ru.practicum.dto.location.LocationFullDto;
import ru.practicum.dto.location.LocationShortDto;
import ru.practicum.dto.location.NewLocationDto;
import ru.practicum.ewmmain.model.Location;

public class LocationMapper {
    public static Location locationDtoToLocation(LocationShortDto locationShortDto) {
        return Location.builder()
                .lon(locationShortDto.getLon())
                .lat(locationShortDto.getLat())
                .build();
    }

    public static Location newLocationDtoToLocation(NewLocationDto newLocationDto) {

        return Location.builder()
                .description(StringUtils.capitalize(newLocationDto.getDescription())
                        .trim().replaceAll("\\s+", " "))
                .lon(newLocationDto.getLon())
                .lat(newLocationDto.getLat())
                .radius(newLocationDto.getRadius())
                .build();
    }

    public static LocationFullDto locationToLocationFullDto(Location location) {

        return LocationFullDto.builder()
                .id(location.getId())
                .description(location.getDescription())
                .lon(location.getLon())
                .lat(location.getLat())
                .radius(location.getRadius())
                .build();
    }

    public static LocationShortDto locationToLocationDto(Location location) {
        return LocationShortDto.builder()
                .lon(location.getLon())
                .lat(location.getLat())
                .build();
    }
}
