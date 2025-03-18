package ru.practicum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Value;

@Value
@Data
public class StatDto {
    Long id;
    @NotBlank
    String app;
    @NotBlank
    String uri;
    @NotBlank
    String ip;
    @NotNull
    String timestamp;
}