package ru.practicum.ewmmain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmmain.model.Location;

import java.util.Optional;

@Repository
public interface LocationDao extends JpaRepository<Location, Long> {
    boolean existsByLonAndLatAndRadius(float lon, float lat, int radius);

    @Query(value = "SELECT l.* " +
                   "FROM location l " +
                   "WHERE distance(lat, lon, :inLat, :inLon) <= cast(radius as float) " +
                   "ORDER BY distance(lat, lon, :inLat, :inLon) " +
                   "LIMIT 1", nativeQuery = true)
    Optional<Location> getLocationByDistance(@Param(value = "inLat") float lat, @Param(value = "inLon") float lon);
}
