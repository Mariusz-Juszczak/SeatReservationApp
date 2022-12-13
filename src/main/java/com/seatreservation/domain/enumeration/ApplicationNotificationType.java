package com.seatreservation.domain.enumeration;

public enum ApplicationNotificationType {

    RESERVATION_HAS_BEEN_CANCELLED(
        "Reservation has been cancelled",
        "Reservation has been cancelled. Please contact us if something went wrong. Name of reservation:",
        NotificationPriority.HIGH),

    RESERVATION_HAS_BEEN_CREATED(
        "Reservation has been created",
        "Reservation has been created. If you change your mind, please remember to cancel the reservation. Name of reservation:",
        NotificationPriority.LOW),

    RESERVATION_HAS_BEEN_APPROVED(
        "Reservation has been approved",
        "Reservation has been approved. Remember to be on time at the office. See you! Name of reservation:",
        NotificationPriority.MEDIUM),

    NEW_RESTRICTION_HAS_BEEN_CREATED(
        "New restriction is coming",
        "There is a new restriction in the office. Restriction name: ",
        NotificationPriority.HIGH);

    private final String title;
    private final String message;
    private final NotificationPriority priority;

    ApplicationNotificationType(String title, String message, NotificationPriority priority) {
        this.title = title;
        this.message = message;
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }
    public NotificationPriority getPriority() {
        return priority;
    }


}
