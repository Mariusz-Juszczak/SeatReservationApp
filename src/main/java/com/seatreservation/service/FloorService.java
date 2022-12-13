package com.seatreservation.service;

import com.seatreservation.domain.Floor;
import com.seatreservation.repository.FloorRepository;

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
public class FloorService {

    private final Logger log = LoggerFactory.getLogger(FloorService.class);

    private final FloorRepository floorRepository;

    public FloorService(FloorRepository floorRepository) {
        this.floorRepository = floorRepository;
    }

    public Floor save(Floor floor) {
        if (floor.getId() != null) {
            throw new BadRequestAlertException("A new floor cannot already have an ID",Floor.class, "id exists");
        }
        log.debug("Request to save Floor : {}", floor);
        return  floorRepository.save(floor);
    }

    public Floor update(Floor floor, Long id) {
        if (floor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", Floor.class, "id null");
        }
        if (!Objects.equals(id, floor.getId())) {
            throw new BadRequestAlertException("Invalid ID", Floor.class, "id invalid");
        }

        if (!floorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", Floor.class, "id not found");
        }
        log.debug("Request to save Floor : {}", floor);
        return floorRepository.save(floor);
    }

    @Transactional(readOnly = true)
    public Page<Floor> findAll(Pageable pageable) {
        log.debug("Request to get all Floors");
        return floorRepository.findAll(pageable);
    }
    @Transactional(readOnly = true)
    public Optional<Floor> findOne(Long id) {
        log.debug("Request to get Floor : {}", id);
        return floorRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Page<Floor> findAllByBuilding(Pageable pageable, Long buildingId) {
        log.debug("Request to get all Floors from Building: {}", buildingId);
        return floorRepository.findFloorsByBuildingId(pageable, buildingId);
    }

    public void delete(Long id) {
        Floor floor = floorRepository.getReferenceById(id);
        if (!floor.getRooms().isEmpty()) {
            throw new BadRequestAlertException("Object have children", Floor.class, "objectHaveChildren");
        }
        log.debug("Request to delete Floor : {}", id);
        floorRepository.deleteById(id);
    }
}
