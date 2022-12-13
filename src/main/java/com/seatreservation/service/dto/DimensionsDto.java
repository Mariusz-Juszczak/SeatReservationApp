package com.seatreservation.service.dto;

import com.seatreservation.domain.Dimensions;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link Dimensions} entity.
 */
public class DimensionsDto implements Serializable {

    private Long id;

    @NotNull
    private Double height;

    @NotNull
    private Double width;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DimensionsDto)) {
            return false;
        }

        DimensionsDto dimensionsDto = (DimensionsDto) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, dimensionsDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DimensionsDto{" +
            "id=" + getId() +
            ", height=" + getHeight() +
            ", width=" + getWidth() +
            "}";
    }
}
