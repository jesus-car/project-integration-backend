package com.dh.roomly.common;

public enum NotFound {
    NOT_FOUND_PRODUCT("Propiedad no encontrada"),
    NOT_FOUND_USER("Usuario no encontrado"),
    NOT_FOUND_BOOKING("Booking no encontrado");


    private final String notFoundMessage;

    NotFound(final String notFoundMessage) {
        this.notFoundMessage = notFoundMessage;
    }

    @Override
    public String toString() {
        return this.notFoundMessage;
    }
}