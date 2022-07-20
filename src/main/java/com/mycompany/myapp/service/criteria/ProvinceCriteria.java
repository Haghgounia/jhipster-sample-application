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
 * Criteria class for the {@link com.mycompany.myapp.domain.Province} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.ProvinceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /provinces?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class ProvinceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter provinceId;

    private StringFilter provinceCode;

    private StringFilter provinceName;

    private StringFilter provinceEnglishName;

    private StringFilter diallingCode;

    private LongFilter countryId;

    private Boolean distinct;

    public ProvinceCriteria() {}

    public ProvinceCriteria(ProvinceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.provinceId = other.provinceId == null ? null : other.provinceId.copy();
        this.provinceCode = other.provinceCode == null ? null : other.provinceCode.copy();
        this.provinceName = other.provinceName == null ? null : other.provinceName.copy();
        this.provinceEnglishName = other.provinceEnglishName == null ? null : other.provinceEnglishName.copy();
        this.diallingCode = other.diallingCode == null ? null : other.diallingCode.copy();
        this.countryId = other.countryId == null ? null : other.countryId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ProvinceCriteria copy() {
        return new ProvinceCriteria(this);
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

    public IntegerFilter getProvinceId() {
        return provinceId;
    }

    public IntegerFilter provinceId() {
        if (provinceId == null) {
            provinceId = new IntegerFilter();
        }
        return provinceId;
    }

    public void setProvinceId(IntegerFilter provinceId) {
        this.provinceId = provinceId;
    }

    public StringFilter getProvinceCode() {
        return provinceCode;
    }

    public StringFilter provinceCode() {
        if (provinceCode == null) {
            provinceCode = new StringFilter();
        }
        return provinceCode;
    }

    public void setProvinceCode(StringFilter provinceCode) {
        this.provinceCode = provinceCode;
    }

    public StringFilter getProvinceName() {
        return provinceName;
    }

    public StringFilter provinceName() {
        if (provinceName == null) {
            provinceName = new StringFilter();
        }
        return provinceName;
    }

    public void setProvinceName(StringFilter provinceName) {
        this.provinceName = provinceName;
    }

    public StringFilter getProvinceEnglishName() {
        return provinceEnglishName;
    }

    public StringFilter provinceEnglishName() {
        if (provinceEnglishName == null) {
            provinceEnglishName = new StringFilter();
        }
        return provinceEnglishName;
    }

    public void setProvinceEnglishName(StringFilter provinceEnglishName) {
        this.provinceEnglishName = provinceEnglishName;
    }

    public StringFilter getDiallingCode() {
        return diallingCode;
    }

    public StringFilter diallingCode() {
        if (diallingCode == null) {
            diallingCode = new StringFilter();
        }
        return diallingCode;
    }

    public void setDiallingCode(StringFilter diallingCode) {
        this.diallingCode = diallingCode;
    }

    public LongFilter getCountryId() {
        return countryId;
    }

    public LongFilter countryId() {
        if (countryId == null) {
            countryId = new LongFilter();
        }
        return countryId;
    }

    public void setCountryId(LongFilter countryId) {
        this.countryId = countryId;
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
        final ProvinceCriteria that = (ProvinceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(provinceId, that.provinceId) &&
            Objects.equals(provinceCode, that.provinceCode) &&
            Objects.equals(provinceName, that.provinceName) &&
            Objects.equals(provinceEnglishName, that.provinceEnglishName) &&
            Objects.equals(diallingCode, that.diallingCode) &&
            Objects.equals(countryId, that.countryId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, provinceId, provinceCode, provinceName, provinceEnglishName, diallingCode, countryId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProvinceCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (provinceId != null ? "provinceId=" + provinceId + ", " : "") +
            (provinceCode != null ? "provinceCode=" + provinceCode + ", " : "") +
            (provinceName != null ? "provinceName=" + provinceName + ", " : "") +
            (provinceEnglishName != null ? "provinceEnglishName=" + provinceEnglishName + ", " : "") +
            (diallingCode != null ? "diallingCode=" + diallingCode + ", " : "") +
            (countryId != null ? "countryId=" + countryId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
