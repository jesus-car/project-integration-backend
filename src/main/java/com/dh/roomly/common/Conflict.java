package com.dh.roomly.common;

public enum Conflict {
    GENERIC_PRODUCT_CONFLICT("There is a conflict with the product."),
    GENERIC_USER_CONFLICT("There is a conflict with the user."),
    GENERIC_BOOKING_CONFLICT("There is a conflict with the booking.");

    private final String conflictMessage;

    Conflict(final String conflictMessage) {
        this.conflictMessage = conflictMessage;
    }

    @Override
    public String toString() {
        return this.conflictMessage;
    }
}