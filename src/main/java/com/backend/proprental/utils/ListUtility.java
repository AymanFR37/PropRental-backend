package com.backend.proprental.utils;

import java.util.Collection;

public class ListUtility {
    private ListUtility() {
    }

    public static boolean isNotEmpty(Collection<?> list) {
        return list != null && !list.isEmpty();
    }

    public static boolean isEmpty(Collection<?> list) {
        return list == null || list.isEmpty();
    }
}
