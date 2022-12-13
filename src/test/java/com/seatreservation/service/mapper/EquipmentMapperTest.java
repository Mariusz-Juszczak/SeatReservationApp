package com.seatreservation.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;

class EquipmentMapperTest {

    private EquipmentMapper equipmentMapper;

    @BeforeEach
    public void setUp() {
        equipmentMapper = new EquipmentMapperImpl();
    }
}
