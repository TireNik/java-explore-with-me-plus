package ru.practicum.compilation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.mapper.EventMapper;
import ru.practicum.events.model.Event;
import ru.practicum.compilation.dto.CompilationDtoRequest;
import ru.practicum.compilation.dto.CompilationDtoResponse;
import ru.practicum.compilation.model.Compilation;

@Mapper(componentModel = "spring", uses = {EventMapper.class})
public interface CompilationMapper {

    @Mapping(target = "events", source = "eventDTOs")
    CompilationDtoResponse toCompilationDto(Compilation compilation, List<EventShortDto> eventDTOs);

    @Mapping(target = "events", source = "events")
    Compilation toCompilation(CompilationDtoRequest compilationDtoRequest, Set<Event> events);
}