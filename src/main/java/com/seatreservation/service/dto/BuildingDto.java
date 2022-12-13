package com.seatreservation.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.*;


public class BuildingDto implements Serializable {

    private Long id;

    @NotBlank
    private String name;

    @Valid
    private AddressDto address;

    private LocationDto location;

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

    public AddressDto getAddress() {
        return address;
    }

    public void setAddress(AddressDto address) {
        this.address = address;
    }

    public LocationDto getLocation() {
        return location;
    }

    public void setLocation(LocationDto location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BuildingDto)) {
            return false;
        }

        BuildingDto buildingDto = (BuildingDto) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, buildingDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BuildingDto{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", address=" + getAddress() +
            ", location=" + getLocation() +
            "}";
    }
}
