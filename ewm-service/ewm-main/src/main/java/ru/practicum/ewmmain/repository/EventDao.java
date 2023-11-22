package ru.practicum.ewmmain.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmmain.enums.StateEvent;
import ru.practicum.ewmmain.model.Event;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventDao extends JpaRepository<Event, Long> {

    @Query(value = "SELECT e " +
                   "FROM Event e " +
                   "WHERE (:users is null OR e.initiator.id IN :users) " +
                   "AND (:states is null OR e.state IN :states) " +
                   "AND (:categories is null OR e.category.id IN :categories) " +
                   "AND e.eventDate BETWEEN :rangeStart AND :rangeEnd " +
                   "ORDER BY e.id")
    List<Event> getEventsAdminWithParam(@Param(value = "users") List<Long> users,
                                        @Param(value = "states") List<StateEvent> states,
                                        @Param(value = "categories") List<Long> categories,
                                        @Param(value = "rangeStart") LocalDateTime rangeStart,
                                        @Param(value = "rangeEnd") LocalDateTime rangeEnd,
                                        PageRequest pageRequest);

    @Query(value = "SELECT e " +
                   "FROM Event e " +
                   "WHERE e.eventDate BETWEEN :rangeStart AND :rangeEnd " +
                   "AND (:text is null OR (lower(e.annotation) like lower(concat('%',:text,'%')) " +
                   "OR lower(e.description) like lower(concat('%',:text,'%')))) " +
                   "AND (:paid is null OR e.paid = :paid) " +
                   "AND (:categories is null OR e.category.id IN :categories) " +
                   "AND (:onlyAvailable is null OR e.participantLimit > e.confirmedRequests) " +
                   "AND e.state = 'PUBLISHED' " +
                   "ORDER BY e.eventDate DESC")
    List<Event> getEventsPublicWithParam(@Param(value = "text") String text,
                                         @Param(value = "paid") Boolean paid,
                                         @Param(value = "categories") List<Long> categories,
                                         @Param(value = "onlyAvailable") Boolean onlyAvailable,
                                         @Param(value = "rangeStart") LocalDateTime rangeStart,
                                         @Param(value = "rangeEnd") LocalDateTime rangeEnd,
                                         PageRequest pageRequest);

    List<Event> findAllByInitiatorIdOrderByEventDate(long userId, PageRequest pageRequest);
}
