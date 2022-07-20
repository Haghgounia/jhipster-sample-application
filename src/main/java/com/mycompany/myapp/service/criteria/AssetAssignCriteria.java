package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.AssetAssign} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.AssetAssignResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /asset-assigns?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class AssetAssignCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter assetId;

    private IntegerFilter employeeId;

    private IntegerFilter assignDate;

    private IntegerFilter returnDate;

    private Boolean distinct;

    public AssetAssignCriteria() {}

    public AssetAssignCriteria(AssetAssignCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.assetId = other.assetId == null ? null : other.assetId.copy();
        this.employeeId = other.employeeId == null ? null : other.employeeId.copy();
        this.assignDate = other.assignDate == null ? null : other.assignDate.copy();
        this.returnDate = other.returnDate == null ? null : other.returnDate.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AssetAssignCriteria copy() {
        return new AssetAssignCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getAssetId() {
        return assetId;
    }

    public IntegerFilter assetId() {
        if (assetId == null) {
            assetId = new IntegerFilter();
        }
        return assetId;
    }

    public void setAssetId(IntegerFilter assetId) {
        this.assetId = assetId;
    }

    public IntegerFilter getEmployeeId() {
        return employeeId;
    }

    public IntegerFilter employeeId() {
        if (employeeId == null) {
            employeeId = new IntegerFilter();
        }
        return employeeId;
    }

    public void setEmployeeId(IntegerFilter employeeId) {
        this.employeeId = employeeId;
    }

    public IntegerFilter getAssignDate() {
        return assignDate;
    }

    public IntegerFilter assignDate() {
        if (assignDate == null) {
            assignDate = new IntegerFilter();
        }
        return assignDate;
    }

    public void setAssignDate(IntegerFilter assignDate) {
        this.assignDate = assignDate;
    }

    public IntegerFilter getReturnDate() {
        return returnDate;
    }

    public IntegerFilter returnDate() {
        if (returnDate == null) {
            returnDate = new IntegerFilter();
        }
        return returnDate;
    }

    public void setReturnDate(IntegerFilter returnDate) {
        this.returnDate = returnDate;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AssetAssignCriteria that = (AssetAssignCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(assetId, that.assetId) &&
            Objects.equals(employeeId, that.employeeId) &&
            Objects.equals(assignDate, that.assignDate) &&
            Objects.equals(returnDate, that.returnDate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, assetId, employeeId, assignDate, returnDate, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetAssignCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (assetId != null ? "assetId=" + assetId + ", " : "") +
            (employeeId != null ? "employeeId=" + employeeId + ", " : "") +
            (assignDate != null ? "assignDate=" + assignDate + ", " : "") +
            (returnDate != null ? "returnDate=" + returnDate + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
