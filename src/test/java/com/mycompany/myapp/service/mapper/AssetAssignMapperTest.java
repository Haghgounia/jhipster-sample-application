package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AssetAssignMapperTest {

    private AssetAssignMapper assetAssignMapper;

    @BeforeEach
    public void setUp() {
        assetAssignMapper = new AssetAssignMapperImpl();
    }
}
