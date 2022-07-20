package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RuralDistrictTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RuralDistrict.class);
        RuralDistrict ruralDistrict1 = new RuralDistrict();
        ruralDistrict1.setId(1L);
        RuralDistrict ruralDistrict2 = new RuralDistrict();
        ruralDistrict2.setId(ruralDistrict1.getId());
        assertThat(ruralDistrict1).isEqualTo(ruralDistrict2);
        ruralDistrict2.setId(2L);
        assertThat(ruralDistrict1).isNotEqualTo(ruralDistrict2);
        ruralDistrict1.setId(null);
        assertThat(ruralDistrict1).isNotEqualTo(ruralDistrict2);
    }
}
