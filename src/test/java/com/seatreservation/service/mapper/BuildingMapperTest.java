package com.seatreservation.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;

class BuildingMapperTest {

    private BuildingMapper buildingMapper;

    @BeforeEach
    public void setUp() {
        buildingMapper = new BuildingMapperImpl();
    }
}
