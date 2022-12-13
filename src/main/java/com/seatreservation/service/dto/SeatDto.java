package com.seatreservation.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.seatreservation.domain.enumeration.AvailabilityStatus;
import com.seatreservation.domain.enumeration.SeatStatus;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.constraints.*;

public class SeatDto implements Serializable {

    private Long id;

    @NotNull
    private Integer seatNumber;

    @NotBlank
    private String name;
    private AvailabilityStatus availabilityStatus;

    @NotNull
    private SeatStatus status;

    private CoordinatesDto coordinates;

    private DimensionsDto dimensions;

    private RoomDto room;
    private List<EquipmentDto> equipments;

    public List<EquipmentDto> getEquipments() {
        return equipments;
    }

    public void setEquipments(List<EquipmentDto> equipments) {
        this.equipments = equipments;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(Integer seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AvailabilityStatus getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(AvailabilityStatus availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public CoordinatesDto getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(CoordinatesDto coordinates) {
        this.coordinates = coordinates;
    }

    public DimensionsDto getDimensions() {
        return dimensions;
    }

    public void setDimensions(DimensionsDto dimensions) {
        this.dimensions = dimensions;
    }

    public RoomDto getRoom() {
        return room;
    }

    public void setRoom(RoomDto room) {
        this.room = room;
    }

    public SeatStatus getStatus() {
        return status;
    }

    public void setStatus(SeatStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SeatDto)) {
            return false;
        }

        SeatDto seatDto = (SeatDto) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, seatDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return "SeatDto{" +
            "id=" + getId() +
            ", seatNumber=" + getSeatNumber() +
            ", name='" + getName() + "'" +
            ", status='" + getAvailabilityStatus() + "'" +
            ", coordinates=" + getCoordinates() +
            ", dimensions=" + getDimensions() +
            ", room=" + getRoom() +
            "}";
    }

    @JsonIgnore
    public String getLocationNameToCompare() {
        return Optional.ofNullable(room)
            .map(RoomDto::getFloor)
            .map(FloorDto::getBuilding)
            .map(BuildingDto::getLocation)
            .map(LocationDto::getName)
            .orElse("");
    }

    @JsonIgnore
    public Long getBuildingIdToCompare() {
        return Optional.ofNullable(room)
            .map(RoomDto::getFloor)
            .map(FloorDto::getBuilding)
            .map(BuildingDto::getId)
            .orElse(-1L);
    }
    @JsonIgnore
    public int getFloorNumberToCompare() {
        return Optional.ofNullable(room)
            .map(RoomDto::getFloor)
            .map(FloorDto::getNumber)
            .orElse(-1);
    }

    @JsonIgnore
    public Long getFloorIdToCompare(){
        return Optional.ofNullable(room)
            .map(RoomDto::getFloor)
            .map(FloorDto::getId)
            .orElse(-1L);
    }
}
