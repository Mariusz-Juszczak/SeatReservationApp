package com.seatreservation.domain;

import java.io.Serializable;
import javax.persistence.*;
@Entity
@Table(name = "coordinates")
public class Coordinates implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "from_top", nullable = false)
    private Double fromTop;

    @Column(name = "from_left", nullable = false)
    private Double fromLeft;

    @Column(name = "angle", nullable = false)
    private Double angle;

    @OneToOne(mappedBy = "coordinates")
    private Seat seat;

    @OneToOne(mappedBy = "coordinates")
    private Room room;

    public Long getId() {
        return this.id;
    }

    public Coordinates id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getFromTop() {
        return this.fromTop;
    }

    public Coordinates fromTop(Double fromTop) {
        this.setFromTop(fromTop);
        return this;
    }

    public void setFromTop(Double fromTop) {
        this.fromTop = fromTop;
    }

    public Double getFromLeft() {
        return this.fromLeft;
    }

    public Coordinates fromLeft(Double fromLeft) {
        this.setFromLeft(fromLeft);
        return this;
    }

    public void setFromLeft(Double fromLeft) {
        this.fromLeft = fromLeft;
    }

    public Double getAngle() {
        return this.angle;
    }

    public Coordinates angle(Double angle) {
        this.setAngle(angle);
        return this;
    }

    public void setAngle(Double angle) {
        this.angle = angle;
    }

    public Seat getSeat() {
        return this.seat;
    }

    public void setSeat(Seat seat) {
        if (this.seat != null) {
            this.seat.setCoordinates(null);
        }
        if (seat != null) {
            seat.setCoordinates(this);
        }
        this.seat = seat;
    }

    public Coordinates seat(Seat seat) {
        this.setSeat(seat);
        return this;
    }

    public Room getRoom() {
        return this.room;
    }

    public void setRoom(Room room) {
        if (this.room != null) {
            this.room.setCoordinates(null);
        }
        if (room != null) {
            room.setCoordinates(this);
        }
        this.room = room;
    }

    public Coordinates room(Room room) {
        this.setRoom(room);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Coordinates)) {
            return false;
        }
        return id != null && id.equals(((Coordinates) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Coordinates{" +
            "id=" + getId() +
            ", fromTop=" + getFromTop() +
            ", fromLeft=" + getFromLeft() +
            ", angle=" + getAngle() +
            "}";

    }
}
