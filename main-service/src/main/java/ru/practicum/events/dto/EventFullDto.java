package ru.practicum.events.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.EventState;

/**
 * DTO for {@link Event}
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventFullDto {
    Long id;
    String annotation;
    boolean paid;

    String title;
    String eventDate;
    String description;

    boolean requestModeration;
    int participantLimit;
    String publishedOn;

    String createdOn;
    CategoryDto category;
    UserShortDto initiator;

    LocationDto location;
    EventState state;
}