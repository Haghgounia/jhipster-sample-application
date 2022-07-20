package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AssetAssignTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetAssign.class);
        AssetAssign assetAssign1 = new AssetAssign();
        assetAssign1.setId(1L);
        AssetAssign assetAssign2 = new AssetAssign();
        assetAssign2.setId(assetAssign1.getId());
        assertThat(assetAssign1).isEqualTo(assetAssign2);
        assetAssign2.setId(2L);
        assertThat(assetAssign1).isNotEqualTo(assetAssign2);
        assetAssign1.setId(null);
        assertThat(assetAssign1).isNotEqualTo(assetAssign2);
    }
}
