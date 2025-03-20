package ru.practicum.util;

@SuppressWarnings("squid:S1075")
public class PathConstants {
    private PathConstants() {
        throw new AssertionError("Utility class should not be instantiated");
    }

    // Базовые пути для контроллеров
    public static final String ADMIN_USERS = "/admin/users";
    public static final String ADMIN_CATEGORIES = "/admin/categories";
    public static final String ADMIN_EVENTS = "/admin/events";
    public static final String USER_EVENTS = "/users/{userId}/events";
    public static final String USER_REQUESTS = "/users/{userId}/requests";
    public static final String EVENTS = "/events";
    public static final String PUBLIC_CATEGORIES = "/categories";
    public static final String COMPILATIONS = "/compilations";

    // Константы для методов (добавляются к базовому пути)
    // Для UserControllerAdmin (/admin/users)
    public static final String USER_ID = "/{userId}";

    // Для CategoryControllerAdmin (/admin/categories)
    public static final String CATEGORY_ID = "/{catId}";

    // Для EventControllerAdmin (/admin/events)
    public static final String EVENT_ID = "/{eventId}";
    public static final String EVENT_COMMENTS = "/{eventId}/comments";

    // Для UserEventController (/users/{userId}/events)
    public static final String USER_EVENT_ID = "/{eventId}";
    public static final String USER_EVENT_REQUESTS = "/{eventId}/requests";

    // Для UserRequestController (/users/{userId}/requests)
    public static final String REQUEST_ID_CANCEL = "/{requestId}/cancel";

    // Для EventController (/events)
    public static final String PUBLIC_EVENT_ID = "/{eventId}";
    public static final String PUBLIC_EVENT_REQUESTS = "/{eventId}/requests";
    public static final String PUBLIC_EVENT_COMMENTS = "/{eventId}/comments";
    public static final String PUBLIC_COMMENT_ID = "/{eventId}/comments/{commentId}";

    // Для CategoryController (/categories)
    public static final String PUBLIC_CATEGORY_ID = "/{catId}";

    // Для CompilationController (/compilations)
    public static final String COMPILATION_ID = "/{compId}";

    // Отдельные пути (без базового контроллера)
    public static final String ADMIN_COMMENTS_BY_ID = "/admin/comments/{commentId}";
}