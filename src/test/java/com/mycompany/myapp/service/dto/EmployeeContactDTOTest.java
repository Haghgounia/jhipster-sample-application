package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmployeeContactDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeeContactDTO.class);
        EmployeeContactDTO employeeContactDTO1 = new EmployeeContactDTO();
        employeeContactDTO1.setId(1L);
        EmployeeContactDTO employeeContactDTO2 = new EmployeeContactDTO();
        assertThat(employeeContactDTO1).isNotEqualTo(employeeContactDTO2);
        employeeContactDTO2.setId(employeeContactDTO1.getId());
        assertThat(employeeContactDTO1).isEqualTo(employeeContactDTO2);
        employeeContactDTO2.setId(2L);
        assertThat(employeeContactDTO1).isNotEqualTo(employeeContactDTO2);
        employeeContactDTO1.setId(null);
        assertThat(employeeContactDTO1).isNotEqualTo(employeeContactDTO2);
    }
}
