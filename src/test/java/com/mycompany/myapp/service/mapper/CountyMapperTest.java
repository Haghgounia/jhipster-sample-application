package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CountyMapperTest {

    private CountyMapper countyMapper;

    @BeforeEach
    public void setUp() {
        countyMapper = new CountyMapperImpl();
    }
}
