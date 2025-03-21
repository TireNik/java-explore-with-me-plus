package ru.practicum.requests.dto;

import lombok.Data;

import java.util.List;

@Data
public class RequestUpdateResultDto {
    private List<ParticipationRequestDto> confirmedRequests;
    private List<ParticipationRequestDto> rejectedRequests;
}
