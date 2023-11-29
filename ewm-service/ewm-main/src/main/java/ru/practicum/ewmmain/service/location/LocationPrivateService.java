package ru.practicum.ewmmain.service.location;

import ru.practicum.dto.location.LocationFullDto;
import ru.practicum.dto.location.LocationShortDto;
import ru.practicum.dto.location.NewLocationDto;
import ru.practicum.dto.request.LocationRequestDto;

import java.util.List;

public interface LocationPrivateService {
    LocationRequestDto addLocationRequest(long userId, NewLocationDto newLocationDto);

    LocationRequestDto getLocationRequest(long userId, long reqId);

    List<LocationShortDto> getAllLocations(long userId);
}
