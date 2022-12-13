package com.seatreservation.domain;

import com.seatreservation.domain.enumeration.SeatStatus;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@Entity
@Table(name = "seat")
public class Seat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "seat_number", nullable = false)
    private Integer seatNumber;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SeatStatus status;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
    private Coordinates coordinates;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
    private Dimensions dimensions;

    @OneToMany(mappedBy = "seat", fetch = FetchType.EAGER)
    private Set<Equipment> equipments = new HashSet<>();

    @ManyToOne
    private Room room;

    public Long getId() {
        return this.id;
    }

    public Seat id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSeatNumber() {
        return this.seatNumber;
    }

    public Seat seatNumber(Integer seatNumber) {
        this.setSeatNumber(seatNumber);
        return this;
    }

    public void setSeatNumber(Integer seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getName() {
        return this.name;
    }

    public Seat name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SeatStatus getStatus() {
        return this.status;
    }

    public Seat status(SeatStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(SeatStatus status) {
        this.status = status;
    }

    public Coordinates getCoordinates() {
        return this.coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Seat coordinates(Coordinates coordinates) {
        this.setCoordinates(coordinates);
        return this;
    }

    public Dimensions getDimensions() {
        return this.dimensions;
    }

    public void setDimensions(Dimensions dimensions) {
        this.dimensions = dimensions;
    }

    public Seat dimensions(Dimensions dimensions) {
        this.setDimensions(dimensions);
        return this;
    }

    public Set<Equipment> getEquipments() {
        return this.equipments;
    }

    public void setEquipments(Set<Equipment> equipment) {
        if (this.equipments != null) {
            this.equipments.forEach(i -> i.setSeat(null));
        }
        if (equipment != null) {
            equipment.forEach(i -> i.setSeat(this));
        }
        this.equipments = equipment;
    }

    public Seat equipments(Set<Equipment> equipment) {
        this.setEquipments(equipment);
        return this;
    }

    public Seat addEquipments(Equipment equipment) {
        this.equipments.add(equipment);
        equipment.setSeat(this);
        return this;
    }

    public Seat removeEquipments(Equipment equipment) {
        this.equipments.remove(equipment);
        equipment.setSeat(null);
        return this;
    }

    public Room getRoom() {
        return this.room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Seat room(Room room) {
        this.setRoom(room);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Seat)) {
            return false;
        }
        return id != null && id.equals(((Seat) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Seat{" +
            "id=" + getId() +
            ", seatNumber=" + getSeatNumber() +
            ", name='" + getName() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
