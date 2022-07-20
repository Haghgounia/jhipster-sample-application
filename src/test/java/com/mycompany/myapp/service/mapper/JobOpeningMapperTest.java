package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JobOpeningMapperTest {

    private JobOpeningMapper jobOpeningMapper;

    @BeforeEach
    public void setUp() {
        jobOpeningMapper = new JobOpeningMapperImpl();
    }
}
