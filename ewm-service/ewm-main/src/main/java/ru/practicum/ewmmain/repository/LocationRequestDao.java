package ru.practicum.ewmmain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmmain.enums.LocationRequestStatus;
import ru.practicum.ewmmain.model.LocationRequest;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRequestDao extends JpaRepository<LocationRequest, Long> {
    boolean existsByLonAndLatAndRadius(float lon, float lat, int radius);

    List<LocationRequest> findAllByStatus(LocationRequestStatus status);

    List<LocationRequest> findAllByIdInAndStatus(List<Long> ids, LocationRequestStatus status);

    Optional<LocationRequest> findByIdAndUserId(long reqId, long userId);
}
