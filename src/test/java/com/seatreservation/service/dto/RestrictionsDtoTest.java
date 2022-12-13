package com.seatreservation.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.seatreservation.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RestrictionsDtoTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RestrictionsDto.class);
        RestrictionsDto restrictionsDto1 = new RestrictionsDto();
        restrictionsDto1.setId(1L);
        RestrictionsDto restrictionsDto2 = new RestrictionsDto();
        assertThat(restrictionsDto1).isNotEqualTo(restrictionsDto2);
        restrictionsDto2.setId(restrictionsDto1.getId());
        assertThat(restrictionsDto1).isEqualTo(restrictionsDto2);
        restrictionsDto2.setId(2L);
        assertThat(restrictionsDto1).isNotEqualTo(restrictionsDto2);
        restrictionsDto1.setId(null);
        assertThat(restrictionsDto1).isNotEqualTo(restrictionsDto2);
    }
}
