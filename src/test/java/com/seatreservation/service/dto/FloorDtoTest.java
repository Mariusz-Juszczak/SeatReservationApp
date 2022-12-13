package com.seatreservation.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.seatreservation.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FloorDtoTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FloorDto.class);
        FloorDto floorDto1 = new FloorDto();
        floorDto1.setId(1L);
        FloorDto floorDto2 = new FloorDto();
        assertThat(floorDto1).isNotEqualTo(floorDto2);
        floorDto2.setId(floorDto1.getId());
        assertThat(floorDto1).isEqualTo(floorDto2);
        floorDto2.setId(2L);
        assertThat(floorDto1).isNotEqualTo(floorDto2);
        floorDto1.setId(null);
        assertThat(floorDto1).isNotEqualTo(floorDto2);
    }
}
