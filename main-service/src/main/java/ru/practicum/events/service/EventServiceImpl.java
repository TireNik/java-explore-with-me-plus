package ru.practicum.events.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.dto.UpdateEventAdminRequestDto;
import ru.practicum.events.dto.UpdateEventUserRequestDto;
import ru.practicum.events.model.EventState;

import java.util.List;

@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService {
    @Override
    public List<EventFullDto> adminEventsSearch(List<Long> users, List<Long> categories, List<EventState> states, String rangeStart, String rangeEnd, int from, int size) {
        return List.of();
    }

    @Override
    public EventFullDto adminEventUpdate(Long eventId, UpdateEventAdminRequestDto eventDto) {
        return null;
    }

    @Override
    public List<EventFullDto> privateUserEvents(Long userId, int from, int size) {
        return List.of();
    }

    @Override
    public EventFullDto privateEventCreate(Long userId, NewEventDto eventCreateDto) {
        return null;
    }

    @Override
    public EventFullDto privateGetUserEvent(Long userId, Long eventId) {
        return null;
    }

    @Override
    public EventFullDto privateUpdateUserEvent(Long userId, Long eventId, UpdateEventUserRequestDto eventUpdateDto) {
        return null;
    }
}
