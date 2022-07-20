package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Asset} entity.
 */
public class AssetDTO implements Serializable {

    private Long id;

    private Integer assetId;

    private String assetSerial;

    private Integer assetStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAssetId() {
        return assetId;
    }

    public void setAssetId(Integer assetId) {
        this.assetId = assetId;
    }

    public String getAssetSerial() {
        return assetSerial;
    }

    public void setAssetSerial(String assetSerial) {
        this.assetSerial = assetSerial;
    }

    public Integer getAssetStatus() {
        return assetStatus;
    }

    public void setAssetStatus(Integer assetStatus) {
        this.assetStatus = assetStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssetDTO)) {
            return false;
        }

        AssetDTO assetDTO = (AssetDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, assetDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetDTO{" +
            "id=" + getId() +
            ", assetId=" + getAssetId() +
            ", assetSerial='" + getAssetSerial() + "'" +
            ", assetStatus=" + getAssetStatus() +
            "}";
    }
}
