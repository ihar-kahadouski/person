package com.khdsk.boost.person.api.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Messages {

    public static final String SUCCESSFUL_DELETE_MSG = "Person with id '%s' was deleted successfully";
    public static final String NOT_FOUND_MSG = "Person with id '%s' is not found";

    public static String buildMessage(String pattern, Long id) {
        return String.format(pattern, id);
    }
}
