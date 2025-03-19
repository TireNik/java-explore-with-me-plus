package ru.practicum.events.service;

import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.dto.UpdateEventAdminRequestDto;
import ru.practicum.events.dto.UpdateEventUserRequestDto;
import ru.practicum.events.model.EventState;

import java.util.List;

public interface EventService {
    List<EventFullDto> adminEventsSearch(List<Long> users, List<Long> categories, List<EventState> states,
                                         String rangeStart, String rangeEnd, int from, int size);

    EventFullDto adminEventUpdate(Long eventId, UpdateEventAdminRequestDto eventDto);

    List<EventFullDto> privateUserEvents(Long userId, int from, int size);

    EventFullDto privateEventCreate(Long userId, NewEventDto eventCreateDto);

    EventFullDto privateGetUserEvent(Long userId, Long eventId);

    EventFullDto privateUpdateUserEvent(Long userId, Long eventId, UpdateEventUserRequestDto eventUpdateDto);

}
