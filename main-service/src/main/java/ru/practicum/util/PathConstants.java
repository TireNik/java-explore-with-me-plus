package ru.practicum.util;

@SuppressWarnings("squid:S1075")
public class PathConstants {
    private PathConstants() {
        throw new AssertionError("Utility class should not be instantiated");
    }

    // Базовые пути
    public static final String ADMIN_PATH = "/admin";
    public static final String USERS_PATH = "/users";
    public static final String EVENTS_PATH = "/events";
    public static final String CATEGORIES_PATH = "/categories";
    public static final String COMPILATIONS_PATH = "/compilations";
    public static final String REQUESTS_PATH = "/requests";
    public static final String COMMENTS_PATH = "/comments";

    // Идентификаторы
    public static final String USER_ID = "/{userId}";
    public static final String EVENT_ID = "/{eventId}";
    public static final String CATEGORY_ID = "/{catId}";
    public static final String COMPILATION_ID = "/{compId}";
    public static final String REQUEST_ID = "/{requestId}";
    public static final String COMMENT_ID = "/{commentId}";

    // Комбинированные пути для Admin API
    // "/admin/users"
    public static final String ADMIN_USERS = ADMIN_PATH + USERS_PATH;
    // "/admin/users/{userId}"
    public static final String ADMIN_USERS_BY_ID = ADMIN_USERS + USER_ID;
    // "/admin/categories"
    public static final String ADMIN_CATEGORIES = ADMIN_PATH + CATEGORIES_PATH;
    // "/admin/categories/{catId}"
    public static final String ADMIN_CATEGORIES_BY_ID = ADMIN_CATEGORIES + CATEGORY_ID;
    // "/admin/events"
    public static final String ADMIN_EVENTS = ADMIN_PATH + EVENTS_PATH;
    // "/admin/events/{eventId}"
    public static final String ADMIN_EVENTS_BY_ID = ADMIN_EVENTS + EVENT_ID;
    // "/admin/events/{eventId}/comments"
    public static final String ADMIN_EVENT_COMMENTS = ADMIN_EVENTS_BY_ID + COMMENTS_PATH;
    // "/admin/comments/{commentId}"
    public static final String ADMIN_COMMENTS_BY_ID = ADMIN_PATH + COMMENTS_PATH + COMMENT_ID;

    // Комбинированные пути для Public API
    // "/users/{userId}"
    public static final String PUBLIC_USERS_BY_ID = USERS_PATH + USER_ID;
    // "/users/{userId}/events"
    public static final String USER_EVENTS = PUBLIC_USERS_BY_ID + EVENTS_PATH;
    // "/users/{userId}/events/{eventId}"
    public static final String USER_EVENTS_BY_ID = USER_EVENTS + EVENT_ID;
    // "/users/{userId}/events/{eventId}/requests"
    public static final String USER_EVENT_REQUESTS = USER_EVENTS_BY_ID + REQUESTS_PATH;
    // "/users/{userId}/requests"
    public static final String USER_REQUESTS = PUBLIC_USERS_BY_ID + REQUESTS_PATH;
    // "/users/{userId}/requests/{requestId}/cancel"
    public static final String USER_REQUEST_CANCEL = USER_REQUESTS + REQUEST_ID + "/cancel";
    // "/events"
    public static final String PUBLIC_EVENTS = EVENTS_PATH;
    // "/events/{eventId}"
    public static final String PUBLIC_EVENTS_BY_ID = EVENTS_PATH + EVENT_ID;
    // "/events/{eventId}/requests"
    public static final String PUBLIC_EVENT_REQUESTS = PUBLIC_EVENTS_BY_ID + REQUESTS_PATH;
    // "/categories"
    public static final String PUBLIC_CATEGORIES = CATEGORIES_PATH;
    // "/categories/{catId}"
    public static final String PUBLIC_CATEGORIES_BY_ID = CATEGORIES_PATH + CATEGORY_ID;
    // "/compilations"
    public static final String PUBLIC_COMPILATIONS = COMPILATIONS_PATH;
    // "/compilations/{compId}"
    public static final String PUBLIC_COMPILATIONS_BY_ID = COMPILATIONS_PATH + COMPILATION_ID;
    // "/events/{eventId}/comments"
    public static final String PUBLIC_EVENT_COMMENTS = PUBLIC_EVENTS_BY_ID + COMMENTS_PATH;
    // "/events/{eventId}/comments/{commentId}"
    public static final String PUBLIC_COMMENTS_BY_ID = COMMENTS_PATH + COMMENT_ID;
}
