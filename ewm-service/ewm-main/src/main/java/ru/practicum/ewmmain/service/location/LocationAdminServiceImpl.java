package ru.practicum.ewmmain.service.location;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.location.LocationDto;
import ru.practicum.dto.location.LocationFullDto;
import ru.practicum.dto.location.NewLocationDto;
import ru.practicum.ewmmain.client.RestClient;
import ru.practicum.ewmmain.exception.DuplicateLocationException;
import ru.practicum.ewmmain.mapper.LocationMapper;
import ru.practicum.ewmmain.model.Location;
import ru.practicum.ewmmain.repository.LocationDao;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationAdminServiceImpl implements LocationAdminService {
    private final LocationDao locationDao;
    private final RestClient client;


    @Override
    public List<LocationFullDto> getAll() {
        return locationDao.findAll().stream()
                .map(LocationMapper::locationToLocationFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public LocationFullDto get(long locId) {
        Location location = locationDao.findById(locId).orElseThrow(
                () -> new NoSuchElementException("Location with ID=" + locId + " was not found"));
        return LocationMapper.locationToLocationFullDto(location);
    }

    @Override
    public LocationFullDto add(NewLocationDto newLocationDto) {
        checkDuplicateLocation(newLocationDto);
        Location location = LocationMapper.newLocationDtoToLocation(newLocationDto);

        Location newLocation = locationDao.save(location);
        return LocationMapper.locationToLocationFullDto(newLocation);
    }

    @Override
    public LocationFullDto add(String address, int radius) {
        NewLocationDto newLocationDto = LocationMapper.locationDtoToNewLocationDto(
                client.getLocationFromAddress(address), radius);

        checkDuplicateLocation(newLocationDto);

        Location location = locationDao.save(LocationMapper.newLocationDtoToLocation(newLocationDto));
        return LocationMapper.locationToLocationFullDto(location);
    }


    @Override
    public LocationFullDto update(long locId, NewLocationDto newLocationDto) {
        checkDuplicateLocation(newLocationDto);
        if (!locationDao.existsById(locId)) {
            throw new NoSuchElementException("Location with ID=" + locId + " was not found");
        }

        Location location = LocationMapper.newLocationDtoToLocation(newLocationDto);
        location.setId(locId);

        return LocationMapper.locationToLocationFullDto(locationDao.save(location));
    }

    @Override
    public void delete(long locId) {
        if (!locationDao.existsById(locId)) {
            throw new NoSuchElementException("Location with ID=" + locId + " was not found");
        }

        locationDao.deleteById(locId);
    }

    private void checkDuplicateLocation(NewLocationDto newLocationDto) {
        float lat = newLocationDto.getLat();
        float lon = newLocationDto.getLon();
        if (locationDao.existsByLatAndLon(lat, lon)) {
            throw new DuplicateLocationException("Duplicate location: " + newLocationDto);
        }
    }
}
