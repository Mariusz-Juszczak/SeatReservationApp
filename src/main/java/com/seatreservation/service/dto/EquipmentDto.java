package com.seatreservation.service.dto;

import com.seatreservation.domain.enumeration.EquipmentType;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;


public class EquipmentDto implements Serializable {

    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private EquipmentType type;

    private String description;

    private SeatDto seat;

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

    public EquipmentType getType() {
        return type;
    }

    public void setType(EquipmentType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SeatDto getSeat() {
        return seat;
    }

    public void setSeat(SeatDto seat) {
        this.seat = seat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EquipmentDto)) {
            return false;
        }

        EquipmentDto equipmentDto = (EquipmentDto) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, equipmentDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EquipmentDto{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", description='" + getDescription() + "'" +
            ", seat=" + getSeat() +
            "}";
    }
}
