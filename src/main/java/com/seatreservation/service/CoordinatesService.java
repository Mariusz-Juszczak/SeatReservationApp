package com.seatreservation.service;

import com.seatreservation.domain.Coordinates;
import com.seatreservation.repository.CoordinatesRepository;
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
public class CoordinatesService {

    private final Logger log = LoggerFactory.getLogger(CoordinatesService.class);

    private final CoordinatesRepository coordinatesRepository;

    public CoordinatesService(CoordinatesRepository coordinatesRepository) {
        this.coordinatesRepository = coordinatesRepository;
    }

    public Coordinates save(Coordinates coordinates) {
        if (coordinates.getId() != null) {
            throw new BadRequestAlertException("A new coordinates cannot already have an ID", Coordinates.class, "id exists");
        }
        log.debug("Request to save Coordinates : {}", coordinates);
        return coordinatesRepository.save(coordinates);
    }

    public Coordinates update(Coordinates coordinates, Long id) {
        if (coordinates.getId() == null) {
            throw new BadRequestAlertException("Invalid id", Coordinates.class, "id null");
        }
        if (!Objects.equals(id, coordinates.getId())) {
            throw new BadRequestAlertException("Invalid ID", Coordinates.class, "id invalid");
        }

        if (!coordinatesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", Coordinates.class, "id not found");
        }
        log.debug("Request to save Coordinates : {}", coordinates);
        return coordinatesRepository.save(coordinates);
    }

    @Transactional(readOnly = true)
    public Page<Coordinates> findAll(Pageable pageable) {
        log.debug("Request to get all Coordinates");
        return coordinatesRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<Coordinates> findAllWhereSeatIsNull() {
        log.debug("Request to get all coordinates where Seat is null");
        return StreamSupport
            .stream(coordinatesRepository.findAll().spliterator(), false)
            .filter(coordinates -> coordinates.getSeat() == null)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional(readOnly = true)
    public List<Coordinates> findAllWhereRoomIsNull() {
        log.debug("Request to get all coordinates where Room is null");
        return StreamSupport
            .stream(coordinatesRepository.findAll().spliterator(), false)
            .filter(coordinates -> coordinates.getRoom() == null)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional(readOnly = true)
    public Optional<Coordinates> findOne(Long id) {
        log.debug("Request to get Coordinates : {}", id);
        return coordinatesRepository.findById(id);
    }

    public void delete(Long id) {
        log.debug("Request to delete Coordinates : {}", id);
        coordinatesRepository.deleteById(id);
    }
}
