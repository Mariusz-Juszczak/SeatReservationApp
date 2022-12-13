package com.seatreservation.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

public class AddressDto implements Serializable {

    private Long id;

    @NotBlank
    private String country;

    @NotBlank
    private String street;

    @NotBlank
    private String postalCode;

    @NotBlank
    private String city;

    @NotBlank
    private String stateProvince;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AddressDto)) {
            return false;
        }

        AddressDto addressDto = (AddressDto) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, addressDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AddressDto{" +
            "id=" + getId() +
            ", country='" + getCountry() + "'" +
            ", street='" + getStreet() + "'" +
            ", postalCode='" + getPostalCode() + "'" +
            ", city='" + getCity() + "'" +
            ", stateProvince='" + getStateProvince() + "'" +
            "}";
    }
}
