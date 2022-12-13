package com.seatreservation.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;

class FloorMapperTest {

    private FloorMapper floorMapper;

    @BeforeEach
    public void setUp() {
        floorMapper = new FloorMapperImpl();
    }
}
