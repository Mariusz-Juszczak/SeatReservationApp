package com.seatreservation.domain;

import com.seatreservation.domain.enumeration.NotificationPriority;
import com.seatreservation.domain.enumeration.NotificationState;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A NotificationAlert.
 */
@Entity
@Table(name = "notification_alert")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class NotificationAlert implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "message")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_priority")
    private NotificationPriority notificationPriority;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_state")
    private NotificationState notificationState;

    @CreationTimestamp
    @Column(name = "created_at")
    private ZonedDateTime createdAt;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name = "user_Id")
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public NotificationAlert id(Long id) {
        this.setId(id);
        return this;
    }

    public NotificationAlert user(User user) {
        this.user = user;
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public NotificationAlert title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return this.message;
    }

    public NotificationAlert message(String message) {
        this.setMessage(message);
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationPriority getNotificationPriority() {
        return this.notificationPriority;
    }

    public NotificationAlert notificationPriority(NotificationPriority notificationPriority) {
        this.setNotificationPriority(notificationPriority);
        return this;
    }

    public void setNotificationPriority(NotificationPriority notificationPriority) {
        this.notificationPriority = notificationPriority;
    }

    public NotificationState getNotificationState() {
        return this.notificationState;
    }

    public NotificationAlert notificationState(NotificationState notificationState) {
        this.setNotificationState(notificationState);
        return this;
    }

    public void setNotificationState(NotificationState notificationState) {
        this.notificationState = notificationState;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public NotificationAlert createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }


    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificationAlert)) {
            return false;
        }
        return id != null && id.equals(((NotificationAlert) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationAlert{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", message='" + getMessage() + "'" +
            ", notificationPriority='" + getNotificationPriority() + "'" +
            ", notificationState='" + getNotificationState() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
