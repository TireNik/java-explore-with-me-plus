package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.practicum.StatDto;
import ru.practicum.model.Stat;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface EndpointHitMapper {
    Stat toEntity(StatDto endpointHitDto);

    StatDto toEndpointHitDto(Stat endpointHit);
}