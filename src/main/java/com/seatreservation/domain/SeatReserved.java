package com.seatreservation.domain;

import com.seatreservation.domain.enumeration.ReservationStatus;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import javax.persistence.*;

@Entity
@Table(name = "seat_reserved")
public class SeatReserved implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "from_date", nullable = false)
    private ZonedDateTime fromDate;

    @Column(name = "to_date")
    private ZonedDateTime toDate;

    @Column(name = "created_date", nullable = false)
    @CreationTimestamp
    private ZonedDateTime createdDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "reservation_status", nullable = false)
    private ReservationStatus reservationStatus;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "SEAT_ID")
    private Seat seat;

    public Long getId() {
        return this.id;
    }

    public SeatReserved id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public SeatReserved name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getFromDate() {
        return this.fromDate;
    }

    public SeatReserved fromDate(ZonedDateTime fromDate) {
        this.setFromDate(fromDate);
        return this;
    }

    public void setFromDate(ZonedDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public ZonedDateTime getToDate() {
        return this.toDate;
    }

    public SeatReserved toDate(ZonedDateTime toDate) {
        this.setToDate(toDate);
        return this;
    }

    public void setToDate(ZonedDateTime toDate) {
        this.toDate = toDate;
    }

    public ZonedDateTime getCreatedDate() {
        return this.createdDate;
    }

    public SeatReserved createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ReservationStatus getReservationStatus() {
        return this.reservationStatus;
    }

    public SeatReserved reservationStatus(ReservationStatus reservationStatus) {
        this.setReservationStatus(reservationStatus);
        return this;
    }

    public void setReservationStatus(ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SeatReserved user(User user) {
        this.setUser(user);
        return this;
    }

    public Seat getSeat() {
        return this.seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public SeatReserved seat(Seat seat) {
        this.setSeat(seat);
        return this;
    }

    public String getReservationName() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
        return " B: " + getBuildingName() + " | F: " + getFloorName() + " | R: " + getRoomName() + " | S: " + seat.getName() + " | from: " + fromDate.format(formatter) + " | to: " + toDate.format(formatter);
    }

    private String getBuildingName() {
        return Optional.ofNullable(seat)
            .map(Seat::getRoom)
            .map(Room::getFloor)
            .map(Floor::getBuilding)
            .map(Building::getName)
            .orElse(" ");
    }

    private String getFloorName() {
        return Optional.ofNullable(seat)
            .map(Seat::getRoom)
            .map(Room::getFloor)
            .map(Floor::getName)
            .orElse(" ");
    }

    private String getRoomName() {
        return Optional.ofNullable(seat)
            .map(Seat::getRoom)
            .map(Room::getName)
            .orElse(" ");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SeatReserved)) {
            return false;
        }
        return id != null && id.equals(((SeatReserved) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "SeatReserved{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", fromDate='" + getFromDate() + "'" +
            ", toDate='" + getToDate() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", reservationStatus='" + getReservationStatus() + "'" +
            "}";
    }
}
