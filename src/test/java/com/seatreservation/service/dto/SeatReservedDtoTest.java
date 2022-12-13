package com.seatreservation.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.seatreservation.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SeatReservedDtoTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SeatReservedDto.class);
        SeatReservedDto seatReservedDto1 = new SeatReservedDto();
        seatReservedDto1.setId(1L);
        SeatReservedDto seatReservedDto2 = new SeatReservedDto();
        assertThat(seatReservedDto1).isNotEqualTo(seatReservedDto2);
        seatReservedDto2.setId(seatReservedDto1.getId());
        assertThat(seatReservedDto1).isEqualTo(seatReservedDto2);
        seatReservedDto2.setId(2L);
        assertThat(seatReservedDto1).isNotEqualTo(seatReservedDto2);
        seatReservedDto1.setId(null);
        assertThat(seatReservedDto1).isNotEqualTo(seatReservedDto2);
    }
}
