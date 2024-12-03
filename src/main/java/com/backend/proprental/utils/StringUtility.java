package com.backend.proprental.utils;

public class StringUtility {
    private StringUtility() {
    }

    public static boolean isNotEmpty(String value) {
        return value != null && !value.isBlank();
    }

    public static boolean isEmpty(String value) {
        return value == null || value.isBlank();
    }
}
