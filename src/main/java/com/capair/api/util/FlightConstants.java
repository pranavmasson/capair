package com.capair.api.util;

public enum FlightConstants {
    ECONOMY("Economy"),
    PREMIUM("Premium"),
    BUSINESS("Business"),
    FIRST_CLASS("First Class");

    private final String value;

    private FlightConstants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
