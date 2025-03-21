package ru.practicum.events.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.dto.UpdateEventUserRequestDto;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.events.service.EventService;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
public class PrivateEventController {

    private final EventService eventService;

    @GetMapping(path = "/{eventId}")
    public ResponseEntity<EventFullDto> getEvent(@PathVariable("userId") Long userId,
                                                 @PathVariable("eventId") Long eventId,
                                                 HttpServletRequest request) {
        EventFullDto dto = eventService.privateGetUserEvent(userId, eventId, request);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<EventFullDto> addEvent(
            @PathVariable Long userId,
            @Valid @RequestBody NewEventDto newEventDto) {

        EventFullDto event = eventService.addEvent(userId, newEventDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(event);
    }

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getEvents(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") @Min(0) int from,
            @RequestParam(defaultValue = "10") @Min(1) int size) {
        List<EventShortDto> events = eventService.getEvents(userId, from, size);
        return ResponseEntity.ok(events);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateEvent(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @Valid @RequestBody UpdateEventUserRequestDto updateRequest) {
        EventFullDto updatedEvent = eventService.updateEvent(userId, eventId, updateRequest);
        return ResponseEntity.ok(updatedEvent);
    }
}
