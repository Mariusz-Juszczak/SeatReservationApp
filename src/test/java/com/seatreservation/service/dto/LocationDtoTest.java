package com.seatreservation.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.seatreservation.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LocationDtoTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LocationDto.class);
        LocationDto locationDto1 = new LocationDto();
        locationDto1.setId(1L);
        LocationDto locationDto2 = new LocationDto();
        assertThat(locationDto1).isNotEqualTo(locationDto2);
        locationDto2.setId(locationDto1.getId());
        assertThat(locationDto1).isEqualTo(locationDto2);
        locationDto2.setId(2L);
        assertThat(locationDto1).isNotEqualTo(locationDto2);
        locationDto1.setId(null);
        assertThat(locationDto1).isNotEqualTo(locationDto2);
    }
}
