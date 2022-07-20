package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Asset.
 */
@Entity
@Table(name = "asset")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Asset implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "asset_id")
    private Integer assetId;

    @Column(name = "asset_serial")
    private String assetSerial;

    @Column(name = "asset_status")
    private Integer assetStatus;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Asset id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAssetId() {
        return this.assetId;
    }

    public Asset assetId(Integer assetId) {
        this.setAssetId(assetId);
        return this;
    }

    public void setAssetId(Integer assetId) {
        this.assetId = assetId;
    }

    public String getAssetSerial() {
        return this.assetSerial;
    }

    public Asset assetSerial(String assetSerial) {
        this.setAssetSerial(assetSerial);
        return this;
    }

    public void setAssetSerial(String assetSerial) {
        this.assetSerial = assetSerial;
    }

    public Integer getAssetStatus() {
        return this.assetStatus;
    }

    public Asset assetStatus(Integer assetStatus) {
        this.setAssetStatus(assetStatus);
        return this;
    }

    public void setAssetStatus(Integer assetStatus) {
        this.assetStatus = assetStatus;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Asset)) {
            return false;
        }
        return id != null && id.equals(((Asset) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Asset{" +
            "id=" + getId() +
            ", assetId=" + getAssetId() +
            ", assetSerial='" + getAssetSerial() + "'" +
            ", assetStatus=" + getAssetStatus() +
            "}";
    }
}
