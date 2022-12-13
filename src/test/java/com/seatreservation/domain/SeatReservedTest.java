package com.seatreservation.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.seatreservation.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SeatReservedTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SeatReserved.class);
        SeatReserved seatReserved1 = new SeatReserved();
        seatReserved1.setId(1L);
        SeatReserved seatReserved2 = new SeatReserved();
        seatReserved2.setId(seatReserved1.getId());
        assertThat(seatReserved1).isEqualTo(seatReserved2);
        seatReserved2.setId(2L);
        assertThat(seatReserved1).isNotEqualTo(seatReserved2);
        seatReserved1.setId(null);
        assertThat(seatReserved1).isNotEqualTo(seatReserved2);
    }
}
