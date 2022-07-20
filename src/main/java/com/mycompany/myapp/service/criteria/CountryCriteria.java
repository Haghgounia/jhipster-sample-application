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
 * Criteria class for the {@link com.mycompany.myapp.domain.Country} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.CountryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /countries?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class CountryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter countryId;

    private StringFilter countryName;

    private StringFilter countryEnglishName;

    private StringFilter countryFullName;

    private StringFilter countryEnglishFullName;

    private StringFilter countryIsoCode;

    private StringFilter nationality;

    private StringFilter englishNationality;

    private IntegerFilter isActive;

    private IntegerFilter sortOrder;

    private LongFilter continentId;

    private LongFilter currencyId;

    private LongFilter officialLanguageId;

    private Boolean distinct;

    public CountryCriteria() {}

    public CountryCriteria(CountryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.countryId = other.countryId == null ? null : other.countryId.copy();
        this.countryName = other.countryName == null ? null : other.countryName.copy();
        this.countryEnglishName = other.countryEnglishName == null ? null : other.countryEnglishName.copy();
        this.countryFullName = other.countryFullName == null ? null : other.countryFullName.copy();
        this.countryEnglishFullName = other.countryEnglishFullName == null ? null : other.countryEnglishFullName.copy();
        this.countryIsoCode = other.countryIsoCode == null ? null : other.countryIsoCode.copy();
        this.nationality = other.nationality == null ? null : other.nationality.copy();
        this.englishNationality = other.englishNationality == null ? null : other.englishNationality.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.sortOrder = other.sortOrder == null ? null : other.sortOrder.copy();
        this.continentId = other.continentId == null ? null : other.continentId.copy();
        this.currencyId = other.currencyId == null ? null : other.currencyId.copy();
        this.officialLanguageId = other.officialLanguageId == null ? null : other.officialLanguageId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CountryCriteria copy() {
        return new CountryCriteria(this);
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

    public IntegerFilter getCountryId() {
        return countryId;
    }

    public IntegerFilter countryId() {
        if (countryId == null) {
            countryId = new IntegerFilter();
        }
        return countryId;
    }

    public void setCountryId(IntegerFilter countryId) {
        this.countryId = countryId;
    }

    public StringFilter getCountryName() {
        return countryName;
    }

    public StringFilter countryName() {
        if (countryName == null) {
            countryName = new StringFilter();
        }
        return countryName;
    }

    public void setCountryName(StringFilter countryName) {
        this.countryName = countryName;
    }

    public StringFilter getCountryEnglishName() {
        return countryEnglishName;
    }

    public StringFilter countryEnglishName() {
        if (countryEnglishName == null) {
            countryEnglishName = new StringFilter();
        }
        return countryEnglishName;
    }

    public void setCountryEnglishName(StringFilter countryEnglishName) {
        this.countryEnglishName = countryEnglishName;
    }

    public StringFilter getCountryFullName() {
        return countryFullName;
    }

    public StringFilter countryFullName() {
        if (countryFullName == null) {
            countryFullName = new StringFilter();
        }
        return countryFullName;
    }

    public void setCountryFullName(StringFilter countryFullName) {
        this.countryFullName = countryFullName;
    }

    public StringFilter getCountryEnglishFullName() {
        return countryEnglishFullName;
    }

    public StringFilter countryEnglishFullName() {
        if (countryEnglishFullName == null) {
            countryEnglishFullName = new StringFilter();
        }
        return countryEnglishFullName;
    }

    public void setCountryEnglishFullName(StringFilter countryEnglishFullName) {
        this.countryEnglishFullName = countryEnglishFullName;
    }

    public StringFilter getCountryIsoCode() {
        return countryIsoCode;
    }

    public StringFilter countryIsoCode() {
        if (countryIsoCode == null) {
            countryIsoCode = new StringFilter();
        }
        return countryIsoCode;
    }

    public void setCountryIsoCode(StringFilter countryIsoCode) {
        this.countryIsoCode = countryIsoCode;
    }

    public StringFilter getNationality() {
        return nationality;
    }

    public StringFilter nationality() {
        if (nationality == null) {
            nationality = new StringFilter();
        }
        return nationality;
    }

    public void setNationality(StringFilter nationality) {
        this.nationality = nationality;
    }

    public StringFilter getEnglishNationality() {
        return englishNationality;
    }

    public StringFilter englishNationality() {
        if (englishNationality == null) {
            englishNationality = new StringFilter();
        }
        return englishNationality;
    }

    public void setEnglishNationality(StringFilter englishNationality) {
        this.englishNationality = englishNationality;
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

    public LongFilter getContinentId() {
        return continentId;
    }

    public LongFilter continentId() {
        if (continentId == null) {
            continentId = new LongFilter();
        }
        return continentId;
    }

    public void setContinentId(LongFilter continentId) {
        this.continentId = continentId;
    }

    public LongFilter getCurrencyId() {
        return currencyId;
    }

    public LongFilter currencyId() {
        if (currencyId == null) {
            currencyId = new LongFilter();
        }
        return currencyId;
    }

    public void setCurrencyId(LongFilter currencyId) {
        this.currencyId = currencyId;
    }

    public LongFilter getOfficialLanguageId() {
        return officialLanguageId;
    }

    public LongFilter officialLanguageId() {
        if (officialLanguageId == null) {
            officialLanguageId = new LongFilter();
        }
        return officialLanguageId;
    }

    public void setOfficialLanguageId(LongFilter officialLanguageId) {
        this.officialLanguageId = officialLanguageId;
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
        final CountryCriteria that = (CountryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(countryId, that.countryId) &&
            Objects.equals(countryName, that.countryName) &&
            Objects.equals(countryEnglishName, that.countryEnglishName) &&
            Objects.equals(countryFullName, that.countryFullName) &&
            Objects.equals(countryEnglishFullName, that.countryEnglishFullName) &&
            Objects.equals(countryIsoCode, that.countryIsoCode) &&
            Objects.equals(nationality, that.nationality) &&
            Objects.equals(englishNationality, that.englishNationality) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(sortOrder, that.sortOrder) &&
            Objects.equals(continentId, that.continentId) &&
            Objects.equals(currencyId, that.currencyId) &&
            Objects.equals(officialLanguageId, that.officialLanguageId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            countryId,
            countryName,
            countryEnglishName,
            countryFullName,
            countryEnglishFullName,
            countryIsoCode,
            nationality,
            englishNationality,
            isActive,
            sortOrder,
            continentId,
            currencyId,
            officialLanguageId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CountryCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (countryId != null ? "countryId=" + countryId + ", " : "") +
            (countryName != null ? "countryName=" + countryName + ", " : "") +
            (countryEnglishName != null ? "countryEnglishName=" + countryEnglishName + ", " : "") +
            (countryFullName != null ? "countryFullName=" + countryFullName + ", " : "") +
            (countryEnglishFullName != null ? "countryEnglishFullName=" + countryEnglishFullName + ", " : "") +
            (countryIsoCode != null ? "countryIsoCode=" + countryIsoCode + ", " : "") +
            (nationality != null ? "nationality=" + nationality + ", " : "") +
            (englishNationality != null ? "englishNationality=" + englishNationality + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (sortOrder != null ? "sortOrder=" + sortOrder + ", " : "") +
            (continentId != null ? "continentId=" + continentId + ", " : "") +
            (currencyId != null ? "currencyId=" + currencyId + ", " : "") +
            (officialLanguageId != null ? "officialLanguageId=" + officialLanguageId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
