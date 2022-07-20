package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.AssetStatus} entity.
 */
public class AssetStatusDTO implements Serializable {

    private Long id;

    private Integer assetStatusId;

    private String assetStatusName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAssetStatusId() {
        return assetStatusId;
    }

    public void setAssetStatusId(Integer assetStatusId) {
        this.assetStatusId = assetStatusId;
    }

    public String getAssetStatusName() {
        return assetStatusName;
    }

    public void setAssetStatusName(String assetStatusName) {
        this.assetStatusName = assetStatusName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssetStatusDTO)) {
            return false;
        }

        AssetStatusDTO assetStatusDTO = (AssetStatusDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, assetStatusDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetStatusDTO{" +
            "id=" + getId() +
            ", assetStatusId=" + getAssetStatusId() +
            ", assetStatusName='" + getAssetStatusName() + "'" +
            "}";
    }
}
