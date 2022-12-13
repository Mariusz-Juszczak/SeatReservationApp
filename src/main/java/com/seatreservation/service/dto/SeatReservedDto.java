package com.seatreservation.service.dto;

import com.seatreservation.domain.enumeration.ReservationStatus;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;


public class SeatReservedDto implements Serializable {

    private Long id;

    private ZonedDateTime fromDate;

    private ZonedDateTime toDate;

    private ZonedDateTime createdDate;

    private ReservationStatus reservationStatus;

    private UserDto user;

    private Long seatId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(ZonedDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public ZonedDateTime getToDate() {
        return toDate;
    }

    public void setToDate(ZonedDateTime toDate) {
        this.toDate = toDate;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ReservationStatus getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public Long getSeatId() {
        return seatId;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SeatReservedDto)) {
            return false;
        }

        SeatReservedDto seatReservedDto = (SeatReservedDto) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, seatReservedDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return "SeatReservedDto{" +
            "id=" + id +
            ", fromDate=" + fromDate +
            ", toDate=" + toDate +
            ", createdDate=" + createdDate +
            ", reservationStatus=" + reservationStatus +
            ", user=" + user +
            ", seatId=" + seatId +
            '}';
    }
}
