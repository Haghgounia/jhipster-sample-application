package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AssetStatusDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetStatusDTO.class);
        AssetStatusDTO assetStatusDTO1 = new AssetStatusDTO();
        assetStatusDTO1.setId(1L);
        AssetStatusDTO assetStatusDTO2 = new AssetStatusDTO();
        assertThat(assetStatusDTO1).isNotEqualTo(assetStatusDTO2);
        assetStatusDTO2.setId(assetStatusDTO1.getId());
        assertThat(assetStatusDTO1).isEqualTo(assetStatusDTO2);
        assetStatusDTO2.setId(2L);
        assertThat(assetStatusDTO1).isNotEqualTo(assetStatusDTO2);
        assetStatusDTO1.setId(null);
        assertThat(assetStatusDTO1).isNotEqualTo(assetStatusDTO2);
    }
}
