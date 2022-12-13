package com.seatreservation.web.rest;

import com.seatreservation.domain.SeatReserved;
import com.seatreservation.repository.UserRepository;
import com.seatreservation.service.NotificationAlertService;
import com.seatreservation.service.SeatReservedService;
import com.seatreservation.service.SeatService;
import com.seatreservation.service.dto.SeatReservedDetailsDto;
import com.seatreservation.service.dto.SeatReservedDto;
import com.seatreservation.service.mapper.SeatReservedDetailsMapper;
import com.seatreservation.service.mapper.SeatReservedMapper;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
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

@RestController
@RequestMapping("/api/reserved-seats")
public class SeatReservedController {

    private final Logger log = LoggerFactory.getLogger(SeatReservedController.class);
    private final SeatReservedService seatReservedService;
    private final SeatReservedMapper seatReservedMapper;
    private final SeatReservedDetailsMapper seatReservedDetailsMapper;
    private final NotificationAlertService notificationAlertService;
    private final UserRepository userRepository;

    private final SeatService seatService;

    public SeatReservedController(SeatReservedService seatReservedService, SeatReservedMapper seatReservedMapper, SeatReservedDetailsMapper seatReservedDetailsMapper, NotificationAlertService notificationAlertService, UserRepository userRepository, SeatService seatService) {
        this.seatReservedService = seatReservedService;
        this.seatReservedMapper = seatReservedMapper;
        this.seatReservedDetailsMapper = seatReservedDetailsMapper;
        this.notificationAlertService = notificationAlertService;
        this.userRepository = userRepository;
        this.seatService = seatService;
    }

    @PostMapping()
    public ResponseEntity<SeatReservedDto> createSeatReservation(@Valid @RequestBody SeatReservedDto seatReservedDto)
        throws URISyntaxException {
        log.debug("REST request to save SeatReserved : {}", seatReservedDto);
        if (!seatReservedService.seatReservedDateValidation(seatReservedDto.getFromDate(), seatReservedDto.getToDate())) {
            return ResponseEntity
                .badRequest()
                .headers(createReservationFailureAlert(SeatReserved.class, " your reservation date is wrong, check if it's correct"))
                .body(seatReservedDto);
        } else if (!seatService.isSeatOccupiedOrRestristed(seatReservedDto.getSeatId(), seatReservedDto.getFromDate(), seatReservedDto.getToDate())) {
            return ResponseEntity
                .badRequest()
                .headers(createReservationFailureAlert(SeatReserved.class, " your Seat is already reserved or restricted"))
                .body(seatReservedDto);
        }
        SeatReserved createdSeatReservation = seatReservedService.save(seatReservedMapper.toEntity(seatReservedDto));
        SeatReservedDto result = seatReservedMapper.toDto(createdSeatReservation);

        seatReservedService.createReservation(result.getId());

        return ResponseEntity
            .created(new URI("/api/reserved-seats/" + result.getId()))
            .headers(createEntityCreationAlert(SeatReserved.class, result.getId().toString()))
            .body(result);
    }

    @GetMapping()
    public ResponseEntity<List<SeatReservedDetailsDto>> getReservedSeatsByCurrentUser(@ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of SeatReserveds for current user");
        Page<SeatReservedDetailsDto> page = seatReservedService.findAllByCurrentUser(pageable).map(seatReservedDetailsMapper::toDto);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/all")
    public ResponseEntity<List<SeatReservedDetailsDto>> getAllReservations(@ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of SeatReserveds for all users");
        Page<SeatReservedDetailsDto> page = seatReservedService.findAll(pageable).map(seatReservedDetailsMapper::toDto);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/pending")
    public ResponseEntity<List<SeatReservedDetailsDto>> getAllPendingReservations(@ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of SeatReserveds for all users");
        Page<SeatReservedDetailsDto> page = seatReservedService.findPending(pageable).map(seatReservedDetailsMapper::toDto);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeatReservedDetailsDto> getReservedSeat(@PathVariable Long id) {
        log.debug("REST request to get SeatReserved : {}", id);
        Optional<SeatReservedDetailsDto> seatReservedDetailsDto = seatReservedService.findOne(id).map(seatReservedDetailsMapper::toDto);
        return ResponseUtil.wrapOrNotFound(seatReservedDetailsDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservedSeat(@PathVariable Long id) {
        log.debug("REST request to delete SeatReserved : {}", id);
        seatReservedService.delete(id);

        return ResponseEntity
            .noContent()
            .headers(createEntityDeletionAlert(SeatReserved.class, id.toString()))
            .build();
    }

    @PatchMapping("/cancel/{id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {
        log.debug("REST request to cancel SeatReserved : {}", id);
        seatReservedService.cancelReservation(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/approve/{id}")
    public ResponseEntity<Void> approveReservation(@PathVariable Long id) {
        log.debug("REST request to approve SeatReserved : {}", id);
        seatReservedService.approveReservation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<SeatReservedDetailsDto>> getUpcomingReservations(@ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of upcoming seat reservations");
        Page<SeatReservedDetailsDto> page = seatReservedService.findUpcomingReservation(pageable).map(seatReservedDetailsMapper::toDto);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/recent")
    public ResponseEntity<List<SeatReservedDetailsDto>> getRecentReservations(@ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of recent seat reservations");
        Page<SeatReservedDetailsDto> page = seatReservedService.findRecentReservation(pageable).map(seatReservedDetailsMapper::toDto);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
