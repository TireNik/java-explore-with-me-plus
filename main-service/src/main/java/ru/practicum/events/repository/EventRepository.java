package ru.practicum.events.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.practicum.events.model.Event;

import java.util.List;
import java.util.Optional;


public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    Optional<Event> findByIdAndInitiatorId(Long eventId, Long userId);

    List<Event> findByInitiatorId(Long userId, Pageable pageable);

    List<Event> findEventsByIdIn(List<Long> ids);

    List<Event> findByCategoryId(Long catId);
}