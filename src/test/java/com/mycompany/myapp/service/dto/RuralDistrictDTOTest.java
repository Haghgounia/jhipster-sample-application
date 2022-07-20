package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RuralDistrictDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RuralDistrictDTO.class);
        RuralDistrictDTO ruralDistrictDTO1 = new RuralDistrictDTO();
        ruralDistrictDTO1.setId(1L);
        RuralDistrictDTO ruralDistrictDTO2 = new RuralDistrictDTO();
        assertThat(ruralDistrictDTO1).isNotEqualTo(ruralDistrictDTO2);
        ruralDistrictDTO2.setId(ruralDistrictDTO1.getId());
        assertThat(ruralDistrictDTO1).isEqualTo(ruralDistrictDTO2);
        ruralDistrictDTO2.setId(2L);
        assertThat(ruralDistrictDTO1).isNotEqualTo(ruralDistrictDTO2);
        ruralDistrictDTO1.setId(null);
        assertThat(ruralDistrictDTO1).isNotEqualTo(ruralDistrictDTO2);
    }
}
