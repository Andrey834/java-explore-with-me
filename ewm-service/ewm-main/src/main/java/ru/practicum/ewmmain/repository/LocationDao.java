package ru.practicum.ewmmain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmmain.model.Location;

import java.util.Optional;

@Repository
public interface LocationDao extends JpaRepository<Location, Long> {
    boolean existsByLatAndLon(float lat, float lon);

    Optional<Location> findByLatAndLon(float lat, float lon);
}
