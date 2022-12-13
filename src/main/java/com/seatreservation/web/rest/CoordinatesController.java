package com.seatreservation.web.rest;

import com.seatreservation.domain.Coordinates;
import com.seatreservation.service.CoordinatesService;
import com.seatreservation.service.dto.CoordinatesDto;
import com.seatreservation.service.mapper.CoordinatesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import static com.seatreservation.utils.EntityAlertUtil.createEntityCreationAlert;
import static com.seatreservation.utils.EntityAlertUtil.createEntityDeletionAlert;


@RestController
@RequestMapping("/api/coordinates")
public class CoordinatesController {

    private final Logger log = LoggerFactory.getLogger(CoordinatesController.class);
    private final CoordinatesService coordinatesService;
    private final CoordinatesMapper coordinatesMapper;

    public CoordinatesController(CoordinatesService coordinatesService, CoordinatesMapper coordinatesMapper) {
        this.coordinatesService = coordinatesService;
        this.coordinatesMapper = coordinatesMapper;
    }


    @PostMapping()
    public ResponseEntity<CoordinatesDto> createCoordinates(@Valid @RequestBody CoordinatesDto coordinatesDto) throws URISyntaxException {
        log.debug("REST request to save Coordinates : {}", coordinatesDto);
        Coordinates createdCoordinates = coordinatesService.save(coordinatesMapper.toEntity(coordinatesDto));
        CoordinatesDto result = coordinatesMapper.toDto(createdCoordinates);
        return ResponseEntity
            .created(new URI("/api/coordinates/" + result.getId()))
            .headers(createEntityCreationAlert(Coordinates.class, result.getId().toString()))
            .body(result);
    }


    @PutMapping("/{id}")
    public ResponseEntity<CoordinatesDto> updateCoordinates(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CoordinatesDto coordinatesDto
    ) throws URISyntaxException {
        log.debug("REST request to update Coordinates : {}, {}", id, coordinatesDto);
        Coordinates createdCoordinates = coordinatesService.update(coordinatesMapper.toEntity(coordinatesDto), id);
        CoordinatesDto result = coordinatesMapper.toDto(createdCoordinates);
        return ResponseEntity
            .ok()
            .headers(createEntityCreationAlert(Coordinates.class, coordinatesDto.getId().toString()))
            .body(result);
    }

    @GetMapping()
    public ResponseEntity<List<CoordinatesDto>> getAllCoordinates(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String filter
    ) {
        if ("seat-is-null".equals(filter)) {
            log.debug("REST request to get all Coordinatess where seat is null");
            return new ResponseEntity<>(coordinatesMapper.toDto(coordinatesService.findAllWhereSeatIsNull()), HttpStatus.OK);
        }
        if ("room-is-null".equals(filter)) {
            log.debug("REST request to get all Coordinatess where room is null");
            return new ResponseEntity<>(coordinatesMapper.toDto(coordinatesService.findAllWhereRoomIsNull()), HttpStatus.OK);
        }
        log.debug("REST request to get a page of Coordinates");
        Page<CoordinatesDto> page = coordinatesService.findAll(pageable).map(coordinatesMapper::toDto);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }


    @GetMapping("/{id}")
    public ResponseEntity<CoordinatesDto> getCoordinates(@PathVariable Long id) {
        log.debug("REST request to get Coordinates : {}", id);
        Optional<CoordinatesDto> coordinatesDto = coordinatesService.findOne(id).map(coordinatesMapper::toDto);
        return ResponseUtil.wrapOrNotFound(coordinatesDto);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoordinates(@PathVariable Long id) {
        log.debug("REST request to delete Coordinates : {}", id);
        coordinatesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(createEntityDeletionAlert(Coordinates.class, id.toString()))
            .build();
    }
}
