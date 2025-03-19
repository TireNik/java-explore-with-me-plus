package ru.practicum.events.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.EventState;

import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    Optional<Event> findByIdAndState(Long id, EventState state);

}