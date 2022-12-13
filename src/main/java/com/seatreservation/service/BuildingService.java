package com.seatreservation.service;

import com.seatreservation.domain.Building;
import com.seatreservation.repository.BuildingRepository;

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
public class BuildingService {

    private final Logger log = LoggerFactory.getLogger(BuildingService.class);

    private final BuildingRepository buildingRepository;


    public BuildingService(BuildingRepository buildingRepository) {
        this.buildingRepository = buildingRepository;
    }

    public Building save(Building building) {
        if (building.getId() != null) {
            throw new BadRequestAlertException("A new building cannot already have an ID", Building.class, "id exists");
        }
        log.debug("Request to save Building : {}", building);
        return buildingRepository.save(building);
    }

    public Building update(Building building, Long id) {
        if (building.getId() == null) {
            throw new BadRequestAlertException("Invalid id", Building.class, "id null");
        }
        if (!Objects.equals(id, building.getId())) {
            throw new BadRequestAlertException("Invalid ID", Building.class, "id invalid");
        }

        if (!buildingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", Building.class, "id not found");
        }
        log.debug("Request to save Building : {}", building);
        return buildingRepository.save(building);
    }

    @Transactional(readOnly = true)
    public Page<Building> findAll(Pageable pageable) {
        log.debug("Request to get all Buildings");
        return buildingRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Building> findOne(Long id) {
        log.debug("Request to get Building : {}", id);
        return buildingRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Page<Building> findAllByLocation(Pageable pageable, Long locationId) {
        log.debug("Request to get all Buildings from Location: {}", locationId);
        return buildingRepository.findBuildingsByLocationId(pageable, locationId);
    }

    public void delete(Long id) {
        Building building = buildingRepository.getReferenceById(id);
        if (!building.getFloors().isEmpty()) {
            throw new BadRequestAlertException("Object have children", Building.class, "objectHaveChildren");
        }
        log.debug("Request to delete Building : {}", id);
        buildingRepository.deleteById(id);
    }
}
