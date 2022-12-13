package com.seatreservation.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

public class CoordinatesDto implements Serializable {

    private Long id;

    @NotNull
    private Double fromTop;

    @NotNull
    private Double fromLeft;

    @NotNull
    private Double angle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getFromTop() {
        return fromTop;
    }

    public void setFromTop(Double fromTop) {
        this.fromTop = fromTop;
    }

    public Double getFromLeft() {
        return fromLeft;
    }

    public void setFromLeft(Double fromLeft) {
        this.fromLeft = fromLeft;
    }

    public Double getAngle() {
        return angle;
    }

    public void setAngle(Double angle) {
        this.angle = angle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CoordinatesDto)) {
            return false;
        }

        CoordinatesDto coordinatesDto = (CoordinatesDto) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, coordinatesDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CoordinatesDto{" +
            "id=" + getId() +
            ", fromTop=" + getFromTop() +
            ", fromLeft=" + getFromLeft() +
            ", angle=" + getAngle() +
            "}";
    }
}
