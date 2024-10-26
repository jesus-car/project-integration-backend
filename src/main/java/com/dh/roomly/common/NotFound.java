package com.dh.roomly.common;

public enum NotFound {
    NOT_FOUND_PRODUCT("Property not found."),
    NOT_FOUND_USER("User not found."),
    NOT_FOUND_BOOKING("Booking not found.");


    private final String notFoundMessage;

    NotFound(final String notFoundMessage) {
        this.notFoundMessage = notFoundMessage;
    }

    @Override
    public String toString() {
        return this.notFoundMessage;
    }
}