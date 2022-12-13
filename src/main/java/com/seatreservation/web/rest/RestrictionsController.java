package com.seatreservation.web.rest;

import com.seatreservation.domain.Restrictions;
import com.seatreservation.service.NotificationAlertService;
import com.seatreservation.service.RestrictionHandler;
import com.seatreservation.service.RestrictionsService;
import com.seatreservation.service.dto.RestrictionsDto;
import com.seatreservation.service.mapper.RestrictionsMapper;
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
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static com.seatreservation.utils.EntityAlertUtil.*;

/**
 * REST controller for managing {@link Restrictions}.
 */
@RestController
@RequestMapping("/api/restrictions")
public class RestrictionsController {

    private final Logger log = LoggerFactory.getLogger(RestrictionsController.class);
    private final RestrictionsService restrictionsService;
    private final RestrictionsMapper restrictionsMapper;

    private final RestrictionHandler restrictionHandler;
    private final NotificationAlertService notificationAlertService;

    public RestrictionsController(RestrictionsService restrictionsService, RestrictionsMapper restrictionsMapper, RestrictionHandler restrictionHandler, NotificationAlertService notificationAlertService) {
        this.restrictionsMapper = restrictionsMapper;
        this.restrictionsService = restrictionsService;
        this.restrictionHandler = restrictionHandler;
        this.notificationAlertService = notificationAlertService;
    }

    @PostMapping()
    public ResponseEntity<RestrictionsDto> createRestrictions(@Valid @RequestBody RestrictionsDto restrictionsDto) throws URISyntaxException {
        log.debug("REST request to save Restrictions : {}", restrictionsDto);
        Restrictions createdRestrictions = restrictionsService.save(restrictionsMapper.toEntity(restrictionsDto));
        RestrictionsDto result = restrictionsMapper.toDto(createdRestrictions);
        return ResponseEntity
            .created(new URI("/api/restrictions/" + result.getId()))
            .headers(createEntityCreationAlert(Restrictions.class, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestrictionsDto> updateRestrictions(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody RestrictionsDto restrictionsDto) throws URISyntaxException {
        log.debug("REST request to update Restrictions : {}, {}", id, restrictionsDto);
        Restrictions createdRestrictions = restrictionsService.update(restrictionsMapper.toEntity(restrictionsDto), id);
        RestrictionsDto result = restrictionsMapper.toDto(createdRestrictions);
        return ResponseEntity.ok().headers(createEntityUpdateAlert(Restrictions.class, restrictionsDto.getId().toString())).body(result);
    }

    @GetMapping()
    public ResponseEntity<List<RestrictionsDto>> getAllRestrictions(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Restrictions");
        Page<RestrictionsDto> page = restrictionsService.findAll(pageable).map(restrictionsMapper::toDto);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestrictionsDto> getRestrictions(@PathVariable Long id) {
        log.debug("REST request to get Restrictions : {}", id);
        Optional<RestrictionsDto> restrictionsDto = restrictionsService.findOne(id).map(restrictionsMapper::toDto);
        return ResponseUtil.wrapOrNotFound(restrictionsDto);
    }

    @GetMapping("/current")
    public ResponseEntity<RestrictionsDto> getCurrentRestrictions() {
        log.debug("REST request to get current Restrictions");
        Optional<RestrictionsDto> currentRestrictionsDto = restrictionsService.findCurrent().map(restrictionsMapper::toDto);
        return ResponseUtil.wrapOrNotFound(currentRestrictionsDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestrictions(@PathVariable Long id) {
        log.debug("REST request to delete Restrictions : {}", id);
        restrictionsService.delete(id);
        return ResponseEntity.noContent().headers(createEntityDeletionAlert(Restrictions.class, id.toString())).build();
    }
}
