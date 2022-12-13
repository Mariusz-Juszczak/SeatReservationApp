package com.seatreservation.web.rest;

import com.seatreservation.domain.Floor;
import com.seatreservation.service.FloorService;
import com.seatreservation.service.dto.FloorDto;
import com.seatreservation.service.mapper.FloorMapper;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import static com.seatreservation.utils.EntityAlertUtil.*;

/**
 * REST controller for managing {@link Floor}.
 */
@RestController
@RequestMapping("/api/floors")
public class FloorController {

    private final Logger log = LoggerFactory.getLogger(FloorController.class);
    private final FloorService floorService;
    private final FloorMapper floorMapper;

    public FloorController(FloorService floorService, FloorMapper floorMapper) {
        this.floorService = floorService;
        this.floorMapper = floorMapper;
    }

    @PostMapping()
    public ResponseEntity<FloorDto> createFloor(@Valid @RequestBody FloorDto floorDto) throws URISyntaxException {
        log.debug("REST request to save Floor : {}", floorDto);
        Floor createdFloor = floorService.save(floorMapper.toEntity(floorDto));
        FloorDto result = floorMapper.toDto(createdFloor);
        return ResponseEntity.created(new URI("/api/floors/" + result.getId())).headers(createEntityCreationAlert(Floor.class, result.getId().toString())).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FloorDto> updateFloor(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody FloorDto floorDto) throws URISyntaxException {
        log.debug("REST request to update Floor : {}, {}", id, floorDto);
        Floor createdFloor = floorService.update(floorMapper.toEntity(floorDto), id);
        FloorDto result = floorMapper.toDto(createdFloor);
        return ResponseEntity.ok().headers(createEntityUpdateAlert(Floor.class, floorDto.getId().toString())).body(result);
    }

    @GetMapping()
    public ResponseEntity<List<FloorDto>> getAllFloors(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Floors");
        Page<FloorDto> page = floorService.findAll(pageable).map(floorMapper::toDto);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FloorDto> getFloor(@PathVariable Long id) {
        log.debug("REST request to get Floor : {}", id);
        Optional<FloorDto> floorDto = floorService.findOne(id).map(floorMapper::toDto);
        return ResponseUtil.wrapOrNotFound(floorDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFloor(@PathVariable Long id) {
        log.debug("REST request to delete Floor : {}", id);
        floorService.delete(id);
        return ResponseEntity.noContent().headers(createEntityDeletionAlert(Floor.class, id.toString())).build();
    }

    @GetMapping("/buildings/{id}")
    public ResponseEntity<List<FloorDto>> getAllFloorsByBuilding(@ParameterObject Pageable pageable, @PathVariable("id") Long buildingId) {
        log.debug("REST request to get a page of Floors by Building: {}", buildingId);
        Page<FloorDto> page = floorService.findAllByBuilding(pageable, buildingId).map(floorMapper::toDto);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
