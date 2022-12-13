package com.seatreservation.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.seatreservation.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DimensionsDtoTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DimensionsDto.class);
        DimensionsDto dimensionsDto1 = new DimensionsDto();
        dimensionsDto1.setId(1L);
        DimensionsDto dimensionsDto2 = new DimensionsDto();
        assertThat(dimensionsDto1).isNotEqualTo(dimensionsDto2);
        dimensionsDto2.setId(dimensionsDto1.getId());
        assertThat(dimensionsDto1).isEqualTo(dimensionsDto2);
        dimensionsDto2.setId(2L);
        assertThat(dimensionsDto1).isNotEqualTo(dimensionsDto2);
        dimensionsDto1.setId(null);
        assertThat(dimensionsDto1).isNotEqualTo(dimensionsDto2);
    }
}
