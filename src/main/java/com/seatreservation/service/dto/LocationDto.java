package com.seatreservation.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

public class LocationDto implements Serializable {

    private Long id;

    @NotBlank
    private String name;

    private Set<UserDto> locationAdmins = new HashSet<>();

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

    public Set<UserDto> getLocationAdmins() {
        return locationAdmins;
    }

    public void setLocationAdmins(Set<UserDto> locationAdmins) {
        this.locationAdmins = locationAdmins;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LocationDto)) {
            return false;
        }

        LocationDto locationDto = (LocationDto) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, locationDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LocationDto{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", locationAdmins=" + getLocationAdmins() +
            "}";
    }
}
