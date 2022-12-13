package com.seatreservation.domain;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "dimensions")
public class Dimensions implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "height", nullable = false)
    private Double height;

    @Column(name = "width", nullable = false)
    private Double width;

    @OneToOne(mappedBy = "dimensions")
    private Seat seat;

    @OneToOne(mappedBy = "dimensions")
    private Room room;

    @OneToOne(mappedBy = "dimensions")
    private Floor floor;

    public Long getId() {
        return this.id;
    }

    public Dimensions id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getHeight() {
        return this.height;
    }

    public Dimensions height(Double height) {
        this.setHeight(height);
        return this;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWidth() {
        return this.width;
    }

    public Dimensions width(Double width) {
        this.setWidth(width);
        return this;
    }

    public void setWidth(Double width) {
        this.width = width;
    }



    public Seat getSeat() {
        return this.seat;
    }

    public void setSeat(Seat seat) {
        if (this.seat != null) {
            this.seat.setDimensions(null);
        }
        if (seat != null) {
            seat.setDimensions(this);
        }
        this.seat = seat;
    }

    public Dimensions seat(Seat seat) {
        this.setSeat(seat);
        return this;
    }

    public Room getRoom() {
        return this.room;
    }

    public void setRoom(Room room) {
        if (this.room != null) {
            this.room.setDimensions(null);
        }
        if (room != null) {
            room.setDimensions(this);
        }
        this.room = room;
    }

    public Dimensions room(Room room) {
        this.setRoom(room);
        return this;
    }

    public Floor getFloor() {
        return this.floor;
    }

    public void setFloor(Floor floor) {
        if (this.floor != null) {
            this.floor.setDimensions(null);
        }
        if (floor != null) {
            floor.setDimensions(this);
        }
        this.floor = floor;
    }

    public Dimensions floor(Floor floor) {
        this.setFloor(floor);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Dimensions)) {
            return false;
        }
        return id != null && id.equals(((Dimensions) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Dimensions{" +
            "id=" + getId() +
            ", height=" + getHeight() +
            ", width=" + getWidth() +
            "}";
    }
}
