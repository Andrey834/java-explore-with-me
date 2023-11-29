package ru.practicum.ewmmain.service.location;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.location.LocationFullDto;
import ru.practicum.dto.location.NewLocationDto;
import ru.practicum.dto.request.LocationRequestDto;
import ru.practicum.dto.request.LocationRequestsResult;
import ru.practicum.ewmmain.enums.LocationRequestStatus;
import ru.practicum.ewmmain.mapper.LocationMapper;
import ru.practicum.ewmmain.mapper.LocationRequestMapper;
import ru.practicum.ewmmain.model.Location;
import ru.practicum.ewmmain.model.LocationRequest;
import ru.practicum.ewmmain.repository.LocationDao;
import ru.practicum.ewmmain.repository.LocationRequestDao;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationAdminServiceImpl implements LocationAdminService {
    private final LocationRequestDao locationRequestDao;
    private final LocationDao locationDao;

    @Override
    public LocationFullDto add(NewLocationDto newLocationDto) {
        Location location = locationDao.save(LocationMapper.newLocationDtoToLocation(newLocationDto));
        return LocationMapper.locationToLocationFullDto(location);
    }

    @Override
    public LocationFullDto update(long locId, NewLocationDto newLocationDto) {
        Location oldLocation = locationDao.findById(locId).orElseThrow(
                () -> new NoSuchElementException("Location with ID=" + locId + " was not found"));

        Location updateLocation = Location.builder()
                .id(oldLocation.getId())
                .description(newLocationDto.getDescription() == null
                        ? oldLocation.getDescription()
                        : newLocationDto.getDescription())
                .lat(newLocationDto.getLat())
                .lon(newLocationDto.getLon())
                .radius(newLocationDto.getRadius())
                .build();

        return LocationMapper.locationToLocationFullDto(locationDao.save(updateLocation));
    }

    @Override
    public LocationFullDto get(long locId) {
        return LocationMapper.locationToLocationFullDto(locationDao.findById(locId).orElseThrow(
                () -> new NoSuchElementException("Location with ID=" + locId + " was not found")));
    }

    @Override
    public List<LocationFullDto> getAll() {
        return locationDao.findAll().stream()
                .map(LocationMapper::locationToLocationFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(long locId) {
        if (locationDao.existsById(locId)) {
            locationDao.deleteById(locId);
        } else {
            throw new NoSuchElementException("Location with ID=" + locId + " was not found");
        }
    }

    @Override
    public List<LocationRequestDto> getUsersLocation() {
        return locationRequestDao.findAllByStatus(LocationRequestStatus.PENDING).stream()
                .map(LocationRequestMapper::locationRequestToLocationRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public LocationRequestsResult approvedUsersLocations(List<Long> approvedIds, List<Long> rejectIds) {
        List<LocationRequest> result = new ArrayList<>();

        List<Long> ids = new ArrayList<>(approvedIds);
        ids.addAll(rejectIds);

        locationRequestDao.findAllByIdInAndStatus(ids, LocationRequestStatus.PENDING).stream()
                .map(req -> {
                    if (approvedIds.contains(req.getId())) {
                        return req.toBuilder().status(LocationRequestStatus.ADDED).build();
                    } else {
                        return req.toBuilder().status(LocationRequestStatus.CANCELLED).build();
                    }
                }).forEach(result::add);

        locationRequestDao.saveAll(result);

        List<Location> locations = new ArrayList<>();

        result.stream()
                .filter(request -> request.getStatus() == LocationRequestStatus.ADDED)
                .map(request -> Location.builder()
                        .lon(request.getLon())
                        .lat(request.getLat())
                        .description(request.getDescription())
                        .radius(request.getRadius())
                        .build()
                ).forEach(locations::add);

        locationDao.saveAll(locations);

        return LocationRequestsResult.builder()
                .confirmedLocation(result.stream()
                        .filter(request -> request.getStatus() == LocationRequestStatus.ADDED)
                        .map(LocationRequestMapper::locationRequestToLocationRequestDto)
                        .collect(Collectors.toList()))
                .rejectedLocation(result.stream()
                        .filter(request -> request.getStatus() == LocationRequestStatus.CANCELLED)
                        .map(LocationRequestMapper::locationRequestToLocationRequestDto)
                        .collect(Collectors.toList()))
                .build();
    }
}
