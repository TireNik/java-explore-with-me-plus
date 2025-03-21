package ru.practicum.util;

@SuppressWarnings("squid:S1075")
public class PathConstants {
    private PathConstants() {
        throw new AssertionError("Utility class should not be instantiated");
    }

    public static final String CATEGORIES = "/categories";
    public static final String COMPILATIONS = "/compilations";
    public static final String EVENTS = "/events";
    public static final String USERS = "/users";

    public static final String CATEGORY_ID = "/{catId}";
    public static final String COMPILATION_ID = "/{compId}";
    public static final String EVENT_ID = "/{eventId}";
    public static final String USER_ID = "/{userId}";

    public static final String ADMIN_CATEGORIES = "/admin/categories";
    public static final String ADMIN_COMPILATIONS = "/admin/compilations";
    public static final String ADMIN_COMPILATION_BY_ID = "/admin/compilations" + COMPILATION_ID;
    public static final String ADMIN_EVENTS = "/admin/events";
    public static final String ADMIN_USERS = "/admin/users";

    public static final String EVENT_PUBLISH = "/{eventId}/publish";
    public static final String COMPILATION_BY_ID = COMPILATIONS + COMPILATION_ID;
    public static final String PRIVATE_EVENTS = "/{userId}/events";
    public static final String PRIVATE_EVENT_BY_ID = "/{userId}/events" + EVENT_ID;
    public static final String PRIVATE_REQUESTS = "/{userId}/requests";
    public static final String EVENT_REQUESTS = "/{eventId}/requests";
    public static final String PRIVATE_EVENT_REQUESTS = PRIVATE_EVENTS + EVENT_REQUESTS;
    public static final String PRIVATE_REQUEST_CANCEL = PRIVATE_REQUESTS + "/{requestId}/cancel";
}