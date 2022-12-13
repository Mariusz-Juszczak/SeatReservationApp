package com.seatreservation.service.dto;

import com.seatreservation.domain.enumeration.RestrictionType;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;


public class RestrictionsDto implements Serializable {

    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private ZonedDateTime fromDate;

    private ZonedDateTime toDate;

    @NotNull
    private RestrictionType restrictionType;

    private Integer restrictionValue;

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

    public RestrictionType getRestrictionType() {
        return restrictionType;
    }

    public void setRestrictionType(RestrictionType restrictionType) {
        this.restrictionType = restrictionType;
    }

    public Integer getRestrictionValue() {
        return restrictionValue;
    }

    public void setRestrictionValue(Integer restrictionValue) {
        this.restrictionValue = restrictionValue;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RestrictionsDto)) {
            return false;
        }

        RestrictionsDto restrictionsDto = (RestrictionsDto) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, restrictionsDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RestrictionsDto{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", fromDate='" + getFromDate() + "'" +
            ", toDate='" + getToDate() + "'" +
            ", restrictionType='" + getRestrictionType() + "'" +
            ", restrictionValue=" + getRestrictionValue() +
            "}";
    }
}
