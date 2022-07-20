package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmployeeContactTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeeContact.class);
        EmployeeContact employeeContact1 = new EmployeeContact();
        employeeContact1.setId(1L);
        EmployeeContact employeeContact2 = new EmployeeContact();
        employeeContact2.setId(employeeContact1.getId());
        assertThat(employeeContact1).isEqualTo(employeeContact2);
        employeeContact2.setId(2L);
        assertThat(employeeContact1).isNotEqualTo(employeeContact2);
        employeeContact1.setId(null);
        assertThat(employeeContact1).isNotEqualTo(employeeContact2);
    }
}
