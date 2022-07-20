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
 * Criteria class for the {@link com.mycompany.myapp.domain.City} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.CityResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cities?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class CityCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter cityId;

    private StringFilter cityCode;

    private StringFilter cityName;

    private StringFilter cityEnglishName;

    private LongFilter districtId;

    private Boolean distinct;

    public CityCriteria() {}

    public CityCriteria(CityCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.cityId = other.cityId == null ? null : other.cityId.copy();
        this.cityCode = other.cityCode == null ? null : other.cityCode.copy();
        this.cityName = other.cityName == null ? null : other.cityName.copy();
        this.cityEnglishName = other.cityEnglishName == null ? null : other.cityEnglishName.copy();
        this.districtId = other.districtId == null ? null : other.districtId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CityCriteria copy() {
        return new CityCriteria(this);
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

    public IntegerFilter getCityId() {
        return cityId;
    }

    public IntegerFilter cityId() {
        if (cityId == null) {
            cityId = new IntegerFilter();
        }
        return cityId;
    }

    public void setCityId(IntegerFilter cityId) {
        this.cityId = cityId;
    }

    public StringFilter getCityCode() {
        return cityCode;
    }

    public StringFilter cityCode() {
        if (cityCode == null) {
            cityCode = new StringFilter();
        }
        return cityCode;
    }

    public void setCityCode(StringFilter cityCode) {
        this.cityCode = cityCode;
    }

    public StringFilter getCityName() {
        return cityName;
    }

    public StringFilter cityName() {
        if (cityName == null) {
            cityName = new StringFilter();
        }
        return cityName;
    }

    public void setCityName(StringFilter cityName) {
        this.cityName = cityName;
    }

    public StringFilter getCityEnglishName() {
        return cityEnglishName;
    }

    public StringFilter cityEnglishName() {
        if (cityEnglishName == null) {
            cityEnglishName = new StringFilter();
        }
        return cityEnglishName;
    }

    public void setCityEnglishName(StringFilter cityEnglishName) {
        this.cityEnglishName = cityEnglishName;
    }

    public LongFilter getDistrictId() {
        return districtId;
    }

    public LongFilter districtId() {
        if (districtId == null) {
            districtId = new LongFilter();
        }
        return districtId;
    }

    public void setDistrictId(LongFilter districtId) {
        this.districtId = districtId;
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
        final CityCriteria that = (CityCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(cityId, that.cityId) &&
            Objects.equals(cityCode, that.cityCode) &&
            Objects.equals(cityName, that.cityName) &&
            Objects.equals(cityEnglishName, that.cityEnglishName) &&
            Objects.equals(districtId, that.districtId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cityId, cityCode, cityName, cityEnglishName, districtId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CityCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (cityId != null ? "cityId=" + cityId + ", " : "") +
            (cityCode != null ? "cityCode=" + cityCode + ", " : "") +
            (cityName != null ? "cityName=" + cityName + ", " : "") +
            (cityEnglishName != null ? "cityEnglishName=" + cityEnglishName + ", " : "") +
            (districtId != null ? "districtId=" + districtId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
