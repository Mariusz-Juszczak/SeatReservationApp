package com.seatreservation.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;

class SeatReservedMapperTest {

    private SeatReservedMapper seatReservedMapper;

    @BeforeEach
    public void setUp() {
        seatReservedMapper = new SeatReservedMapperImpl();
    }
}
