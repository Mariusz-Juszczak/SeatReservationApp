package com.seatreservation.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;

class RoomMapperTest {

    private RoomMapper roomMapper;

    @BeforeEach
    public void setUp() {
        roomMapper = new RoomMapperImpl();
    }
}
