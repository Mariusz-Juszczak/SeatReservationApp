package com.seatreservation.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@Entity
@Table(name = "building")
public class Building implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
    private Address address;

    @OneToMany(mappedBy = "building")
    private Set<Floor> floors = new HashSet<>();

    @ManyToOne
    private Location location;

    public Long getId() {
        return this.id;
    }

    public Building id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Building name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return this.address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Building address(Address address) {
        this.setAddress(address);
        return this;
    }

    public Set<Floor> getFloors() {
        return this.floors;
    }

    public void setFloors(Set<Floor> floors) {
        if (this.floors != null) {
            this.floors.forEach(i -> i.setBuilding(null));
        }
        if (floors != null) {
            floors.forEach(i -> i.setBuilding(this));
        }
        this.floors = floors;
    }

    public Building floors(Set<Floor> floors) {
        this.setFloors(floors);
        return this;
    }

    public Building addFloors(Floor floor) {
        this.floors.add(floor);
        floor.setBuilding(this);
        return this;
    }

    public Building removeFloors(Floor floor) {
        this.floors.remove(floor);
        floor.setBuilding(null);
        return this;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Building location(Location location) {
        this.setLocation(location);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Building)) {
            return false;
        }
        return id != null && id.equals(((Building) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Building{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
