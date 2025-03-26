package ru.practicum.subscriptions.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.subscriptions.dto.SubscriptionDto;
import ru.practicum.subscriptions.service.SubscriptionService;

import java.util.List;

import static ru.practicum.util.PathConstants.PATH_TO_SUBSCRIPTION;
import static ru.practicum.util.PathConstants.PATH_TO_USER;
import static ru.practicum.util.PathConstants.SUBSCRIBERS;
import static ru.practicum.util.PathConstants.SUBSCRIPTIONS;

@Slf4j
@RestController
@RequestMapping(PATH_TO_USER)
@RequiredArgsConstructor
public class SubscriptionControllerPrivate {
    private final SubscriptionService subscriptionService;

    @PostMapping(PATH_TO_SUBSCRIPTION)
    @ResponseStatus(HttpStatus.CREATED)
    public SubscriptionDto subscribe(@PathVariable Long userId, @PathVariable Long subscribedToId) {
        log.info("Эндпоинт /subscriptions/{subscribedToId}. POST запрос от пользователя с id {}" +
                        " на подписку на пользователя с id {}.",
                userId, subscribedToId);
        return subscriptionService.subscribe(userId, subscribedToId);
    }

    @DeleteMapping(PATH_TO_SUBSCRIPTION)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unsubscribe(@PathVariable Long userId, @PathVariable Long subscribedToId) {
        log.info("Эндпоинт /subscriptions/{subscribedToId}. DELETE запрос от пользователя с id {}" +
                        " на отписку от пользователя с id {}.",
                userId, subscribedToId);
        subscriptionService.unsubscribe(userId, subscribedToId);
    }

    @GetMapping(SUBSCRIPTIONS)
    public List<SubscriptionDto> getSubscriptions(@PathVariable Long userId) {
        log.info("Эндпоинт /subscriptions. GET запрос всех подписок пользователя с id {}", userId);
        return subscriptionService.getSubscriptions(userId);
    }

    @GetMapping(SUBSCRIBERS)
    public List<SubscriptionDto> getSubscribers(@PathVariable Long userId) {
        log.info("Эндпоинт /subscribers. GET запрос всех подписчиков пользователя с id {}", userId);
        return subscriptionService.getSubscribers(userId);
    }
}
