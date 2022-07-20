package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.AssetAssign} entity.
 */
public class AssetAssignDTO implements Serializable {

    private Long id;

    private Integer assetId;

    private Integer employeeId;

    private Integer assignDate;

    private Integer returnDate;

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

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getAssignDate() {
        return assignDate;
    }

    public void setAssignDate(Integer assignDate) {
        this.assignDate = assignDate;
    }

    public Integer getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Integer returnDate) {
        this.returnDate = returnDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssetAssignDTO)) {
            return false;
        }

        AssetAssignDTO assetAssignDTO = (AssetAssignDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, assetAssignDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetAssignDTO{" +
            "id=" + getId() +
            ", assetId=" + getAssetId() +
            ", employeeId=" + getEmployeeId() +
            ", assignDate=" + getAssignDate() +
            ", returnDate=" + getReturnDate() +
            "}";
    }
}
