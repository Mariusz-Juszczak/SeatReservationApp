package com.seatreservation.web.rest;

import com.seatreservation.domain.NotificationAlert;
import com.seatreservation.service.NotificationAlertService;
import com.seatreservation.service.dto.NotificationAlertDto;
import com.seatreservation.service.mapper.NotificationAlertMapper;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import static com.seatreservation.utils.EntityAlertUtil.*;

/**
 * REST controller for managing {@link NotificationAlert}.
 */
@RestController
@RequestMapping("/api/notification-alerts")
public class NotificationAlertController {

    private final Logger log = LoggerFactory.getLogger(NotificationAlertController.class);
    private final NotificationAlertService notificationAlertService;
    private final NotificationAlertMapper notificationAlertMapper;

    public NotificationAlertController(NotificationAlertService notificationAlertService, NotificationAlertMapper notificationAlertMapper) {
        this.notificationAlertService = notificationAlertService;
        this.notificationAlertMapper = notificationAlertMapper;
    }


    @PostMapping()
    public ResponseEntity<NotificationAlertDto> createNotificationAlert(@RequestBody NotificationAlertDto notificationAlertDto) throws URISyntaxException {
        log.debug("REST request to save NotificationAlert : {}", notificationAlertDto);
        NotificationAlert createdNotificationAlert = notificationAlertService.save(notificationAlertMapper.toEntity(notificationAlertDto));
        NotificationAlertDto result = notificationAlertMapper.toDto(createdNotificationAlert);
        return ResponseEntity.created(new URI("/api/notification-alerts/" + result.getId())).headers(createEntityCreationAlert(NotificationAlert.class, result.getId().toString())).body(result);
    }


    @PutMapping("/{id}")
    public ResponseEntity<NotificationAlertDto> updateNotificationAlert(@PathVariable(value = "id", required = false) final Long id, @RequestBody NotificationAlertDto notificationAlertDto) {
        log.debug("REST request to update NotificationAlert : {}, {}", id, notificationAlertDto);
        NotificationAlert createdNotificationAlert = notificationAlertService.update(notificationAlertMapper.toEntity(notificationAlertDto), id);
        NotificationAlertDto result = notificationAlertMapper.toDto(createdNotificationAlert);
        return ResponseEntity.ok().headers(createEntityUpdateAlert(NotificationAlert.class, notificationAlertDto.getId().toString())).body(result);
    }


    @GetMapping("/{id}")
    public ResponseEntity<NotificationAlertDto> getNotificationAlert(@PathVariable Long id) {
        log.debug("REST request to get NotificationAlert : {}", id);
        Optional<NotificationAlertDto> notificationAlertDto = notificationAlertService.findOne(id).map(notificationAlertMapper::toDto);
        return ResponseUtil.wrapOrNotFound(notificationAlertDto);
    }

    @PatchMapping("/read/{id}")
    public ResponseEntity<NotificationAlertDto> markAsRead(@PathVariable Long id) {
        log.debug("REST request to get NotificationAlert and update the state : {}", id);
        notificationAlertService.updateStateToRead(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping()
    public ResponseEntity<List<NotificationAlertDto>> getNotificationAlertsPerUser(@ParameterObject Pageable pageable) {
        log.debug("REST request to get page of NotificationAlertPerUser");
        Page<NotificationAlertDto> page = notificationAlertService.findAllByUser(pageable).map(notificationAlertMapper::toDto);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/new")
    public ResponseEntity<List<NotificationAlertDto>> getNewNotificationAlertsPerUser(@ParameterObject Pageable pageable) {
        log.debug("REST request to get page of new NotificationAlertPerUser");
        Page<NotificationAlertDto> page = notificationAlertService.findNewByUser(pageable).map(notificationAlertMapper::toDto);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> markAsDeleted(@PathVariable Long id) {
        log.debug("REST request to delete NotificationAlert : {}", id);
        notificationAlertService.updateStateToDeleted(id);
        return ResponseEntity.noContent().headers(createEntityDeletionAlert(NotificationAlert.class, id.toString())).build();
    }
}
