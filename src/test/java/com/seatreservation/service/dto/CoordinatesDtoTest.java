package com.seatreservation.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.seatreservation.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CoordinatesDtoTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CoordinatesDto.class);
        CoordinatesDto coordinatesDto1 = new CoordinatesDto();
        coordinatesDto1.setId(1L);
        CoordinatesDto coordinatesDto2 = new CoordinatesDto();
        assertThat(coordinatesDto1).isNotEqualTo(coordinatesDto2);
        coordinatesDto2.setId(coordinatesDto1.getId());
        assertThat(coordinatesDto1).isEqualTo(coordinatesDto2);
        coordinatesDto2.setId(2L);
        assertThat(coordinatesDto1).isNotEqualTo(coordinatesDto2);
        coordinatesDto1.setId(null);
        assertThat(coordinatesDto1).isNotEqualTo(coordinatesDto2);
    }
}
