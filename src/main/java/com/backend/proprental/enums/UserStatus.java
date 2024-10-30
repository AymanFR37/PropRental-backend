package com.backend.proprental.enums;

import lombok.Getter;

@Getter
public enum UserStatus {
    ACTIVE(1, "Active"),
    SUSPENDED(2, "Suspended"),
    DEACTIVATED(3, "Deactivated");

    private final Integer value;
    private final String label;

    UserStatus(Integer value, String label) {
        this.value = value;
        this.label = label;
    }
}
