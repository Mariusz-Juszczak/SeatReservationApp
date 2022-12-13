package com.seatreservation.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;

class DimensionsMapperTest {

    private DimensionsMapper dimensionsMapper;

    @BeforeEach
    public void setUp() {
        dimensionsMapper = new DimensionsMapperImpl();
    }
}
