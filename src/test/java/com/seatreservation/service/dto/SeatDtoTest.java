package com.seatreservation.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.seatreservation.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SeatDtoTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SeatDto.class);
        SeatDto seatDto1 = new SeatDto();
        seatDto1.setId(1L);
        SeatDto seatDto2 = new SeatDto();
        assertThat(seatDto1).isNotEqualTo(seatDto2);
        seatDto2.setId(seatDto1.getId());
        assertThat(seatDto1).isEqualTo(seatDto2);
        seatDto2.setId(2L);
        assertThat(seatDto1).isNotEqualTo(seatDto2);
        seatDto1.setId(null);
        assertThat(seatDto1).isNotEqualTo(seatDto2);
    }
}
