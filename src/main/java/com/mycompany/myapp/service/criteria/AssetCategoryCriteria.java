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
 * Criteria class for the {@link com.mycompany.myapp.domain.AssetCategory} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.AssetCategoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /asset-categories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class AssetCategoryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter assetCategoryId;

    private StringFilter assetCategoryName;

    private Boolean distinct;

    public AssetCategoryCriteria() {}

    public AssetCategoryCriteria(AssetCategoryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.assetCategoryId = other.assetCategoryId == null ? null : other.assetCategoryId.copy();
        this.assetCategoryName = other.assetCategoryName == null ? null : other.assetCategoryName.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AssetCategoryCriteria copy() {
        return new AssetCategoryCriteria(this);
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

    public IntegerFilter getAssetCategoryId() {
        return assetCategoryId;
    }

    public IntegerFilter assetCategoryId() {
        if (assetCategoryId == null) {
            assetCategoryId = new IntegerFilter();
        }
        return assetCategoryId;
    }

    public void setAssetCategoryId(IntegerFilter assetCategoryId) {
        this.assetCategoryId = assetCategoryId;
    }

    public StringFilter getAssetCategoryName() {
        return assetCategoryName;
    }

    public StringFilter assetCategoryName() {
        if (assetCategoryName == null) {
            assetCategoryName = new StringFilter();
        }
        return assetCategoryName;
    }

    public void setAssetCategoryName(StringFilter assetCategoryName) {
        this.assetCategoryName = assetCategoryName;
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
        final AssetCategoryCriteria that = (AssetCategoryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(assetCategoryId, that.assetCategoryId) &&
            Objects.equals(assetCategoryName, that.assetCategoryName) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, assetCategoryId, assetCategoryName, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetCategoryCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (assetCategoryId != null ? "assetCategoryId=" + assetCategoryId + ", " : "") +
            (assetCategoryName != null ? "assetCategoryName=" + assetCategoryName + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
