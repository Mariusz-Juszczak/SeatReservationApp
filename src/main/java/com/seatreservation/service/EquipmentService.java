package com.seatreservation.service;

import com.seatreservation.domain.Equipment;
import com.seatreservation.repository.EquipmentRepository;

import java.util.Objects;
import java.util.Optional;

import com.seatreservation.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EquipmentService {

    private final Logger log = LoggerFactory.getLogger(EquipmentService.class);

    private final EquipmentRepository equipmentRepository;

    public EquipmentService(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    public Equipment save(Equipment equipment) {
        if (equipment.getId() != null) {
            throw new BadRequestAlertException("A new equipment cannot already have an ID", Equipment.class, "id exists");
        }
        log.debug("Request to save Equipment : {}", equipment);
        return equipmentRepository.save(equipment);
    }

    public Equipment update(Equipment equipment, Long id) {
        if (equipment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", Equipment.class, "id null");
        }
        if (!Objects.equals(id, equipment.getId())) {
            throw new BadRequestAlertException("Invalid ID", Equipment.class, "id invalid");
        }

        if (!equipmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", Equipment.class, "id not found");
        }
        log.debug("Request to save Equipment : {}", equipment);
        return equipmentRepository.save(equipment);
    }

    @Transactional(readOnly = true)
    public Page<Equipment> findAll(Pageable pageable) {
        log.debug("Request to get all Equipment");
        return equipmentRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Equipment> findOne(Long id) {
        log.debug("Request to get Equipment : {}", id);
        return equipmentRepository.findById(id);
    }

    public void delete(Long id) {
        log.debug("Request to delete Equipment : {}", id);
        equipmentRepository.deleteById(id);
    }
}
