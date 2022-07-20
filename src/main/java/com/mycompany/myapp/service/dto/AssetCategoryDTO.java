package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.AssetCategory} entity.
 */
public class AssetCategoryDTO implements Serializable {

    private Long id;

    private Integer assetCategoryId;

    private String assetCategoryName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAssetCategoryId() {
        return assetCategoryId;
    }

    public void setAssetCategoryId(Integer assetCategoryId) {
        this.assetCategoryId = assetCategoryId;
    }

    public String getAssetCategoryName() {
        return assetCategoryName;
    }

    public void setAssetCategoryName(String assetCategoryName) {
        this.assetCategoryName = assetCategoryName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssetCategoryDTO)) {
            return false;
        }

        AssetCategoryDTO assetCategoryDTO = (AssetCategoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, assetCategoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetCategoryDTO{" +
            "id=" + getId() +
            ", assetCategoryId=" + getAssetCategoryId() +
            ", assetCategoryName='" + getAssetCategoryName() + "'" +
            "}";
    }
}
