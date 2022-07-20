package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CountyDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CountyDTO.class);
        CountyDTO countyDTO1 = new CountyDTO();
        countyDTO1.setId(1L);
        CountyDTO countyDTO2 = new CountyDTO();
        assertThat(countyDTO1).isNotEqualTo(countyDTO2);
        countyDTO2.setId(countyDTO1.getId());
        assertThat(countyDTO1).isEqualTo(countyDTO2);
        countyDTO2.setId(2L);
        assertThat(countyDTO1).isNotEqualTo(countyDTO2);
        countyDTO1.setId(null);
        assertThat(countyDTO1).isNotEqualTo(countyDTO2);
    }
}
