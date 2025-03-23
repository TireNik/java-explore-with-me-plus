package ru.practicum.events.service;

import jakarta.persistence.criteria.Predicate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StatDto;
import ru.practicum.ViewStats;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.error.exception.*;
import ru.practicum.events.dto.*;
import ru.practicum.events.mapper.EventMapper;
import ru.practicum.events.mapper.LocationMapper;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.EventSort;
import ru.practicum.events.model.EventState;
import ru.practicum.events.model.Location;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.events.repository.LocationRepository;
import ru.practicum.requests.model.RequestStatus;
import ru.practicum.requests.repository.RequestRepository;
import ru.practicum.stats.client.StatClient;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Slf4j
public class EventServiceImpl implements EventService {

    private final RequestRepository requestRepository;

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final StatClient statClient;
    private final CategoryRepository categoryRepository;
    private final LocationMapper locationMapper;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

    @Transactional
    @Override
    public EventFullDto getEventById(Long id, HttpServletRequest request) {
        Event event = eventRepository.findById(id)
                .filter(e -> e.getState() == EventState.PUBLISHED)
                .orElseThrow(() -> new NotFoundException("Событие с id=" + id + " не найдено"));

        StatDto statDto = new StatDto(
                null,
                "main-service",
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now().format(FORMATTER)
        );
        log.info("Статистика: {}", statDto);
        statClient.hit(statDto);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Поток был прерван во время ожидания", e);
        }

        List<ViewStats> stats = statClient.getStat(
                event.getPublishedOn(),
                LocalDateTime.now(),
                List.of("/events/" + id),
                true
        );

        long views = stats.isEmpty() ? 0 : stats.getFirst().getHits();
        long confirmedRequests = requestRepository.countByEventIdAndStatus(id, RequestStatus.CONFIRMED);

        EventFullDto eventFullDto = eventMapper.toEventFullDto(event);
        eventFullDto.setViews(views);
        eventFullDto.setConfirmedRequests((int) confirmedRequests);
        eventRepository.save(event);

        return eventFullDto;
    }

    @Override
    public List<EventShortDto> getPublicEvents(String text, List<Long> categories, Boolean paid,
                                               LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                               Boolean onlyAvailable, String sort, int from, int size,
                                               HttpServletRequest request) {
        Specification<Event> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (text != null && !text.isBlank()) {
                String pattern = "%%" + text.toLowerCase() + "%%";
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("annotation")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), pattern)
                ));
            }

            if (categories != null && !categories.isEmpty()) {
                predicates.add(root.get("category").get("id").in(categories));
            }

            if (paid != null) {
                predicates.add(criteriaBuilder.equal(root.get("paid"), paid));
            }

            if (rangeStart != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), rangeStart));
            }

            if (rangeEnd != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"), rangeEnd));
            }

            if (onlyAvailable != null && onlyAvailable) {
                predicates.add(criteriaBuilder.greaterThan(root.get("participantLimit"), 0));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        EventSort eventSort = sort != null ? EventSort.valueOf(sort.toUpperCase()) : null;
        Sort sorting = Sort.unsorted();
        if (eventSort != null) {
            switch (eventSort) {
                case EVENT_DATE -> sorting = Sort.by(Sort.Direction.DESC, "eventDate");
                case VIEWS -> sorting = Sort.by(Sort.Direction.DESC, "views");
            }
        }

        Pageable pageable = PageRequest.of(from / size, size, sorting);
        List<Event> events = eventRepository.findAll(spec, pageable).getContent();

        return events.stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public List<EventFullDto> getAdminEventById(List<Long> userIds, List<String> states, List<Long> categories,
                                                String rangeStart, String rangeEnd, Long from, Long size) {
        Specification<Event> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (userIds != null && !userIds.isEmpty()) {
                predicates.add(root.get("user").get("id").in(userIds));
            }
            if (states != null && !states.isEmpty()) {
                List<EventState> eventStates = states.stream()
                        .map(EventState::valueOf)
                        .collect(Collectors.toList());
                predicates.add(root.get("state").in(eventStates));
            }
            if (categories != null && !categories.isEmpty()) {
                predicates.add(root.get("category").get("id").in(categories));
            }

            if (rangeStart != null && !rangeStart.isBlank()) {
                LocalDateTime startDate = LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), startDate));
            }

            if (rangeEnd != null && !rangeEnd.isBlank()) {
                LocalDateTime endDate = LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"), endDate));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Pageable pageable = PageRequest.of(from.intValue() / size.intValue(), size.intValue());

        List<Event> events = eventRepository.findAll(spec, pageable).getContent();

        return events.stream()
                .map(eventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto updateEventAdmin(Long eventId, UpdateEventAdminRequestDto dto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));

        // Форматтер для даты "yyyy-MM-dd HH:mm:ss"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if (dto.getEventDate() != null) {
            LocalDateTime eventDateTime = LocalDateTime.parse(dto.getEventDate(), formatter);
            if (event.getState() == EventState.PUBLISHED && eventDateTime.isBefore(event.getPublishedOn().plusHours(1))) {
                throw new ConflictException("Дата начала события должна быть не ранее чем за час от даты публикации");
            }
            event.setEventDate(eventDateTime);
        }

        if (dto.getAnnotation() != null) event.setAnnotation(dto.getAnnotation());
        if (dto.getTitle() != null) event.setTitle(dto.getTitle());
        if (dto.getDescription() != null) event.setDescription(dto.getDescription());
        if (dto.getCategory() != null) {
            Category category = categoryRepository.findById(dto.getCategory())
                    .orElseThrow(() -> new NotFoundException("Категория с id=" + dto.getCategory() + " не найдена"));
            event.setCategory(category);
        }
        if (dto.getLocation() != null) {
            Location location = locationRepository.save(locationMapper.toEntity(dto.getLocation()));
            event.setLocation(location);
        }
        if (dto.getPaid() != null) event.setPaid(dto.getPaid());
        if (dto.getParticipantLimit() != null) event.setParticipantLimit(dto.getParticipantLimit());
        if (dto.getRequestModeration() != null) event.setRequestModeration(dto.getRequestModeration());

        if (dto.getStateAction() != null) {
            switch (dto.getStateAction()) {
                case PUBLISH_EVENT:
                    if (!event.getState().equals(EventState.PENDING)) {
                        throw new ConflictException("Cannot publish the event because it's not in the right state: " + event.getState());
                    }
                    event.setState(EventState.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                    break;

                case REJECT_EVENT:
                    if (event.getState().equals(EventState.PUBLISHED)) {
                        throw new ConflictException("Невозможно отклонить событие, потому что оно уже опубликовано");
                    }
                    event.setState(EventState.CANCELED);
                    break;
            }
        }

        Event updatedEvent = eventRepository.save(event);
        return eventMapper.toEventFullDto(updatedEvent);
    }

    public EventFullDto privateGetUserEvent(Long userId, Long eventId, HttpServletRequest request) {
        log.info("userId: {}", userId);
        try {
            statClient.hit(new StatDto(
                    null,
                    "event-service",
                    request.getRequestURI(),
                    request.getRemoteAddr(),
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            ));

            if (!userRepository.existsById(userId)) {
                throw new NotFoundException("User with id=" + userId + " was not found");
            }

            Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                    .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " не был найден"));

            return eventMapper.toEventFullDto(event);
        } catch (Exception e) {
            log.error("Error occurred while retrieving event for userId: {} and eventId: {}", userId, eventId, e);
            throw e;
        }
    }

    @Override
    @Transactional
    public EventFullDto addEvent(Long userId, NewEventDto newEventDto) {
        User initiator = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));

        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new NotFoundException("Category with id=" + newEventDto.getCategory() + " was not found"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime eventDateTime = LocalDateTime.parse(newEventDto.getEventDate(), formatter);
        if (eventDateTime.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ConflictException("Дата и время события не может быть раньше, чем через два часа от текущего момента");
        }

        Location location = locationRepository.save(locationMapper.toEntity(newEventDto.getLocation()));

        Event event = new Event();
        event.setAnnotation(newEventDto.getAnnotation());
        event.setTitle(newEventDto.getTitle());
        event.setDescription(newEventDto.getDescription());
        event.setCategory(category);
        event.setEventDate(eventDateTime);
        event.setLocation(location);
        event.setInitiator(initiator);
        event.setCreatedOn(LocalDateTime.now());
        event.setState(EventState.PENDING);

        event.setPaid(newEventDto.getPaid() != null ? newEventDto.getPaid() : false);
        event.setParticipantLimit(newEventDto.getParticipantLimit() != null ? newEventDto.getParticipantLimit() : 0);
        event.setRequestModeration(newEventDto.getRequestModeration() != null ? newEventDto.getRequestModeration() : true);

        Event savedEvent = eventRepository.save(event);
        return eventMapper.toEventFullDto(savedEvent);
    }

    @Override
    public List<EventShortDto> getEvents(Long userId, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<Event> events = eventRepository.findByInitiatorId(userId, pageable);
        return events.stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequestDto updateRequest) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));

        if (!event.getInitiator().getId().equals(userId)) {
            throw new NotFoundException("Пользователь с id=" + userId + " не является владельцем события с id=" + eventId);
        }

        if (event.getState() != EventState.PENDING && event.getState() != EventState.CANCELED) {
            throw new ConflictException("Only pending or canceled events can be changed");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if (updateRequest.getEventDate() != null) {
            LocalDateTime eventDateTime = LocalDateTime.parse(updateRequest.getEventDate(), formatter);
            if (eventDateTime.isBefore(LocalDateTime.now().plusHours(2))) {
                throw new ConflictException("Дата и время события не может быть раньше, чем через два часа от текущего момента");
            }
            event.setEventDate(eventDateTime);
        }

        if (updateRequest.getAnnotation() != null) event.setAnnotation(updateRequest.getAnnotation());
        if (updateRequest.getTitle() != null) event.setTitle(updateRequest.getTitle());
        if (updateRequest.getDescription() != null) event.setDescription(updateRequest.getDescription());
        if (updateRequest.getCategory() != null) {
            Category category = categoryRepository.findById(updateRequest.getCategory())
                    .orElseThrow(() -> new NotFoundException("Категория с id=" + updateRequest.getCategory() + " не найдена"));
            event.setCategory(category);
        }
        if (updateRequest.getLocation() != null) {
            Location location = locationRepository.save(locationMapper.toEntity(updateRequest.getLocation()));
            event.setLocation(location);
        }
        if (updateRequest.getPaid() != null) event.setPaid(updateRequest.getPaid());
        if (updateRequest.getParticipantLimit() != null) event.setParticipantLimit(updateRequest.getParticipantLimit());
        if (updateRequest.getRequestModeration() != null) event.setRequestModeration(updateRequest.getRequestModeration());

        if (updateRequest.getStateAction() != null) {
            switch (updateRequest.getStateAction()) {
                case SEND_TO_REVIEW:
                    if (event.getState() == EventState.CANCELED) {
                        event.setState(EventState.PENDING);
                    } else {
                        throw new ConflictException("Событие уже находится в состоянии ожидания модерации");
                    }
                    break;
                case CANCEL_REVIEW:
                    if (event.getState() == EventState.PENDING) {
                        event.setState(EventState.CANCELED);
                    } else {
                        throw new ConflictException("Событие уже находится в состоянии отмены");
                    }
                    break;
            }
        }

        Event updatedEvent = eventRepository.save(event);
        return eventMapper.toEventFullDto(updatedEvent);
    }

}
