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
 * Criteria class for the {@link com.mycompany.myapp.domain.AssetStatus} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.AssetStatusResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /asset-statuses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class AssetStatusCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter assetStatusId;

    private StringFilter assetStatusName;

    private Boolean distinct;

    public AssetStatusCriteria() {}

    public AssetStatusCriteria(AssetStatusCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.assetStatusId = other.assetStatusId == null ? null : other.assetStatusId.copy();
        this.assetStatusName = other.assetStatusName == null ? null : other.assetStatusName.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AssetStatusCriteria copy() {
        return new AssetStatusCriteria(this);
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

    public IntegerFilter getAssetStatusId() {
        return assetStatusId;
    }

    public IntegerFilter assetStatusId() {
        if (assetStatusId == null) {
            assetStatusId = new IntegerFilter();
        }
        return assetStatusId;
    }

    public void setAssetStatusId(IntegerFilter assetStatusId) {
        this.assetStatusId = assetStatusId;
    }

    public StringFilter getAssetStatusName() {
        return assetStatusName;
    }

    public StringFilter assetStatusName() {
        if (assetStatusName == null) {
            assetStatusName = new StringFilter();
        }
        return assetStatusName;
    }

    public void setAssetStatusName(StringFilter assetStatusName) {
        this.assetStatusName = assetStatusName;
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
        final AssetStatusCriteria that = (AssetStatusCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(assetStatusId, that.assetStatusId) &&
            Objects.equals(assetStatusName, that.assetStatusName) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, assetStatusId, assetStatusName, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetStatusCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (assetStatusId != null ? "assetStatusId=" + assetStatusId + ", " : "") +
            (assetStatusName != null ? "assetStatusName=" + assetStatusName + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
