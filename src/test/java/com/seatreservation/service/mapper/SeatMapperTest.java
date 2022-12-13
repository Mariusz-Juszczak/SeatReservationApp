package com.seatreservation.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;

class SeatMapperTest {

    private SeatMapper seatMapper;

    @BeforeEach
    public void setUp() {
        seatMapper = new SeatMapperImpl();
    }
}
