package com.seatreservation.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.seatreservation.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AddressDtoTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AddressDto.class);
        AddressDto addressDto1 = new AddressDto();
        addressDto1.setId(1L);
        AddressDto addressDto2 = new AddressDto();
        assertThat(addressDto1).isNotEqualTo(addressDto2);
        addressDto2.setId(addressDto1.getId());
        assertThat(addressDto1).isEqualTo(addressDto2);
        addressDto2.setId(2L);
        assertThat(addressDto1).isNotEqualTo(addressDto2);
        addressDto1.setId(null);
        assertThat(addressDto1).isNotEqualTo(addressDto2);
    }
}
