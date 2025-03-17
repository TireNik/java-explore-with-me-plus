package ru.practicum;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Data
public class StatDto {
    @NotBlank
    String app;
    @NotBlank
    String uri;
    @NotBlank
    String ip;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime timeStamp;
}