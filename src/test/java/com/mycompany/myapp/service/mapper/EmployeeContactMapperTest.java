package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmployeeContactMapperTest {

    private EmployeeContactMapper employeeContactMapper;

    @BeforeEach
    public void setUp() {
        employeeContactMapper = new EmployeeContactMapperImpl();
    }
}
