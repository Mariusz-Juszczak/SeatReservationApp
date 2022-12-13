package com.seatreservation.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.seatreservation.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CoordinatesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Coordinates.class);
        Coordinates coordinates1 = new Coordinates();
        coordinates1.setId(1L);
        Coordinates coordinates2 = new Coordinates();
        coordinates2.setId(coordinates1.getId());
        assertThat(coordinates1).isEqualTo(coordinates2);
        coordinates2.setId(2L);
        assertThat(coordinates1).isNotEqualTo(coordinates2);
        coordinates1.setId(null);
        assertThat(coordinates1).isNotEqualTo(coordinates2);
    }
}
