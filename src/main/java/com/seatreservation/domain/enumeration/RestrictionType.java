package com.seatreservation.domain.enumeration;

/**
 * The RestrictionType enumeration.
 */
public enum RestrictionType {

    PERCENTAGE("Allowed ${value}% of seats"),
    PER_SEATS_NUMBER("Allowed every ${value} places");

    private static final String VALUE_PARAMETER_KEY = "${value}";
    private final String messagePattern;

    RestrictionType(String messagePattern) {
        this.messagePattern = messagePattern;
    }

    public String getMessage(Integer value) {
        return messagePattern.replace(VALUE_PARAMETER_KEY, value.toString());
    }
}
