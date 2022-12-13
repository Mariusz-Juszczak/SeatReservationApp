package com.seatreservation.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.seatreservation.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RestrictionsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Restrictions.class);
        Restrictions restrictions1 = new Restrictions();
        restrictions1.setId(1L);
        Restrictions restrictions2 = new Restrictions();
        restrictions2.setId(restrictions1.getId());
        assertThat(restrictions1).isEqualTo(restrictions2);
        restrictions2.setId(2L);
        assertThat(restrictions1).isNotEqualTo(restrictions2);
        restrictions1.setId(null);
        assertThat(restrictions1).isNotEqualTo(restrictions2);
    }
}
