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
 * Criteria class for the {@link com.mycompany.myapp.domain.Language} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.LanguageResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /languages?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class LanguageCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter languageId;

    private StringFilter languageIsoCode;

    private StringFilter languageName;

    private StringFilter languageEnglishName;

    private IntegerFilter isActive;

    private IntegerFilter sortOrder;

    private Boolean distinct;

    public LanguageCriteria() {}

    public LanguageCriteria(LanguageCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.languageId = other.languageId == null ? null : other.languageId.copy();
        this.languageIsoCode = other.languageIsoCode == null ? null : other.languageIsoCode.copy();
        this.languageName = other.languageName == null ? null : other.languageName.copy();
        this.languageEnglishName = other.languageEnglishName == null ? null : other.languageEnglishName.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.sortOrder = other.sortOrder == null ? null : other.sortOrder.copy();
        this.distinct = other.distinct;
    }

    @Override
    public LanguageCriteria copy() {
        return new LanguageCriteria(this);
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

    public IntegerFilter getLanguageId() {
        return languageId;
    }

    public IntegerFilter languageId() {
        if (languageId == null) {
            languageId = new IntegerFilter();
        }
        return languageId;
    }

    public void setLanguageId(IntegerFilter languageId) {
        this.languageId = languageId;
    }

    public StringFilter getLanguageIsoCode() {
        return languageIsoCode;
    }

    public StringFilter languageIsoCode() {
        if (languageIsoCode == null) {
            languageIsoCode = new StringFilter();
        }
        return languageIsoCode;
    }

    public void setLanguageIsoCode(StringFilter languageIsoCode) {
        this.languageIsoCode = languageIsoCode;
    }

    public StringFilter getLanguageName() {
        return languageName;
    }

    public StringFilter languageName() {
        if (languageName == null) {
            languageName = new StringFilter();
        }
        return languageName;
    }

    public void setLanguageName(StringFilter languageName) {
        this.languageName = languageName;
    }

    public StringFilter getLanguageEnglishName() {
        return languageEnglishName;
    }

    public StringFilter languageEnglishName() {
        if (languageEnglishName == null) {
            languageEnglishName = new StringFilter();
        }
        return languageEnglishName;
    }

    public void setLanguageEnglishName(StringFilter languageEnglishName) {
        this.languageEnglishName = languageEnglishName;
    }

    public IntegerFilter getIsActive() {
        return isActive;
    }

    public IntegerFilter isActive() {
        if (isActive == null) {
            isActive = new IntegerFilter();
        }
        return isActive;
    }

    public void setIsActive(IntegerFilter isActive) {
        this.isActive = isActive;
    }

    public IntegerFilter getSortOrder() {
        return sortOrder;
    }

    public IntegerFilter sortOrder() {
        if (sortOrder == null) {
            sortOrder = new IntegerFilter();
        }
        return sortOrder;
    }

    public void setSortOrder(IntegerFilter sortOrder) {
        this.sortOrder = sortOrder;
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
        final LanguageCriteria that = (LanguageCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(languageId, that.languageId) &&
            Objects.equals(languageIsoCode, that.languageIsoCode) &&
            Objects.equals(languageName, that.languageName) &&
            Objects.equals(languageEnglishName, that.languageEnglishName) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(sortOrder, that.sortOrder) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, languageId, languageIsoCode, languageName, languageEnglishName, isActive, sortOrder, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LanguageCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (languageId != null ? "languageId=" + languageId + ", " : "") +
            (languageIsoCode != null ? "languageIsoCode=" + languageIsoCode + ", " : "") +
            (languageName != null ? "languageName=" + languageName + ", " : "") +
            (languageEnglishName != null ? "languageEnglishName=" + languageEnglishName + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (sortOrder != null ? "sortOrder=" + sortOrder + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
