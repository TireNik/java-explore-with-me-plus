package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StatDto;
import ru.practicum.ViewStats;
import ru.practicum.mapper.StatMapper;
import ru.practicum.model.Stat;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;
    private final StatMapper statsMapper;

    @Transactional
    @Override
    public StatDto createHit(StatDto dto) {
        Stat stat = statsMapper.toEntity(dto);
        Stat savedStat = statsRepository.save(stat);
        return statsMapper.toDto(savedStat);
    }

    @Override
    public List<ViewStats> getAllStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        List<Object[]> stats;
        if (uris != null && !uris.isEmpty()) {
            if (unique) {
                stats = statsRepository.findUniqueStatsByUris(start, end, uris);
            } else {
                stats = statsRepository.findStatsByUris(start, end, uris);
            }
        } else {
            if (unique) {
                stats = statsRepository.findUniqueStats(start, end);
            } else {
                stats = statsRepository.findStats(start, end);
            }
        }

        return stats.stream()
                .map(obj -> new ViewStats(
                        (String) obj[0],       // app
                        (String) obj[1],       // uri
                        ((Number) obj[2]).longValue() // hits
                ))
                .collect(Collectors.toList());
    }
}