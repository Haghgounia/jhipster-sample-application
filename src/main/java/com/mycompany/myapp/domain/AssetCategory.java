package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AssetCategory.
 */
@Entity
@Table(name = "asset_category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AssetCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "asset_category_id")
    private Integer assetCategoryId;

    @Column(name = "asset_category_name")
    private String assetCategoryName;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AssetCategory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAssetCategoryId() {
        return this.assetCategoryId;
    }

    public AssetCategory assetCategoryId(Integer assetCategoryId) {
        this.setAssetCategoryId(assetCategoryId);
        return this;
    }

    public void setAssetCategoryId(Integer assetCategoryId) {
        this.assetCategoryId = assetCategoryId;
    }

    public String getAssetCategoryName() {
        return this.assetCategoryName;
    }

    public AssetCategory assetCategoryName(String assetCategoryName) {
        this.setAssetCategoryName(assetCategoryName);
        return this;
    }

    public void setAssetCategoryName(String assetCategoryName) {
        this.assetCategoryName = assetCategoryName;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssetCategory)) {
            return false;
        }
        return id != null && id.equals(((AssetCategory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetCategory{" +
            "id=" + getId() +
            ", assetCategoryId=" + getAssetCategoryId() +
            ", assetCategoryName='" + getAssetCategoryName() + "'" +
            "}";
    }
}
