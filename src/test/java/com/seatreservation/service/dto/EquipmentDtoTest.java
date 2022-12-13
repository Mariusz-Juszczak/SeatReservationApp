package com.seatreservation.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.seatreservation.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EquipmentDtoTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EquipmentDto.class);
        EquipmentDto equipmentDto1 = new EquipmentDto();
        equipmentDto1.setId(1L);
        EquipmentDto equipmentDto2 = new EquipmentDto();
        assertThat(equipmentDto1).isNotEqualTo(equipmentDto2);
        equipmentDto2.setId(equipmentDto1.getId());
        assertThat(equipmentDto1).isEqualTo(equipmentDto2);
        equipmentDto2.setId(2L);
        assertThat(equipmentDto1).isNotEqualTo(equipmentDto2);
        equipmentDto1.setId(null);
        assertThat(equipmentDto1).isNotEqualTo(equipmentDto2);
    }
}
