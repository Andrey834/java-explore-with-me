package ru.practicum.ewmmain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmmain.model.Location;

@Repository
public interface LocationDao extends JpaRepository<Location, Long> {
}
