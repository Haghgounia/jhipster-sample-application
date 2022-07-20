package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AssetStatus.
 */
@Entity
@Table(name = "asset_status")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AssetStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "asset_status_id")
    private Integer assetStatusId;

    @Column(name = "asset_status_name")
    private String assetStatusName;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AssetStatus id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAssetStatusId() {
        return this.assetStatusId;
    }

    public AssetStatus assetStatusId(Integer assetStatusId) {
        this.setAssetStatusId(assetStatusId);
        return this;
    }

    public void setAssetStatusId(Integer assetStatusId) {
        this.assetStatusId = assetStatusId;
    }

    public String getAssetStatusName() {
        return this.assetStatusName;
    }

    public AssetStatus assetStatusName(String assetStatusName) {
        this.setAssetStatusName(assetStatusName);
        return this;
    }

    public void setAssetStatusName(String assetStatusName) {
        this.assetStatusName = assetStatusName;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssetStatus)) {
            return false;
        }
        return id != null && id.equals(((AssetStatus) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetStatus{" +
            "id=" + getId() +
            ", assetStatusId=" + getAssetStatusId() +
            ", assetStatusName='" + getAssetStatusName() + "'" +
            "}";
    }
}
