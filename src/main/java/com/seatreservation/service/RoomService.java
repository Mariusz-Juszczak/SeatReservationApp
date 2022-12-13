package com.seatreservation.service;

import com.seatreservation.domain.Room;
import com.seatreservation.repository.RoomRepository;

import java.util.Objects;
import java.util.Optional;

import com.seatreservation.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoomService {

    private final Logger log = LoggerFactory.getLogger(RoomService.class);

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Room save(Room room) {
        if (room.getId() != null) {
            throw new BadRequestAlertException("A new room cannot already have an ID", Room.class, "id exists");
        }
        log.debug("Request to save Room : {}", room);
        return roomRepository.save(room);
    }

    public Room update(Room room,Long id) {
        if (room.getId() == null) {
            throw new BadRequestAlertException("Invalid id", Room.class, "id null");
        }
        if (!Objects.equals(id, room.getId())) {
            throw new BadRequestAlertException("Invalid ID", Room.class, "id invalid");
        }
        if (!roomRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", Room.class, "id not found");
        }
        log.debug("Request to save Room : {}", room);
        return roomRepository.save(room);
    }

    @Transactional(readOnly = true)
    public Page<Room> findAll(Pageable pageable) {
        log.debug("Request to get all Rooms");
        return roomRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Room> findOne(Long id) {
        log.debug("Request to get Room : {}", id);
        return roomRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Page<Room> findAllByFloor(Pageable pageable, Long floorId) {
        log.debug("Request to get all Rooms from Floor: {}", floorId);
        return roomRepository.findRoomsByFloorId(pageable, floorId);
    }

    public void delete(Long id) {
        Room room = roomRepository.getReferenceById(id);
        if (!room.getSeats().isEmpty()) {
            throw new BadRequestAlertException("Object have children", Room.class, "objectHaveChildren");
        }
        log.debug("Request to delete Room : {}", id);
        roomRepository.deleteById(id);
    }
}
