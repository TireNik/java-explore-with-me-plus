package ru.practicum.events.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class AdminEventController {

    private final EventRepository eventRepository;

    private final ObjectMapper objectMapper;

    @GetMapping
    public PagedModel<Event> getAll(Pageable pageable) {
        Page<Event> events = eventRepository.findAll(pageable);
        return new PagedModel<>(events);
    }

    @GetMapping("/{id}")
    public Event getOne(@PathVariable Long id) {
        Optional<Event> eventOptional = eventRepository.findById(id);
        return eventOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
    }

    @GetMapping("/by-ids")
    public List<Event> getMany(@RequestParam List<Long> ids) {
        return eventRepository.findAllById(ids);
    }

    @PostMapping
    public Event create(@RequestBody Event event) {
        return eventRepository.save(event);
    }

    @PatchMapping("/{id}")
    public Event patch(@PathVariable Long id, @RequestBody JsonNode patchNode) throws IOException {
        Event event = eventRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        objectMapper.readerForUpdating(event).readValue(patchNode);

        return eventRepository.save(event);
    }

    @PatchMapping
    public List<Long> patchMany(@RequestParam List<Long> ids, @RequestBody JsonNode patchNode) throws IOException {
        Collection<Event> events = eventRepository.findAllById(ids);

        for (Event event : events) {
            objectMapper.readerForUpdating(event).readValue(patchNode);
        }

        List<Event> resultEvents = eventRepository.saveAll(events);
        return resultEvents.stream()
                .map(Event::getId)
                .toList();
    }

    @DeleteMapping("/{id}")
    public Event delete(@PathVariable Long id) {
        Event event = eventRepository.findById(id).orElse(null);
        if (event != null) {
            eventRepository.delete(event);
        }
        return event;
    }

    @DeleteMapping
    public void deleteMany(@RequestParam List<Long> ids) {
        eventRepository.deleteAllById(ids);
    }
}
