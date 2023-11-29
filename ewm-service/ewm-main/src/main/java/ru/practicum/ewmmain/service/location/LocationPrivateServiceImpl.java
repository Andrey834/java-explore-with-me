package ru.practicum.ewmmain.service.location;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.location.LocationShortDto;
import ru.practicum.dto.location.NewLocationDto;
import ru.practicum.dto.request.LocationRequestDto;
import ru.practicum.ewmmain.enums.LocationRequestStatus;
import ru.practicum.ewmmain.exception.DuplicateLocationException;
import ru.practicum.ewmmain.mapper.LocationMapper;
import ru.practicum.ewmmain.mapper.LocationRequestMapper;
import ru.practicum.ewmmain.model.LocationRequest;
import ru.practicum.ewmmain.repository.LocationDao;
import ru.practicum.ewmmain.repository.LocationRequestDao;
import ru.practicum.ewmmain.service.user.UserService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationPrivateServiceImpl implements LocationPrivateService {
    private final UserService userService;
    private final LocationDao locationDao;
    private final LocationRequestDao locationRequestDao;

    @Override
    public LocationRequestDto addLocationRequest(long userId, NewLocationDto newLocationDto) {
        userService.existsUserById(userId);

        if (locationDao.existsByLonAndLatAndRadius(
                newLocationDto.getLon(), newLocationDto.getLat(), newLocationDto.getRadius())) {
            throw new DuplicateLocationException("This location exists");
        }

        if (locationRequestDao.existsByLonAndLatAndRadius(
                newLocationDto.getLon(), newLocationDto.getLat(), newLocationDto.getRadius())) {
            throw new DuplicateLocationException("This location is under moderation");
        }

        LocationRequest locationRequest = locationRequestDao.save(LocationRequest.builder()
                .lon(newLocationDto.getLon())
                .lat(newLocationDto.getLat())
                .description(newLocationDto.getDescription())
                .radius(newLocationDto.getRadius())
                .userId(userId)
                .status(LocationRequestStatus.PENDING)
                .build());

        return LocationRequestMapper.locationRequestToLocationRequestDto(locationRequest);
    }

    @Override
    public LocationRequestDto getLocationRequest(long userId, long reqId) {
        userService.existsUserById(userId);

        LocationRequest locationRequest = locationRequestDao.findByIdAndUserId(reqId, userId).orElseThrow(
                () -> new NoSuchElementException("request id= " + reqId + " was not found"));

        return LocationRequestMapper.locationRequestToLocationRequestDto(locationRequest);
    }

    @Override
    public List<LocationShortDto> getAllLocations(long userId) {
        userService.existsUserById(userId);
        return locationDao.findAll().stream()
                .map(LocationMapper::locationToLocationDto)
                .collect(Collectors.toList());
    }
}
