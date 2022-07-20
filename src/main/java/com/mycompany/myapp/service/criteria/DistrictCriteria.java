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
 * Criteria class for the {@link com.mycompany.myapp.domain.District} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.DistrictResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /districts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class DistrictCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter districtId;

    private StringFilter districtCode;

    private StringFilter districtName;

    private StringFilter districtEnglishName;

    private LongFilter countyId;

    private Boolean distinct;

    public DistrictCriteria() {}

    public DistrictCriteria(DistrictCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.districtId = other.districtId == null ? null : other.districtId.copy();
        this.districtCode = other.districtCode == null ? null : other.districtCode.copy();
        this.districtName = other.districtName == null ? null : other.districtName.copy();
        this.districtEnglishName = other.districtEnglishName == null ? null : other.districtEnglishName.copy();
        this.countyId = other.countyId == null ? null : other.countyId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DistrictCriteria copy() {
        return new DistrictCriteria(this);
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

    public IntegerFilter getDistrictId() {
        return districtId;
    }

    public IntegerFilter districtId() {
        if (districtId == null) {
            districtId = new IntegerFilter();
        }
        return districtId;
    }

    public void setDistrictId(IntegerFilter districtId) {
        this.districtId = districtId;
    }

    public StringFilter getDistrictCode() {
        return districtCode;
    }

    public StringFilter districtCode() {
        if (districtCode == null) {
            districtCode = new StringFilter();
        }
        return districtCode;
    }

    public void setDistrictCode(StringFilter districtCode) {
        this.districtCode = districtCode;
    }

    public StringFilter getDistrictName() {
        return districtName;
    }

    public StringFilter districtName() {
        if (districtName == null) {
            districtName = new StringFilter();
        }
        return districtName;
    }

    public void setDistrictName(StringFilter districtName) {
        this.districtName = districtName;
    }

    public StringFilter getDistrictEnglishName() {
        return districtEnglishName;
    }

    public StringFilter districtEnglishName() {
        if (districtEnglishName == null) {
            districtEnglishName = new StringFilter();
        }
        return districtEnglishName;
    }

    public void setDistrictEnglishName(StringFilter districtEnglishName) {
        this.districtEnglishName = districtEnglishName;
    }

    public LongFilter getCountyId() {
        return countyId;
    }

    public LongFilter countyId() {
        if (countyId == null) {
            countyId = new LongFilter();
        }
        return countyId;
    }

    public void setCountyId(LongFilter countyId) {
        this.countyId = countyId;
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
        final DistrictCriteria that = (DistrictCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(districtId, that.districtId) &&
            Objects.equals(districtCode, that.districtCode) &&
            Objects.equals(districtName, that.districtName) &&
            Objects.equals(districtEnglishName, that.districtEnglishName) &&
            Objects.equals(countyId, that.countyId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, districtId, districtCode, districtName, districtEnglishName, countyId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DistrictCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (districtId != null ? "districtId=" + districtId + ", " : "") +
            (districtCode != null ? "districtCode=" + districtCode + ", " : "") +
            (districtName != null ? "districtName=" + districtName + ", " : "") +
            (districtEnglishName != null ? "districtEnglishName=" + districtEnglishName + ", " : "") +
            (countyId != null ? "countyId=" + countyId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
