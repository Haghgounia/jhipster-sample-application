package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class JobOpeningDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobOpeningDTO.class);
        JobOpeningDTO jobOpeningDTO1 = new JobOpeningDTO();
        jobOpeningDTO1.setId(1L);
        JobOpeningDTO jobOpeningDTO2 = new JobOpeningDTO();
        assertThat(jobOpeningDTO1).isNotEqualTo(jobOpeningDTO2);
        jobOpeningDTO2.setId(jobOpeningDTO1.getId());
        assertThat(jobOpeningDTO1).isEqualTo(jobOpeningDTO2);
        jobOpeningDTO2.setId(2L);
        assertThat(jobOpeningDTO1).isNotEqualTo(jobOpeningDTO2);
        jobOpeningDTO1.setId(null);
        assertThat(jobOpeningDTO1).isNotEqualTo(jobOpeningDTO2);
    }
}
