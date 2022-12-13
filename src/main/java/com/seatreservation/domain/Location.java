package com.seatreservation.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@Entity
@Table(name = "location")
public class Location implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "location")
    private Set<Building> buildings = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_location__location_admins",
        joinColumns = @JoinColumn(name = "location_id"),
        inverseJoinColumns = @JoinColumn(name = "location_admins_id")
    )

    private Set<User> locationAdmins = new HashSet<>();

    public Long getId() {
        return this.id;
    }

    public Location id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Location name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Building> getBuildings() {
        return this.buildings;
    }

    public void setBuildings(Set<Building> buildings) {
        if (this.buildings != null) {
            this.buildings.forEach(i -> i.setLocation(null));
        }
        if (buildings != null) {
            buildings.forEach(i -> i.setLocation(this));
        }
        this.buildings = buildings;
    }

    public Location buildings(Set<Building> buildings) {
        this.setBuildings(buildings);
        return this;
    }

    public Location addBuildings(Building building) {
        this.buildings.add(building);
        building.setLocation(this);
        return this;
    }

    public Location removeBuildings(Building building) {
        this.buildings.remove(building);
        building.setLocation(null);
        return this;
    }

    public Set<User> getLocationAdmins() {
        return this.locationAdmins;
    }

    public void setLocationAdmins(Set<User> users) {
        this.locationAdmins = users;
    }

    public Location locationAdmins(Set<User> users) {
        this.setLocationAdmins(users);
        return this;
    }

    public Location addLocationAdmins(User user) {
        this.locationAdmins.add(user);
        return this;
    }

    public Location removeLocationAdmins(User user) {
        this.locationAdmins.remove(user);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Location)) {
            return false;
        }
        return id != null && id.equals(((Location) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Location{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
