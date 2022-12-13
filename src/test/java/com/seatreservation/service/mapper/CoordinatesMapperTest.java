package com.seatreservation.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;

class CoordinatesMapperTest {

    private CoordinatesMapper coordinatesMapper;

    @BeforeEach
    public void setUp() {
        coordinatesMapper = new CoordinatesMapperImpl();
    }
}
