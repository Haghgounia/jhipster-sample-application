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
 * Criteria class for the {@link com.mycompany.myapp.domain.County} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.CountyResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /counties?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class CountyCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter countyId;

    private StringFilter countyCode;

    private StringFilter countyName;

    private StringFilter countyEnglishName;

    private LongFilter provinceId;

    private Boolean distinct;

    public CountyCriteria() {}

    public CountyCriteria(CountyCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.countyId = other.countyId == null ? null : other.countyId.copy();
        this.countyCode = other.countyCode == null ? null : other.countyCode.copy();
        this.countyName = other.countyName == null ? null : other.countyName.copy();
        this.countyEnglishName = other.countyEnglishName == null ? null : other.countyEnglishName.copy();
        this.provinceId = other.provinceId == null ? null : other.provinceId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CountyCriteria copy() {
        return new CountyCriteria(this);
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

    public IntegerFilter getCountyId() {
        return countyId;
    }

    public IntegerFilter countyId() {
        if (countyId == null) {
            countyId = new IntegerFilter();
        }
        return countyId;
    }

    public void setCountyId(IntegerFilter countyId) {
        this.countyId = countyId;
    }

    public StringFilter getCountyCode() {
        return countyCode;
    }

    public StringFilter countyCode() {
        if (countyCode == null) {
            countyCode = new StringFilter();
        }
        return countyCode;
    }

    public void setCountyCode(StringFilter countyCode) {
        this.countyCode = countyCode;
    }

    public StringFilter getCountyName() {
        return countyName;
    }

    public StringFilter countyName() {
        if (countyName == null) {
            countyName = new StringFilter();
        }
        return countyName;
    }

    public void setCountyName(StringFilter countyName) {
        this.countyName = countyName;
    }

    public StringFilter getCountyEnglishName() {
        return countyEnglishName;
    }

    public StringFilter countyEnglishName() {
        if (countyEnglishName == null) {
            countyEnglishName = new StringFilter();
        }
        return countyEnglishName;
    }

    public void setCountyEnglishName(StringFilter countyEnglishName) {
        this.countyEnglishName = countyEnglishName;
    }

    public LongFilter getProvinceId() {
        return provinceId;
    }

    public LongFilter provinceId() {
        if (provinceId == null) {
            provinceId = new LongFilter();
        }
        return provinceId;
    }

    public void setProvinceId(LongFilter provinceId) {
        this.provinceId = provinceId;
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
        final CountyCriteria that = (CountyCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(countyId, that.countyId) &&
            Objects.equals(countyCode, that.countyCode) &&
            Objects.equals(countyName, that.countyName) &&
            Objects.equals(countyEnglishName, that.countyEnglishName) &&
            Objects.equals(provinceId, that.provinceId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, countyId, countyCode, countyName, countyEnglishName, provinceId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CountyCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (countyId != null ? "countyId=" + countyId + ", " : "") +
            (countyCode != null ? "countyCode=" + countyCode + ", " : "") +
            (countyName != null ? "countyName=" + countyName + ", " : "") +
            (countyEnglishName != null ? "countyEnglishName=" + countyEnglishName + ", " : "") +
            (provinceId != null ? "provinceId=" + provinceId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
