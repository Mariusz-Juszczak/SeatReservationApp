package com.seatreservation.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.seatreservation.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RoomDtoTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoomDto.class);
        RoomDto roomDto1 = new RoomDto();
        roomDto1.setId(1L);
        RoomDto roomDto2 = new RoomDto();
        assertThat(roomDto1).isNotEqualTo(roomDto2);
        roomDto2.setId(roomDto1.getId());
        assertThat(roomDto1).isEqualTo(roomDto2);
        roomDto2.setId(2L);
        assertThat(roomDto1).isNotEqualTo(roomDto2);
        roomDto1.setId(null);
        assertThat(roomDto1).isNotEqualTo(roomDto2);
    }
}
