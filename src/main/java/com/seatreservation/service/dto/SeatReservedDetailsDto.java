package com.seatreservation.service.dto;

import com.seatreservation.domain.enumeration.ReservationStatus;


import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;


public class SeatReservedDetailsDto implements Serializable {
    private Long id;
    private ZonedDateTime fromDate;
    private ZonedDateTime toDate;
    private ZonedDateTime createdDate;
    private ReservationStatus reservationStatus;
    private Long locationId;
    private Long buildingId;
    private Long floorId;
    private Long roomId;
    private Long seatId;
    private String locationName;

    private String buildingName;

    private String floorName;

    private String roomName;

    private String seatName;

    private String buildingAddress;

    private List<EquipmentDto> equipments;

    public List<EquipmentDto> getEquipments() {
        return equipments;
    }

    public void setEquipments(List<EquipmentDto> equipments) {
        this.equipments = equipments;
    }

    private UserDto user;



    public String getBuildingAddress() {
        return buildingAddress;
    }

    public void setBuildingAddress(String buildingAddress) {
        this.buildingAddress = buildingAddress;
    }

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

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    public Long getFloorId() {
        return floorId;
    }

    public void setFloorId(Long floorId) {
        this.floorId = floorId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getSeatId() {
        return seatId;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getSeatName() {
        return seatName;
    }

    public void setSeatName(String seatName) {
        this.seatName = seatName;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SeatReservedDetailsDto)) {
            return false;
        }

        SeatReservedDetailsDto seatReservedDetailsDto = (SeatReservedDetailsDto) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, seatReservedDetailsDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SeatReservedDetailsDto{" +
            "id=" + getId() +
            ", location='" + getLocationName() + "'" +
            ", building='" + getBuildingName() + "'" +
            ", floor='" + getFloorName() + "'" +
            ", room='" + getRoomName() + "'" +
            ", seat='" + getSeatName() + "'" +
            ", user='" + getUser() + "'" +
            ", equipment='" + getEquipments() + "'" +
            "}";
    }
}

