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
 * Criteria class for the {@link com.mycompany.myapp.domain.RuralDistrict} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.RuralDistrictResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /rural-districts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class RuralDistrictCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter ruralDistrictId;

    private StringFilter ruralDistrictCode;

    private StringFilter ruralDistrictName;

    private StringFilter ruralDistrictEnglishName;

    private LongFilter districtId;

    private Boolean distinct;

    public RuralDistrictCriteria() {}

    public RuralDistrictCriteria(RuralDistrictCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.ruralDistrictId = other.ruralDistrictId == null ? null : other.ruralDistrictId.copy();
        this.ruralDistrictCode = other.ruralDistrictCode == null ? null : other.ruralDistrictCode.copy();
        this.ruralDistrictName = other.ruralDistrictName == null ? null : other.ruralDistrictName.copy();
        this.ruralDistrictEnglishName = other.ruralDistrictEnglishName == null ? null : other.ruralDistrictEnglishName.copy();
        this.districtId = other.districtId == null ? null : other.districtId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public RuralDistrictCriteria copy() {
        return new RuralDistrictCriteria(this);
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

    public IntegerFilter getRuralDistrictId() {
        return ruralDistrictId;
    }

    public IntegerFilter ruralDistrictId() {
        if (ruralDistrictId == null) {
            ruralDistrictId = new IntegerFilter();
        }
        return ruralDistrictId;
    }

    public void setRuralDistrictId(IntegerFilter ruralDistrictId) {
        this.ruralDistrictId = ruralDistrictId;
    }

    public StringFilter getRuralDistrictCode() {
        return ruralDistrictCode;
    }

    public StringFilter ruralDistrictCode() {
        if (ruralDistrictCode == null) {
            ruralDistrictCode = new StringFilter();
        }
        return ruralDistrictCode;
    }

    public void setRuralDistrictCode(StringFilter ruralDistrictCode) {
        this.ruralDistrictCode = ruralDistrictCode;
    }

    public StringFilter getRuralDistrictName() {
        return ruralDistrictName;
    }

    public StringFilter ruralDistrictName() {
        if (ruralDistrictName == null) {
            ruralDistrictName = new StringFilter();
        }
        return ruralDistrictName;
    }

    public void setRuralDistrictName(StringFilter ruralDistrictName) {
        this.ruralDistrictName = ruralDistrictName;
    }

    public StringFilter getRuralDistrictEnglishName() {
        return ruralDistrictEnglishName;
    }

    public StringFilter ruralDistrictEnglishName() {
        if (ruralDistrictEnglishName == null) {
            ruralDistrictEnglishName = new StringFilter();
        }
        return ruralDistrictEnglishName;
    }

    public void setRuralDistrictEnglishName(StringFilter ruralDistrictEnglishName) {
        this.ruralDistrictEnglishName = ruralDistrictEnglishName;
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
        final RuralDistrictCriteria that = (RuralDistrictCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(ruralDistrictId, that.ruralDistrictId) &&
            Objects.equals(ruralDistrictCode, that.ruralDistrictCode) &&
            Objects.equals(ruralDistrictName, that.ruralDistrictName) &&
            Objects.equals(ruralDistrictEnglishName, that.ruralDistrictEnglishName) &&
            Objects.equals(districtId, that.districtId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ruralDistrictId, ruralDistrictCode, ruralDistrictName, ruralDistrictEnglishName, districtId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RuralDistrictCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (ruralDistrictId != null ? "ruralDistrictId=" + ruralDistrictId + ", " : "") +
            (ruralDistrictCode != null ? "ruralDistrictCode=" + ruralDistrictCode + ", " : "") +
            (ruralDistrictName != null ? "ruralDistrictName=" + ruralDistrictName + ", " : "") +
            (ruralDistrictEnglishName != null ? "ruralDistrictEnglishName=" + ruralDistrictEnglishName + ", " : "") +
            (districtId != null ? "districtId=" + districtId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
