package com.seatreservation.web.rest;

import com.seatreservation.domain.Building;
import com.seatreservation.service.BuildingService;
import com.seatreservation.service.dto.BuildingDto;
import com.seatreservation.service.mapper.BuildingMapper;
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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import static com.seatreservation.utils.EntityAlertUtil.*;

/**
 * REST controller for managing {@link Building}.
 */
@RestController
@RequestMapping("/api/buildings")
public class BuildingController {

    private final Logger log = LoggerFactory.getLogger(BuildingController.class);
    private final BuildingService buildingService;
    private final BuildingMapper buildingMapper;

    public BuildingController(BuildingService buildingService, BuildingMapper buildingMapper) {
        this.buildingService = buildingService;
        this.buildingMapper = buildingMapper;
    }


    @PostMapping()
    public ResponseEntity<BuildingDto> createBuilding(@Valid @RequestBody BuildingDto buildingDto) throws URISyntaxException {
        log.debug("REST request to save Building : {}", buildingDto);
        Building createdBuilding = buildingService.save(buildingMapper.toEntity(buildingDto));
        BuildingDto result = buildingMapper.toDto(createdBuilding);
        return ResponseEntity.created(new URI("/api/buildings/" + result.getId())).headers(createEntityCreationAlert(Building.class, result.getId().toString())).body(result);
    }


    @PutMapping("/{id}")
    public ResponseEntity<BuildingDto> updateBuilding(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody BuildingDto buildingDto) throws URISyntaxException {
        log.debug("REST request to update Building : {}, {}", id, buildingDto);
        Building createdBuilding = buildingService.update(buildingMapper.toEntity(buildingDto), id);
        BuildingDto result = buildingMapper.toDto(createdBuilding);
        return ResponseEntity.ok().headers(createEntityUpdateAlert(Building.class, buildingDto.getId().toString())).body(result);
    }


    @GetMapping()
    public ResponseEntity<List<BuildingDto>> getAllBuildings(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Buildings");
        Page<BuildingDto> page = buildingService.findAll(pageable).map(buildingMapper::toDto);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }


    @GetMapping("/{id}")
    public ResponseEntity<BuildingDto> getBuilding(@PathVariable Long id) {
        log.debug("REST request to get Building : {}", id);
        Optional<BuildingDto> buildingDto = buildingService.findOne(id).map(buildingMapper::toDto);
        return ResponseUtil.wrapOrNotFound(buildingDto);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBuilding(@PathVariable Long id) {
        log.debug("REST request to delete Building : {}", id);
        buildingService.delete(id);
        return ResponseEntity.noContent().headers(createEntityDeletionAlert(Building.class, id.toString())).build();
    }


    @GetMapping("/locations/{id}")
    public ResponseEntity<List<BuildingDto>> getAllBuildingsByLocation(@ParameterObject Pageable pageable, @PathVariable("id") Long locationId) {
        log.debug("REST request to get a page of Buildings by Location: {}", locationId);
        Page<BuildingDto> page = buildingService.findAllByLocation(pageable, locationId).map(buildingMapper::toDto);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
