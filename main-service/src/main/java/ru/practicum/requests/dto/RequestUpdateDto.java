package ru.practicum.requests.dto;

import lombok.Data;

import java.util.List;

@Data
public class RequestUpdateDto {
    private List<Long> requestIds;
    private String status;
}
