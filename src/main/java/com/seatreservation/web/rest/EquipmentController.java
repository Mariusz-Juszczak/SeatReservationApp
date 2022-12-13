package com.seatreservation.web.rest;

import com.seatreservation.domain.Equipment;
import com.seatreservation.service.EquipmentService;
import com.seatreservation.service.dto.EquipmentDto;
import com.seatreservation.service.mapper.EquipmentMapper;
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
 * REST controller for managing {@link Equipment}.
 */
@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {

    private final Logger log = LoggerFactory.getLogger(EquipmentController.class);
    private final EquipmentService equipmentService;
    private final EquipmentMapper equipmentMapper;

    public EquipmentController(EquipmentService equipmentService, EquipmentMapper equipmentMapper) {
        this.equipmentService = equipmentService;
        this.equipmentMapper = equipmentMapper;
    }

    @PostMapping()
    public ResponseEntity<EquipmentDto> createEquipment(@Valid @RequestBody EquipmentDto equipmentDto) throws URISyntaxException {
        log.debug("REST request to save Equipment : {}", equipmentDto);
        Equipment createdEquipment = equipmentService.save(equipmentMapper.toEntity(equipmentDto));
        EquipmentDto result = equipmentMapper.toDto(createdEquipment);
        return ResponseEntity.created(new URI("/api/equipment/" + result.getId())).headers(createEntityCreationAlert(Equipment.class, result.getId().toString())).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipmentDto> updateEquipment(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody EquipmentDto equipmentDto) throws URISyntaxException {
        log.debug("REST request to update Equipment : {}, {}", id, equipmentDto);
        Equipment createdEquipment = equipmentService.update(equipmentMapper.toEntity(equipmentDto), id);
        EquipmentDto result = equipmentMapper.toDto(createdEquipment);
        return ResponseEntity.ok().headers(createEntityUpdateAlert(Equipment.class, equipmentDto.getId().toString())).body(result);
    }

    @GetMapping()
    public ResponseEntity<List<EquipmentDto>> getAllEquipment(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Equipment");
        Page<EquipmentDto> page = equipmentService.findAll(pageable).map(equipmentMapper::toDto);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipmentDto> getEquipment(@PathVariable Long id) {
        log.debug("REST request to get Equipment : {}", id);
        Optional<EquipmentDto> equipmentDto = equipmentService.findOne(id).map(equipmentMapper::toDto);
        return ResponseUtil.wrapOrNotFound(equipmentDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipment(@PathVariable Long id) {
        log.debug("REST request to delete Equipment : {}", id);
        equipmentService.delete(id);
        return ResponseEntity.noContent().headers(createEntityDeletionAlert(Equipment.class, id.toString())).build();
    }
}
