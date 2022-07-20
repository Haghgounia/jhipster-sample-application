package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AssetAssignDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetAssignDTO.class);
        AssetAssignDTO assetAssignDTO1 = new AssetAssignDTO();
        assetAssignDTO1.setId(1L);
        AssetAssignDTO assetAssignDTO2 = new AssetAssignDTO();
        assertThat(assetAssignDTO1).isNotEqualTo(assetAssignDTO2);
        assetAssignDTO2.setId(assetAssignDTO1.getId());
        assertThat(assetAssignDTO1).isEqualTo(assetAssignDTO2);
        assetAssignDTO2.setId(2L);
        assertThat(assetAssignDTO1).isNotEqualTo(assetAssignDTO2);
        assetAssignDTO1.setId(null);
        assertThat(assetAssignDTO1).isNotEqualTo(assetAssignDTO2);
    }
}
