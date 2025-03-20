package ru.practicum.events.service;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.events.dto.*;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    List<EventShortDto> getPublicEvents(String text, List<Long> categories, Boolean paid,
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                        Boolean onlyAvailable, String sort, int from, int size,
                                        HttpServletRequest request);

    EventFullDto getPublishedEventById(Long id, HttpServletRequest request);

    EventFullDto getEventById(List<Long> userIds, List<String> states, List<Long> categories, String rangeStart,
                              String rangeEnd, Long from, Long size, HttpServletRequest request);
}
