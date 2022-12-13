package com.seatreservation.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.seatreservation.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BuildingDtoTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BuildingDto.class);
        BuildingDto buildingDto1 = new BuildingDto();
        buildingDto1.setId(1L);
        BuildingDto buildingDto2 = new BuildingDto();
        assertThat(buildingDto1).isNotEqualTo(buildingDto2);
        buildingDto2.setId(buildingDto1.getId());
        assertThat(buildingDto1).isEqualTo(buildingDto2);
        buildingDto2.setId(2L);
        assertThat(buildingDto1).isNotEqualTo(buildingDto2);
        buildingDto1.setId(null);
        assertThat(buildingDto1).isNotEqualTo(buildingDto2);
    }
}
