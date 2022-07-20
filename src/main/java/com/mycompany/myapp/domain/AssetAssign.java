package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AssetAssign.
 */
@Entity
@Table(name = "asset_assign")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AssetAssign implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "asset_id")
    private Integer assetId;

    @Column(name = "employee_id")
    private Integer employeeId;

    @Column(name = "assign_date")
    private Integer assignDate;

    @Column(name = "return_date")
    private Integer returnDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AssetAssign id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAssetId() {
        return this.assetId;
    }

    public AssetAssign assetId(Integer assetId) {
        this.setAssetId(assetId);
        return this;
    }

    public void setAssetId(Integer assetId) {
        this.assetId = assetId;
    }

    public Integer getEmployeeId() {
        return this.employeeId;
    }

    public AssetAssign employeeId(Integer employeeId) {
        this.setEmployeeId(employeeId);
        return this;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getAssignDate() {
        return this.assignDate;
    }

    public AssetAssign assignDate(Integer assignDate) {
        this.setAssignDate(assignDate);
        return this;
    }

    public void setAssignDate(Integer assignDate) {
        this.assignDate = assignDate;
    }

    public Integer getReturnDate() {
        return this.returnDate;
    }

    public AssetAssign returnDate(Integer returnDate) {
        this.setReturnDate(returnDate);
        return this;
    }

    public void setReturnDate(Integer returnDate) {
        this.returnDate = returnDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssetAssign)) {
            return false;
        }
        return id != null && id.equals(((AssetAssign) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetAssign{" +
            "id=" + getId() +
            ", assetId=" + getAssetId() +
            ", employeeId=" + getEmployeeId() +
            ", assignDate=" + getAssignDate() +
            ", returnDate=" + getReturnDate() +
            "}";
    }
}
