package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CandidateMapperTest {

    private CandidateMapper candidateMapper;

    @BeforeEach
    public void setUp() {
        candidateMapper = new CandidateMapperImpl();
    }
}
