package com.seatreservation.web.rest;

import com.seatreservation.domain.Dimensions;
import com.seatreservation.service.DimensionsService;
import com.seatreservation.service.dto.DimensionsDto;
import com.seatreservation.service.mapper.DimensionsMapper;
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

import static com.seatreservation.utils.EntityAlertUtil.*;

/**
 * REST controller for managing {@link Dimensions}.
 */
@RestController
@RequestMapping("/api/dimensions")
public class DimensionsController {

    private final Logger log = LoggerFactory.getLogger(DimensionsController.class);
    private final DimensionsService dimensionsService;
    private final DimensionsMapper dimensionsMapper;

    public DimensionsController(DimensionsService dimensionsService, DimensionsMapper dimensionsMapper) {
        this.dimensionsService = dimensionsService;
        this.dimensionsMapper = dimensionsMapper;
    }


    @PostMapping()
    public ResponseEntity<DimensionsDto> createDimensions(@Valid @RequestBody DimensionsDto dimensionsDto) throws URISyntaxException {
        log.debug("REST request to save Dimensions : {}", dimensionsDto);
        Dimensions createdDimensions = dimensionsService.save(dimensionsMapper.toEntity(dimensionsDto));
        DimensionsDto result = dimensionsMapper.toDto(createdDimensions);
        return ResponseEntity
            .created(new URI("/api/dimensions/" + result.getId()))
            .headers(createEntityCreationAlert(Dimensions.class, result.getId().toString()))
            .body(result);
    }


    @PutMapping("/{id}")
    public ResponseEntity<DimensionsDto> updateDimensions(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DimensionsDto dimensionsDto
    ) throws URISyntaxException {
        log.debug("REST request to update Dimensions : {}, {}", id, dimensionsDto);
        Dimensions createdDimensions = dimensionsService.update(dimensionsMapper.toEntity(dimensionsDto), id);
        DimensionsDto result = dimensionsMapper.toDto(createdDimensions);
        return ResponseEntity
            .ok()
            .headers(createEntityUpdateAlert(Dimensions.class, dimensionsDto.getId().toString()))
            .body(result);
    }

    @GetMapping()
    public ResponseEntity<List<DimensionsDto>> getAllDimensions(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String filter
    ) {
        if ("seat-is-null".equals(filter)) {
            log.debug("REST request to get all Dimensionss where seat is null");
            return new ResponseEntity<>(dimensionsMapper.toDto(dimensionsService.findAllWhereSeatIsNull()), HttpStatus.OK);
        }

        if ("room-is-null".equals(filter)) {
            log.debug("REST request to get all Dimensionss where room is null");
            return new ResponseEntity<>(dimensionsMapper.toDto(dimensionsService.findAllWhereRoomIsNull()), HttpStatus.OK);
        }

        if ("floor-is-null".equals(filter)) {
            log.debug("REST request to get all Dimensionss where floor is null");
            return new ResponseEntity<>(dimensionsMapper.toDto(dimensionsService.findAllWhereFloorIsNull()), HttpStatus.OK);
        }
        log.debug("REST request to get a page of Dimensions");
        Page<DimensionsDto> page = dimensionsService.findAll(pageable).map(dimensionsMapper::toDto);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DimensionsDto> getDimensions(@PathVariable Long id) {
        log.debug("REST request to get Dimensions : {}", id);
        Optional<DimensionsDto> dimensionsDto = dimensionsService.findOne(id).map(dimensionsMapper::toDto);
        return ResponseUtil.wrapOrNotFound(dimensionsDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDimensions(@PathVariable Long id) {
        log.debug("REST request to delete Dimensions : {}", id);
        dimensionsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(createEntityDeletionAlert(Dimensions.class, id.toString()))
            .build();
    }
}
