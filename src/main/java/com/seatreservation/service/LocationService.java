package com.seatreservation.service;

import com.seatreservation.domain.Location;
import com.seatreservation.repository.LocationRepository;

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
public class LocationService {

    private final Logger log = LoggerFactory.getLogger(LocationService.class);
    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;

    }
    public Location save(Location location) {
        if (location.getId() != null) {
            throw new BadRequestAlertException("A new location cannot already have an ID", Location.class, "id exists");
        }
        log.debug("Request to save Location : {}", location);

        return locationRepository.save(location);
    }
    public Location update(Location location, Long id) {
        if (location.getId() == null) {
            throw new BadRequestAlertException("Invalid id", Location.class, "id null");
        }
        if (!Objects.equals(id, location.getId())) {
            throw new BadRequestAlertException("Invalid ID", Location.class, "id invalid");
        }

        if (!locationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", Location.class, "id not found");
        }
        log.debug("Request to save Location : {}", location);
        return locationRepository.save(location);
    }



    @Transactional(readOnly = true)
    public Page<Location> findAll(Pageable pageable) {
        log.debug("Request to get all Locations");
        return locationRepository.findAll(pageable);
    }

    public Page<Location> findAllWithEagerRelationships(Pageable pageable) {
        return locationRepository.findAllWithEagerRelationships(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Location> findOne(Long id) {
        log.debug("Request to get Location : {}", id);
        return locationRepository.findOneWithEagerRelationships(id);
    }

    public void delete(Long id) {
        Location location = locationRepository.getReferenceById(id);
        if (!location.getBuildings().isEmpty()) {
            throw new BadRequestAlertException("Object have children", Location.class, "objectHaveChildren");
        }
        log.debug("Request to delete Location : {}", id);
        locationRepository.deleteById(id);
    }
}
