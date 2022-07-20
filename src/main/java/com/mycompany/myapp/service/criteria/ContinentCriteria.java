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
 * Criteria class for the {@link com.mycompany.myapp.domain.Continent} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.ContinentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /continents?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class ContinentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter continentId;

    private StringFilter continentCode;

    private StringFilter continentName;

    private StringFilter continentEnglishName;

    private Boolean distinct;

    public ContinentCriteria() {}

    public ContinentCriteria(ContinentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.continentId = other.continentId == null ? null : other.continentId.copy();
        this.continentCode = other.continentCode == null ? null : other.continentCode.copy();
        this.continentName = other.continentName == null ? null : other.continentName.copy();
        this.continentEnglishName = other.continentEnglishName == null ? null : other.continentEnglishName.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ContinentCriteria copy() {
        return new ContinentCriteria(this);
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

    public IntegerFilter getContinentId() {
        return continentId;
    }

    public IntegerFilter continentId() {
        if (continentId == null) {
            continentId = new IntegerFilter();
        }
        return continentId;
    }

    public void setContinentId(IntegerFilter continentId) {
        this.continentId = continentId;
    }

    public StringFilter getContinentCode() {
        return continentCode;
    }

    public StringFilter continentCode() {
        if (continentCode == null) {
            continentCode = new StringFilter();
        }
        return continentCode;
    }

    public void setContinentCode(StringFilter continentCode) {
        this.continentCode = continentCode;
    }

    public StringFilter getContinentName() {
        return continentName;
    }

    public StringFilter continentName() {
        if (continentName == null) {
            continentName = new StringFilter();
        }
        return continentName;
    }

    public void setContinentName(StringFilter continentName) {
        this.continentName = continentName;
    }

    public StringFilter getContinentEnglishName() {
        return continentEnglishName;
    }

    public StringFilter continentEnglishName() {
        if (continentEnglishName == null) {
            continentEnglishName = new StringFilter();
        }
        return continentEnglishName;
    }

    public void setContinentEnglishName(StringFilter continentEnglishName) {
        this.continentEnglishName = continentEnglishName;
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
        final ContinentCriteria that = (ContinentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(continentId, that.continentId) &&
            Objects.equals(continentCode, that.continentCode) &&
            Objects.equals(continentName, that.continentName) &&
            Objects.equals(continentEnglishName, that.continentEnglishName) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, continentId, continentCode, continentName, continentEnglishName, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContinentCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (continentId != null ? "continentId=" + continentId + ", " : "") +
            (continentCode != null ? "continentCode=" + continentCode + ", " : "") +
            (continentName != null ? "continentName=" + continentName + ", " : "") +
            (continentEnglishName != null ? "continentEnglishName=" + continentEnglishName + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
