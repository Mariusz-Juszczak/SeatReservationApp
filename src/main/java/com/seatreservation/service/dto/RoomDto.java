package com.seatreservation.service.dto;

import com.seatreservation.domain.enumeration.AvailabilityStatus;
import com.seatreservation.domain.enumeration.RoomType;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;


public class RoomDto implements Serializable {

    private Long id;

    @NotBlank
    private String name;

    @Lob
    private byte[] map;

    private String mapContentType;

    @NotNull
    private AvailabilityStatus status;

    @NotNull
    private RoomType roomType;

    private CoordinatesDto coordinates;

    private DimensionsDto dimensions;

    private FloorDto floor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getMap() {
        return map;
    }

    public void setMap(byte[] map) {
        this.map = map;
    }

    public String getMapContentType() {
        return mapContentType;
    }

    public void setMapContentType(String mapContentType) {
        this.mapContentType = mapContentType;
    }

    public AvailabilityStatus getStatus() {
        return status;
    }

    public void setStatus(AvailabilityStatus status) {
        this.status = status;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
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

    public FloorDto getFloor() {
        return floor;
    }

    public void setFloor(FloorDto floor) {
        this.floor = floor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RoomDto)) {
            return false;
        }

        RoomDto roomDto = (RoomDto) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, roomDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RoomDto{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", map='" + getMap() + "'" +
            ", status='" + getStatus() + "'" +
            ", roomType='" + getRoomType() + "'" +
            ", coordinates=" + getCoordinates() +
            ", dimensions=" + getDimensions() +
            ", floor=" + getFloor() +
            "}";
    }
}
