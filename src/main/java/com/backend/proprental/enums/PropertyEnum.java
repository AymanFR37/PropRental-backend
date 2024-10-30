package com.backend.proprental.enums;

import lombok.Getter;

public class PropertyEnum {

    @Getter
    public enum Type {
        APARTMENT(1, "Apartment"),
        HOUSE(2, "House"),
        VILLA(3, "Villa"),
        ROOM(4, "Room"),
        OTHER(5, "Other");

        private final String description;
        private final int value;

        Type(int value, String description) {
            this.value = value;
            this.description = description;
        }
    }

    @Getter
    public enum Status {
        AVAILABLE(1, "Available"),
        BOOKED(2, "Booked"),
        DEACTIVATED(3, "Deactivated");

        private final String description;
        private final int value;

        Status(int value, String description) {
            this.value = value;
            this.description = description;
        }
    }

    @Getter
    public enum Amenity {
        WIFI(1, "Wifi"),
        KITCHEN(2, "Kitchen"),
        GARAGE(3, "Garage"),
        PARKING(4, "Parking"),
        POOL(5, "Pool"),
        GYM(6, "Gym"),
        LAUNDRY(7, "Laundry"),
        RECEPTION(8, "Reception"),
        SECURITY(9, "Security");

        private final String description;
        private final int value;

        Amenity(int value, String description) {
            this.value = value;
            this.description = description;
        }
    }
}
