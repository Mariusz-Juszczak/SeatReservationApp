package com.seatreservation.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.seatreservation.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DimensionsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Dimensions.class);
        Dimensions dimensions1 = new Dimensions();
        dimensions1.setId(1L);
        Dimensions dimensions2 = new Dimensions();
        dimensions2.setId(dimensions1.getId());
        assertThat(dimensions1).isEqualTo(dimensions2);
        dimensions2.setId(2L);
        assertThat(dimensions1).isNotEqualTo(dimensions2);
        dimensions1.setId(null);
        assertThat(dimensions1).isNotEqualTo(dimensions2);
    }
}
