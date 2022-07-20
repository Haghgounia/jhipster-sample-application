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
 * Criteria class for the {@link com.mycompany.myapp.domain.Asset} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.AssetResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /assets?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class AssetCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter assetId;

    private StringFilter assetSerial;

    private IntegerFilter assetStatus;

    private Boolean distinct;

    public AssetCriteria() {}

    public AssetCriteria(AssetCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.assetId = other.assetId == null ? null : other.assetId.copy();
        this.assetSerial = other.assetSerial == null ? null : other.assetSerial.copy();
        this.assetStatus = other.assetStatus == null ? null : other.assetStatus.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AssetCriteria copy() {
        return new AssetCriteria(this);
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

    public StringFilter getAssetSerial() {
        return assetSerial;
    }

    public StringFilter assetSerial() {
        if (assetSerial == null) {
            assetSerial = new StringFilter();
        }
        return assetSerial;
    }

    public void setAssetSerial(StringFilter assetSerial) {
        this.assetSerial = assetSerial;
    }

    public IntegerFilter getAssetStatus() {
        return assetStatus;
    }

    public IntegerFilter assetStatus() {
        if (assetStatus == null) {
            assetStatus = new IntegerFilter();
        }
        return assetStatus;
    }

    public void setAssetStatus(IntegerFilter assetStatus) {
        this.assetStatus = assetStatus;
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
        final AssetCriteria that = (AssetCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(assetId, that.assetId) &&
            Objects.equals(assetSerial, that.assetSerial) &&
            Objects.equals(assetStatus, that.assetStatus) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, assetId, assetSerial, assetStatus, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (assetId != null ? "assetId=" + assetId + ", " : "") +
            (assetSerial != null ? "assetSerial=" + assetSerial + ", " : "") +
            (assetStatus != null ? "assetStatus=" + assetStatus + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
