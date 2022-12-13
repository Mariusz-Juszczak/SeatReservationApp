package com.seatreservation.service.dto;

import com.seatreservation.domain.enumeration.NotificationPriority;
import com.seatreservation.domain.enumeration.NotificationState;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

public class NotificationAlertDto implements Serializable {

    private Long id;

    private String title;

    private String message;

    private NotificationPriority notificationPriority;

    private NotificationState notificationState;

    private ZonedDateTime createdAt;

    private UserDto user;

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationPriority getNotificationPriority() {
        return notificationPriority;
    }

    public void setNotificationPriority(NotificationPriority notificationPriority) {
        this.notificationPriority = notificationPriority;
    }

    public NotificationState getNotificationState() {
        return notificationState;
    }

    public void setNotificationState(NotificationState notificationState) {
        this.notificationState = notificationState;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificationAlertDto)) {
            return false;
        }

        NotificationAlertDto notificationAlertDto = (NotificationAlertDto) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, notificationAlertDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationAlertDto{" + "id=" + getId() + ", title='" + getTitle() + "'" + ", message='" + getMessage() + "'" + ", notificationPriority='" + getNotificationPriority() + "'" + ", notificationState='" + getNotificationState() + "'" + ", createdAt='" + getCreatedAt() + "'" + "}";
    }
}
