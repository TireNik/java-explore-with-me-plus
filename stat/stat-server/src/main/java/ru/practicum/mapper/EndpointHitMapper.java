package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.practicum.EndpointHitDto;
import ru.practicum.model.EndpointHit;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface EndpointHitMapper {
    EndpointHit toEntity(EndpointHitDto endpointHitDto);

    EndpointHitDto toEndpointHitDto(EndpointHit endpointHit);
}