package com.seatreservation.service;

import com.seatreservation.domain.Dimensions;
import com.seatreservation.repository.DimensionsRepository;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.seatreservation.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DimensionsService {

    private final Logger log = LoggerFactory.getLogger(DimensionsService.class);

    private final DimensionsRepository dimensionsRepository;

    public DimensionsService(DimensionsRepository dimensionsRepository) {
        this.dimensionsRepository = dimensionsRepository;
    }

    public Dimensions save(Dimensions dimensions) {
        if (dimensions.getId() != null) {
            throw new BadRequestAlertException("A new dimensions cannot already have an ID",Dimensions.class, "id exists");
        }
        log.debug("Request to save Dimensions : {}", dimensions);
        return dimensionsRepository.save(dimensions);
    }

    public Dimensions update(Dimensions dimensions, Long id) {
        if (dimensions.getId() == null) {
            throw new BadRequestAlertException("Invalid id", Dimensions.class, "id null");
        }
        if (!Objects.equals(id, dimensions.getId())) {
            throw new BadRequestAlertException("Invalid ID", Dimensions.class, "id invalid");
        }
        if (!dimensionsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", Dimensions.class, "id not found");
        }
        log.debug("Request to save Dimensions : {}", dimensions);
        return dimensionsRepository.save(dimensions);
    }

    @Transactional(readOnly = true)
    public Page<Dimensions> findAll(Pageable pageable) {
        log.debug("Request to get all Dimensions");
        return dimensionsRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<Dimensions> findAllWhereSeatIsNull() {
        log.debug("Request to get all dimensions where Seat is null");
        return StreamSupport
            .stream(dimensionsRepository.findAll().spliterator(), false)
            .filter(dimensions -> dimensions.getSeat() == null)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional(readOnly = true)
    public List<Dimensions> findAllWhereRoomIsNull() {
        log.debug("Request to get all dimensions where Room is null");
        return StreamSupport
            .stream(dimensionsRepository.findAll().spliterator(), false)
            .filter(dimensions -> dimensions.getRoom() == null)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional(readOnly = true)
    public List<Dimensions> findAllWhereFloorIsNull() {
        log.debug("Request to get all dimensions where Floor is null");
        return StreamSupport
            .stream(dimensionsRepository.findAll().spliterator(), false)
            .filter(dimensions -> dimensions.getFloor() == null)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional(readOnly = true)
    public Optional<Dimensions> findOne(Long id) {
        log.debug("Request to get Dimensions : {}", id);
        return dimensionsRepository.findById(id);
    }

    public void delete(Long id) {
        log.debug("Request to delete Dimensions : {}", id);
        dimensionsRepository.deleteById(id);
    }
}
