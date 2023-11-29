package ru.practicum.ewmmain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmmain.model.ParticipationRequest;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestDao extends JpaRepository<ParticipationRequest, Long> {

    @Query(value = "SELECT p " +
                   "FROM ParticipationRequest p " +
                   "WHERE p.requester.id = :userId " +
                   "ORDER BY p.id")
    List<ParticipationRequest> findAllByRequesterId(long userId);

    boolean existsByRequesterIdAndEventId(long userId, long eventId);

    Optional<ParticipationRequest> findByRequesterIdAndId(long userId, long requestId);

    @Query(value = "SELECT p " +
                   "FROM ParticipationRequest p " +
                   "WHERE p.event.initiator.id = :userId " +
                   "AND p.event.id = :eventId")
    List<ParticipationRequest> getParticipationRequest(@Param(value = "userId") long userId,
                                                       @Param(value = "eventId") long eventId);

    List<ParticipationRequest> findAllByEventId(long eventId);


}
