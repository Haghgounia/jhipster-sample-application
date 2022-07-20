package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AssetStatusTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetStatus.class);
        AssetStatus assetStatus1 = new AssetStatus();
        assetStatus1.setId(1L);
        AssetStatus assetStatus2 = new AssetStatus();
        assetStatus2.setId(assetStatus1.getId());
        assertThat(assetStatus1).isEqualTo(assetStatus2);
        assetStatus2.setId(2L);
        assertThat(assetStatus1).isNotEqualTo(assetStatus2);
        assetStatus1.setId(null);
        assertThat(assetStatus1).isNotEqualTo(assetStatus2);
    }
}
