package com.seatreservation.domain;

import com.seatreservation.domain.enumeration.AvailabilityStatus;
import com.seatreservation.domain.enumeration.RoomType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@Entity
@Table(name = "room")
public class Room implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Lob
    @Column(name = "map")
    private byte[] map;

    @Column(name = "map_content_type")
    private String mapContentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AvailabilityStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "room_type", nullable = false)
    private RoomType roomType;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
    private Coordinates coordinates;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
    private Dimensions dimensions;

    @OneToMany(mappedBy = "room")
    private Set<Seat> seats = new HashSet<>();

    @ManyToOne
    private Floor floor;

    public Long getId() {
        return this.id;
    }

    public Room id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Room name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getMap() {
        return this.map;
    }

    public Room map(byte[] map) {
        this.setMap(map);
        return this;
    }

    public void setMap(byte[] map) {
        this.map = map;
    }

    public String getMapContentType() {
        return this.mapContentType;
    }

    public Room mapContentType(String mapContentType) {
        this.mapContentType = mapContentType;
        return this;
    }

    public void setMapContentType(String mapContentType) {
        this.mapContentType = mapContentType;
    }

    public AvailabilityStatus getStatus() {
        return this.status;
    }

    public Room status(AvailabilityStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(AvailabilityStatus status) {
        this.status = status;
    }

    public RoomType getRoomType() {
        return this.roomType;
    }

    public Room roomType(RoomType roomType) {
        this.setRoomType(roomType);
        return this;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public Coordinates getCoordinates() {
        return this.coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Room coordinates(Coordinates coordinates) {
        this.setCoordinates(coordinates);
        return this;
    }

    public Dimensions getDimensions() {
        return this.dimensions;
    }

    public void setDimensions(Dimensions dimensions) {
        this.dimensions = dimensions;
    }

    public Room dimensions(Dimensions dimensions) {
        this.setDimensions(dimensions);
        return this;
    }

    public Set<Seat> getSeats() {
        return this.seats;
    }

    public void setSeats(Set<Seat> seats) {
        if (this.seats != null) {
            this.seats.forEach(i -> i.setRoom(null));
        }
        if (seats != null) {
            seats.forEach(i -> i.setRoom(this));
        }
        this.seats = seats;
    }

    public Room seats(Set<Seat> seats) {
        this.setSeats(seats);
        return this;
    }

    public Room addSeats(Seat seat) {
        this.seats.add(seat);
        seat.setRoom(this);
        return this;
    }

    public Room removeSeats(Seat seat) {
        this.seats.remove(seat);
        seat.setRoom(null);
        return this;
    }

    public Floor getFloor() {
        return this.floor;
    }

    public void setFloor(Floor floor) {
        this.floor = floor;
    }

    public Room floor(Floor floor) {
        this.setFloor(floor);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Room)) {
            return false;
        }
        return id != null && id.equals(((Room) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Room{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", map='" + getMap() + "'" +
            ", mapContentType='" + getMapContentType() + "'" +
            ", status='" + getStatus() + "'" +
            ", roomType='" + getRoomType() + "'" +
            "}";
    }
}
