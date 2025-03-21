package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationDtoRequest;
import ru.practicum.compilation.dto.CompilationDtoResponse;

import java.util.List;

public interface CompilationService {
    CompilationDtoResponse createCompilationAdmin(CompilationDtoRequest compilationDtoRequest);

    CompilationDtoResponse updateCompilationAdmin(CompilationDtoRequest compilationDtoRequest, Long compId);

    void deleteCompilationAdmin(Long compId);

    CompilationDtoResponse getCompilationByIdPublic(Long compId);

    List<CompilationDtoResponse> getAllCompilationsPublic(Boolean pinned, Integer from, Integer size);
}