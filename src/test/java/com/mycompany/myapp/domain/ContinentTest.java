package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContinentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Continent.class);
        Continent continent1 = new Continent();
        continent1.setId(1L);
        Continent continent2 = new Continent();
        continent2.setId(continent1.getId());
        assertThat(continent1).isEqualTo(continent2);
        continent2.setId(2L);
        assertThat(continent1).isNotEqualTo(continent2);
        continent1.setId(null);
        assertThat(continent1).isNotEqualTo(continent2);
    }
}
