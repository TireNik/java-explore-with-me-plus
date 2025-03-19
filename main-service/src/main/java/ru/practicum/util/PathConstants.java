package ru.practicum.util;

@SuppressWarnings("squid:S1075")
public class PathConstants {
    private PathConstants() {
        throw new AssertionError("Utility class should not be instantiated");
    }

    public static final String USERS_PATH = "/admin/users";
    public static final String USER_ID_PATH = "/{user-id}";
}
