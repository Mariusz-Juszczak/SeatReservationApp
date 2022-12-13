package com.seatreservation.web.rest;

import com.seatreservation.domain.Seat;
import com.seatreservation.service.ReservationService;
import com.seatreservation.service.SeatService;
import com.seatreservation.service.dto.SeatDto;
import com.seatreservation.service.mapper.SeatMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
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
 * REST controller for managing {@link Seat}.
 */
@RestController
@RequestMapping("/api/seats")
public class SeatController {

    private final Logger log = LoggerFactory.getLogger(SeatController.class);
    private final SeatService seatService;
    private final SeatMapper seatMapper;

    private final ReservationService reservationService;

    public SeatController(SeatService seatService, SeatMapper seatMapper, ReservationService reservationService) {
        this.seatMapper = seatMapper;
        this.seatService = seatService;
        this.reservationService = reservationService;
    }

    @PostMapping()
    public ResponseEntity<SeatDto> createSeat(@Valid @RequestBody SeatDto seatDto) throws URISyntaxException {
        log.debug("REST request to save Seat : {}", seatDto);
        Seat createdSeat = seatService.save(seatMapper.toEntity(seatDto));
        SeatDto result = seatMapper.toDto(createdSeat);
        return ResponseEntity.created(new URI("/api/seats/" + result.getId())).headers(createEntityCreationAlert(Seat.class, result.getId().toString())).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SeatDto> updateSeat(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody SeatDto seatDto) throws URISyntaxException {
        log.debug("REST request to update Seat : {}, {}", id, seatDto);
        Seat createdSeat = seatService.update(seatMapper.toEntity(seatDto), id);
        SeatDto result = seatMapper.toDto(createdSeat);
        return ResponseEntity.ok().headers(createEntityUpdateAlert(Seat.class, seatDto.getId().toString())).body(result);
    }

    @GetMapping()
    public ResponseEntity<List<SeatDto>> getAllSeats(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Seats");
        Page<SeatDto> page = seatService.findAll(pageable).map(seatMapper::toDto);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeatDto> getSeat(@PathVariable Long id) {
        log.debug("REST request to get Seat : {}", id);
        Optional<SeatDto> seatDto = seatService.findOne(id).map(seatMapper::toDto);
        return ResponseUtil.wrapOrNotFound(seatDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeat(@PathVariable Long id) {
        log.debug("REST request to delete Seat : {}", id);
        seatService.delete(id);
        return ResponseEntity.noContent().headers(createEntityDeletionAlert(Seat.class, id.toString())).build();
    }

    @GetMapping("/rooms/{id}")
    public ResponseEntity<List<SeatDto>> getAllSeatsByRoom(@ParameterObject Pageable pageable,
                                                           @PathVariable("id") Long roomId,
                                                           @RequestParam(name = "from_date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime fromDate,
                                                           @RequestParam(name = "to_date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime toDate) {
        log.debug("REST request to get Seats by Room : {}", roomId);
        Page<SeatDto> page = seatService.findAllByRoom(pageable, roomId).map(seatMapper::toDto);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/floors/{id}")
    public ResponseEntity<List<SeatDto>> getAllSeatsByFloor(@PathVariable("id") Long floorId,
                                                            @RequestParam(name = "from_date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime fromDate,
                                                            @RequestParam(name = "to_date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime toDate) {
        log.debug("REST request to get Seats by Floor : {}", floorId);
        List<SeatDto> result = seatService.getSeatsDetailsPerFloorInGivenPeriodOfTim(floorId, fromDate,toDate);
        return ResponseEntity.ok().body(result);
    }
}
