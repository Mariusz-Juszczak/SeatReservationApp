package com.seatreservation.web.rest;

import com.seatreservation.domain.Location;
import com.seatreservation.service.LocationService;
import com.seatreservation.service.dto.LocationDto;
import com.seatreservation.service.mapper.LocationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing {@link Location}.
 */
@RestController
@RequestMapping("/api/locations")
public class LocationController {

    private final Logger log = LoggerFactory.getLogger(LocationController.class);
    private final LocationService locationService;
    private final LocationMapper locationMapper;

    public LocationController(LocationService locationService, LocationMapper locationMapper) {
        this.locationService = locationService;
        this.locationMapper = locationMapper;
    }

    @PostMapping()
    public ResponseEntity<LocationDto> createLocation(@Valid @RequestBody LocationDto locationDto) throws URISyntaxException {
        log.debug("REST request to save Location : {}", locationDto);
        Location createdLocation = locationService.save(locationMapper.toEntity(locationDto));
        LocationDto result = locationMapper.toDto(createdLocation);
        return ResponseEntity.created(new URI("/api/locations/" + result.getId())).headers(createEntityCreationAlert(Location.class, result.getId().toString())).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationDto> updateLocation(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody LocationDto locationDto) throws URISyntaxException {
        Location createdLocation = locationService.update(locationMapper.toEntity(locationDto), id);
        LocationDto result = locationMapper.toDto(createdLocation);
        log.debug("REST request to update Location : {}, {}", id, locationDto);
        return ResponseEntity.ok().headers(createEntityUpdateAlert(Location.class, locationDto.getId().toString())).body(result);
    }

    @GetMapping()
    public ResponseEntity<List<LocationDto>> getAllLocations(@org.springdoc.api.annotations.ParameterObject Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of Locations");
        Page<LocationDto> page;
        if (eagerload) {
            page = locationService.findAllWithEagerRelationships(pageable).map(locationMapper::toDto);
        } else {
            page = locationService.findAll(pageable).map(locationMapper::toBasicDto);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationDto> getLocation(@PathVariable Long id) {
        log.debug("REST request to get Location : {}", id);
        Optional<LocationDto> locationDto = locationService.findOne(id).map(locationMapper::toDto);
        return ResponseUtil.wrapOrNotFound(locationDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        log.debug("REST request to delete Location : {}", id);
        locationService.delete(id);
        return ResponseEntity.noContent().headers(createEntityDeletionAlert(Location.class, id.toString())).build();
    }
}
