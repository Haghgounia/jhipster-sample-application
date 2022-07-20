package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContinentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContinentDTO.class);
        ContinentDTO continentDTO1 = new ContinentDTO();
        continentDTO1.setId(1L);
        ContinentDTO continentDTO2 = new ContinentDTO();
        assertThat(continentDTO1).isNotEqualTo(continentDTO2);
        continentDTO2.setId(continentDTO1.getId());
        assertThat(continentDTO1).isEqualTo(continentDTO2);
        continentDTO2.setId(2L);
        assertThat(continentDTO1).isNotEqualTo(continentDTO2);
        continentDTO1.setId(null);
        assertThat(continentDTO1).isNotEqualTo(continentDTO2);
    }
}
