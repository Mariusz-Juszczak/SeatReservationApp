package com.seatreservation.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@Entity
@Table(name = "floor")
public class Floor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "number", nullable = false)
    private Integer number;

    @Lob
    @Column(name = "map")
    private byte[] map;

    @Column(name = "map_content_type")
    private String mapContentType;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
    private Dimensions dimensions;

    @OneToMany(mappedBy = "floor")
    private Set<Room> rooms = new HashSet<>();

    @ManyToOne
    private Building building;

    public Long getId() {
        return this.id;
    }

    public Floor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Floor name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumber() {
        return this.number;
    }

    public Floor number(Integer number) {
        this.setNumber(number);
        return this;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public byte[] getMap() {
        return this.map;
    }

    public Floor map(byte[] map) {
        this.setMap(map);
        return this;
    }

    public void setMap(byte[] map) {
        this.map = map;
    }

    public String getMapContentType() {
        return this.mapContentType;
    }

    public Floor mapContentType(String mapContentType) {
        this.mapContentType = mapContentType;
        return this;
    }

    public void setMapContentType(String mapContentType) {
        this.mapContentType = mapContentType;
    }

    public Dimensions getDimensions() {
        return this.dimensions;
    }

    public void setDimensions(Dimensions dimensions) {
        this.dimensions = dimensions;
    }

    public Floor dimensions(Dimensions dimensions) {
        this.setDimensions(dimensions);
        return this;
    }

    public Set<Room> getRooms() {
        return this.rooms;
    }

    public void setRooms(Set<Room> rooms) {
        if (this.rooms != null) {
            this.rooms.forEach(i -> i.setFloor(null));
        }
        if (rooms != null) {
            rooms.forEach(i -> i.setFloor(this));
        }
        this.rooms = rooms;
    }

    public Floor rooms(Set<Room> rooms) {
        this.setRooms(rooms);
        return this;
    }

    public Floor addRooms(Room room) {
        this.rooms.add(room);
        room.setFloor(this);
        return this;
    }

    public Floor removeRooms(Room room) {
        this.rooms.remove(room);
        room.setFloor(null);
        return this;
    }

    public Building getBuilding() {
        return this.building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public Floor building(Building building) {
        this.setBuilding(building);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Floor)) {
            return false;
        }
        return id != null && id.equals(((Floor) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Floor{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", number=" + getNumber() +
            ", map='" + getMap() + "'" +
            ", mapContentType='" + getMapContentType() + "'" +
            "}";
    }
}
