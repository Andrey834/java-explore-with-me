package ru.practicum.ewmmain.service.location;

import ru.practicum.dto.location.LocationFullDto;
import ru.practicum.dto.location.NewLocationDto;

import java.util.List;

public interface LocationAdminService {
    List<LocationFullDto> getAll();

    LocationFullDto get(long locId);

    LocationFullDto add(NewLocationDto newLocationDto);

    LocationFullDto add(String address, int radius);

    LocationFullDto update(long locId, NewLocationDto newLocationDto);

    void delete(long locId);
}
