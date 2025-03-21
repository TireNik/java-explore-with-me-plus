package ru.practicum.events.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.practicum.events.dto.LocationDto;
import ru.practicum.events.model.Location;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LocationMapper {

    Location toEntity(LocationDto locationDto);

    LocationDto toLocationDto(Location location);
}