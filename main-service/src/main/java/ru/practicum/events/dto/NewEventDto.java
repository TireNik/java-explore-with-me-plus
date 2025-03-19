package ru.practicum.events.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Value;
import ru.practicum.events.model.Event;

/**
 * DTO for {@link Event}
 */
@Data
public class NewEventDto {
    @NotNull
    @Size(min = 20, max = 2000)
    @NotEmpty
    @NotBlank
    String annotation;
    @NotNull
    @Size(min = 3, max = 120)
    @NotEmpty
    @NotBlank
    String title;
    @NotNull
    String eventDate;
    @NotNull
    @Size(min = 20, max = 7000)
    @NotEmpty
    @NotBlank
    String description;
    @NotNull
    Long category;
    @NotNull
    LocationDto location;
    @PositiveOrZero
    Integer participantLimit;
    Boolean requestModeration = true;
}