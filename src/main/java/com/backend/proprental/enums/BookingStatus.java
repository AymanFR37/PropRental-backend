package com.backend.proprental.enums;

import lombok.Getter;

@Getter
public enum BookingStatus {
    PENDING(1, "Pending"),
    CONFIRMED(2, "Confirmed"),
    CANCELLED(3, "Cancelled"),
    COMPLETED(4, "Completed");

    private final Integer value;
    private final String label;

    BookingStatus(Integer value, String label) {
        this.value = value;
        this.label = label;
    }
}
