package ru.practicum.statserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.statserver.model.ViewStats;
import ru.practicum.statserver.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface StatRepository extends JpaRepository<EndpointHit, Long> {

    @Query(value = "SELECT new ru.practicum.statserver.model.ViewStats(e.app, e.uri, COUNT(e.ip)) " +
                   "FROM EndpointHit e " +
                   "WHERE e.timestamp BETWEEN :start AND :end " +
                   "GROUP BY e.app, e.uri")
    List<ViewStats> getHits(@Param("start") LocalDateTime start,
                            @Param("end") LocalDateTime end);

    @Query(value = "SELECT new ru.practicum.statserver.model.ViewStats(e.app, e.uri, COUNT(e.ip)) " +
                   "FROM EndpointHit e " +
                   "WHERE e.timestamp BETWEEN :start AND :end " +
                   "  AND e.uri IN :uris " +
                   "GROUP BY e.app, e.uri")
    List<ViewStats> getHitsWithUris(@Param("start") LocalDateTime start,
                                    @Param("end") LocalDateTime end,
                                    @Param("uris") Set<String> uris);

    @Query(value = "SELECT new ru.practicum.statserver.model.ViewStats(e.app, e.uri, COUNT(DISTINCT e.ip)) " +
                   "FROM EndpointHit e " +
                   "WHERE e.timestamp BETWEEN :start AND :end " +
                   "GROUP BY e.app, e.uri")
    List<ViewStats> getHitsWithUnique(@Param("start") LocalDateTime start,
                                      @Param("end") LocalDateTime end);

    @Query(value = "SELECT new ru.practicum.statserver.model.ViewStats(e.app, e.uri, COUNT(DISTINCT e.ip)) " +
                   "FROM EndpointHit e " +
                   "WHERE e.timestamp BETWEEN :start AND :end " +
                   "  AND e.uri IN :uris " +
                   "GROUP BY e.app, e.uri")
    List<ViewStats> getHitsWithUrisAndUnique(@Param("start") LocalDateTime start,
                                             @Param("end") LocalDateTime end,
                                             @Param("uris") Set<String> uris);

}
