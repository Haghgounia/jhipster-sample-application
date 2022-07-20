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
 * Criteria class for the {@link com.mycompany.myapp.domain.Currency} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.CurrencyResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /currencies?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class CurrencyCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter currencyId;

    private StringFilter currencyAlphabeticIso;

    private StringFilter currencyNumericIso;

    private StringFilter currencyName;

    private StringFilter currencyEnglishName;

    private StringFilter currencySymbol;

    private IntegerFilter floatingPoint;

    private IntegerFilter isBaseCurrency;

    private IntegerFilter isDefaultCurrency;

    private IntegerFilter conversionMethod;

    private IntegerFilter isActive;

    private IntegerFilter sortOrder;

    private Boolean distinct;

    public CurrencyCriteria() {}

    public CurrencyCriteria(CurrencyCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.currencyId = other.currencyId == null ? null : other.currencyId.copy();
        this.currencyAlphabeticIso = other.currencyAlphabeticIso == null ? null : other.currencyAlphabeticIso.copy();
        this.currencyNumericIso = other.currencyNumericIso == null ? null : other.currencyNumericIso.copy();
        this.currencyName = other.currencyName == null ? null : other.currencyName.copy();
        this.currencyEnglishName = other.currencyEnglishName == null ? null : other.currencyEnglishName.copy();
        this.currencySymbol = other.currencySymbol == null ? null : other.currencySymbol.copy();
        this.floatingPoint = other.floatingPoint == null ? null : other.floatingPoint.copy();
        this.isBaseCurrency = other.isBaseCurrency == null ? null : other.isBaseCurrency.copy();
        this.isDefaultCurrency = other.isDefaultCurrency == null ? null : other.isDefaultCurrency.copy();
        this.conversionMethod = other.conversionMethod == null ? null : other.conversionMethod.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.sortOrder = other.sortOrder == null ? null : other.sortOrder.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CurrencyCriteria copy() {
        return new CurrencyCriteria(this);
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

    public IntegerFilter getCurrencyId() {
        return currencyId;
    }

    public IntegerFilter currencyId() {
        if (currencyId == null) {
            currencyId = new IntegerFilter();
        }
        return currencyId;
    }

    public void setCurrencyId(IntegerFilter currencyId) {
        this.currencyId = currencyId;
    }

    public StringFilter getCurrencyAlphabeticIso() {
        return currencyAlphabeticIso;
    }

    public StringFilter currencyAlphabeticIso() {
        if (currencyAlphabeticIso == null) {
            currencyAlphabeticIso = new StringFilter();
        }
        return currencyAlphabeticIso;
    }

    public void setCurrencyAlphabeticIso(StringFilter currencyAlphabeticIso) {
        this.currencyAlphabeticIso = currencyAlphabeticIso;
    }

    public StringFilter getCurrencyNumericIso() {
        return currencyNumericIso;
    }

    public StringFilter currencyNumericIso() {
        if (currencyNumericIso == null) {
            currencyNumericIso = new StringFilter();
        }
        return currencyNumericIso;
    }

    public void setCurrencyNumericIso(StringFilter currencyNumericIso) {
        this.currencyNumericIso = currencyNumericIso;
    }

    public StringFilter getCurrencyName() {
        return currencyName;
    }

    public StringFilter currencyName() {
        if (currencyName == null) {
            currencyName = new StringFilter();
        }
        return currencyName;
    }

    public void setCurrencyName(StringFilter currencyName) {
        this.currencyName = currencyName;
    }

    public StringFilter getCurrencyEnglishName() {
        return currencyEnglishName;
    }

    public StringFilter currencyEnglishName() {
        if (currencyEnglishName == null) {
            currencyEnglishName = new StringFilter();
        }
        return currencyEnglishName;
    }

    public void setCurrencyEnglishName(StringFilter currencyEnglishName) {
        this.currencyEnglishName = currencyEnglishName;
    }

    public StringFilter getCurrencySymbol() {
        return currencySymbol;
    }

    public StringFilter currencySymbol() {
        if (currencySymbol == null) {
            currencySymbol = new StringFilter();
        }
        return currencySymbol;
    }

    public void setCurrencySymbol(StringFilter currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public IntegerFilter getFloatingPoint() {
        return floatingPoint;
    }

    public IntegerFilter floatingPoint() {
        if (floatingPoint == null) {
            floatingPoint = new IntegerFilter();
        }
        return floatingPoint;
    }

    public void setFloatingPoint(IntegerFilter floatingPoint) {
        this.floatingPoint = floatingPoint;
    }

    public IntegerFilter getIsBaseCurrency() {
        return isBaseCurrency;
    }

    public IntegerFilter isBaseCurrency() {
        if (isBaseCurrency == null) {
            isBaseCurrency = new IntegerFilter();
        }
        return isBaseCurrency;
    }

    public void setIsBaseCurrency(IntegerFilter isBaseCurrency) {
        this.isBaseCurrency = isBaseCurrency;
    }

    public IntegerFilter getIsDefaultCurrency() {
        return isDefaultCurrency;
    }

    public IntegerFilter isDefaultCurrency() {
        if (isDefaultCurrency == null) {
            isDefaultCurrency = new IntegerFilter();
        }
        return isDefaultCurrency;
    }

    public void setIsDefaultCurrency(IntegerFilter isDefaultCurrency) {
        this.isDefaultCurrency = isDefaultCurrency;
    }

    public IntegerFilter getConversionMethod() {
        return conversionMethod;
    }

    public IntegerFilter conversionMethod() {
        if (conversionMethod == null) {
            conversionMethod = new IntegerFilter();
        }
        return conversionMethod;
    }

    public void setConversionMethod(IntegerFilter conversionMethod) {
        this.conversionMethod = conversionMethod;
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
        final CurrencyCriteria that = (CurrencyCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(currencyId, that.currencyId) &&
            Objects.equals(currencyAlphabeticIso, that.currencyAlphabeticIso) &&
            Objects.equals(currencyNumericIso, that.currencyNumericIso) &&
            Objects.equals(currencyName, that.currencyName) &&
            Objects.equals(currencyEnglishName, that.currencyEnglishName) &&
            Objects.equals(currencySymbol, that.currencySymbol) &&
            Objects.equals(floatingPoint, that.floatingPoint) &&
            Objects.equals(isBaseCurrency, that.isBaseCurrency) &&
            Objects.equals(isDefaultCurrency, that.isDefaultCurrency) &&
            Objects.equals(conversionMethod, that.conversionMethod) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(sortOrder, that.sortOrder) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            currencyId,
            currencyAlphabeticIso,
            currencyNumericIso,
            currencyName,
            currencyEnglishName,
            currencySymbol,
            floatingPoint,
            isBaseCurrency,
            isDefaultCurrency,
            conversionMethod,
            isActive,
            sortOrder,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CurrencyCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (currencyId != null ? "currencyId=" + currencyId + ", " : "") +
            (currencyAlphabeticIso != null ? "currencyAlphabeticIso=" + currencyAlphabeticIso + ", " : "") +
            (currencyNumericIso != null ? "currencyNumericIso=" + currencyNumericIso + ", " : "") +
            (currencyName != null ? "currencyName=" + currencyName + ", " : "") +
            (currencyEnglishName != null ? "currencyEnglishName=" + currencyEnglishName + ", " : "") +
            (currencySymbol != null ? "currencySymbol=" + currencySymbol + ", " : "") +
            (floatingPoint != null ? "floatingPoint=" + floatingPoint + ", " : "") +
            (isBaseCurrency != null ? "isBaseCurrency=" + isBaseCurrency + ", " : "") +
            (isDefaultCurrency != null ? "isDefaultCurrency=" + isDefaultCurrency + ", " : "") +
            (conversionMethod != null ? "conversionMethod=" + conversionMethod + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (sortOrder != null ? "sortOrder=" + sortOrder + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
