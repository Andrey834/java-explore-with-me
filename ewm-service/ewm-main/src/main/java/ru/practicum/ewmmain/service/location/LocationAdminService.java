package ru.practicum.ewmmain.service.location;

import ru.practicum.dto.location.LocationFullDto;
import ru.practicum.dto.location.NewLocationDto;
import ru.practicum.dto.request.LocationRequestDto;
import ru.practicum.dto.request.LocationRequestsResult;

import java.util.List;

public interface LocationAdminService {

    LocationFullDto add(NewLocationDto newLocationDto);

    LocationFullDto update(long locId, NewLocationDto newLocationDto);

    LocationFullDto get(long locId);

    List<LocationFullDto> getAll();

    void delete(long locId);

    List<LocationRequestDto> getUsersLocation();

    LocationRequestsResult approvedUsersLocations(List<Long> approvedIds, List<Long> rejectIds);

}
