package com.seatreservation.web.rest;

import com.seatreservation.domain.Room;
import com.seatreservation.service.RoomService;
import com.seatreservation.service.dto.RoomDto;
import com.seatreservation.service.mapper.RoomMapper;
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
 * REST controller for managing {@link Room}.
 */
@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final Logger log = LoggerFactory.getLogger(RoomController.class);
    private final RoomService roomService;
    private final RoomMapper roomMapper;

    public RoomController(RoomService roomService, RoomMapper roomMapper) {
        this.roomService = roomService;
        this.roomMapper = roomMapper;

    }

    @PostMapping()
    public ResponseEntity<RoomDto> createRoom(@Valid @RequestBody RoomDto roomDto) throws URISyntaxException {
        log.debug("REST request to save Room : {}", roomDto);
        Room createdRoom = roomService.save(roomMapper.toEntity(roomDto));
        RoomDto result = roomMapper.toDto(createdRoom);
        return ResponseEntity.created(new URI("/api/rooms/" + result.getId())).headers(createEntityCreationAlert(Room.class, result.getId().toString())).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomDto> updateRoom(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody RoomDto roomDto) throws URISyntaxException {
        log.debug("REST request to update Room : {}, {}", id, roomDto);
        Room createdRoom = roomService.update(roomMapper.toEntity(roomDto), id);
        RoomDto result = roomMapper.toDto(createdRoom);
        return ResponseEntity.ok().headers(createEntityUpdateAlert(Room.class, roomDto.getId().toString())).body(result);
    }

    @GetMapping()
    public ResponseEntity<List<RoomDto>> getAllRooms(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Rooms");
        Page<RoomDto> page = roomService.findAll(pageable).map(roomMapper::toDto);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomDto> getRoom(@PathVariable Long id) {
        log.debug("REST request to get Room : {}", id);
        Optional<RoomDto> roomDto = roomService.findOne(id).map(roomMapper::toDto);
        return ResponseUtil.wrapOrNotFound(roomDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        log.debug("REST request to delete Room : {}", id);
        roomService.delete(id);
        return ResponseEntity.noContent().headers(createEntityDeletionAlert(Room.class, id.toString())).build();
    }

    @GetMapping("/floors/{id}")
    public ResponseEntity<List<RoomDto>> getAllRoomsByFloor(@ParameterObject Pageable pageable, @PathVariable("id") Long floorId) {
        log.debug("REST request to get a page of Rooms by Floor: {}", floorId);
        Page<RoomDto> page = roomService.findAllByFloor(pageable, floorId).map(roomMapper::toDto);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
