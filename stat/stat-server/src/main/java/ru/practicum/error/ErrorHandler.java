package ru.practicum.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.error.exceptions.ValidationException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleException(Exception e) {
        log.error("500 {}", e.getMessage(), e);
        return new ApiError("INTERNAL_SERVER_ERROR", "Ошибка сервера", e.getMessage(), Collections.singletonList(e.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(ValidationException e) {
        log.warn("400 {}", e.getMessage(), e);
        return new ApiError(
                "BAD_REQUEST",
                "Некорректный запрос",
                e.getMessage(),
                Collections.singletonList(e.getMessage())
        );
    }
}
