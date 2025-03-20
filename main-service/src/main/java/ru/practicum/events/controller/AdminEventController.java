package ru.practicum.events.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.events.service.EventService;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class AdminEventController {

    private final EventRepository eventRepository;

    private final EventService eventService;
    private final ObjectMapper objectMapper;

    @GetMapping
    public List<EventFullDto> getEventById(List<Long> userIds, List<String> states, List<Long> categories, String rangeStart,
                                           String rangeEnd, Long from, Long size, HttpServletRequest request) {
        return eventService.getEventById(userIds, states, categories, rangeStart, rangeEnd, from, size, request);
    }
}
