package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Currency} entity.
 */
public class CurrencyDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer currencyId;

    @NotNull
    private String currencyAlphabeticIso;

    @NotNull
    private String currencyNumericIso;

    @NotNull
    private String currencyName;

    private String currencyEnglishName;

    private String currencySymbol;

    private Integer floatingPoint;

    private Integer isBaseCurrency;

    private Integer isDefaultCurrency;

    private Integer conversionMethod;

    private Integer isActive;

    private Integer sortOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public String getCurrencyAlphabeticIso() {
        return currencyAlphabeticIso;
    }

    public void setCurrencyAlphabeticIso(String currencyAlphabeticIso) {
        this.currencyAlphabeticIso = currencyAlphabeticIso;
    }

    public String getCurrencyNumericIso() {
        return currencyNumericIso;
    }

    public void setCurrencyNumericIso(String currencyNumericIso) {
        this.currencyNumericIso = currencyNumericIso;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCurrencyEnglishName() {
        return currencyEnglishName;
    }

    public void setCurrencyEnglishName(String currencyEnglishName) {
        this.currencyEnglishName = currencyEnglishName;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public Integer getFloatingPoint() {
        return floatingPoint;
    }

    public void setFloatingPoint(Integer floatingPoint) {
        this.floatingPoint = floatingPoint;
    }

    public Integer getIsBaseCurrency() {
        return isBaseCurrency;
    }

    public void setIsBaseCurrency(Integer isBaseCurrency) {
        this.isBaseCurrency = isBaseCurrency;
    }

    public Integer getIsDefaultCurrency() {
        return isDefaultCurrency;
    }

    public void setIsDefaultCurrency(Integer isDefaultCurrency) {
        this.isDefaultCurrency = isDefaultCurrency;
    }

    public Integer getConversionMethod() {
        return conversionMethod;
    }

    public void setConversionMethod(Integer conversionMethod) {
        this.conversionMethod = conversionMethod;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CurrencyDTO)) {
            return false;
        }

        CurrencyDTO currencyDTO = (CurrencyDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, currencyDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CurrencyDTO{" +
            "id=" + getId() +
            ", currencyId=" + getCurrencyId() +
            ", currencyAlphabeticIso='" + getCurrencyAlphabeticIso() + "'" +
            ", currencyNumericIso='" + getCurrencyNumericIso() + "'" +
            ", currencyName='" + getCurrencyName() + "'" +
            ", currencyEnglishName='" + getCurrencyEnglishName() + "'" +
            ", currencySymbol='" + getCurrencySymbol() + "'" +
            ", floatingPoint=" + getFloatingPoint() +
            ", isBaseCurrency=" + getIsBaseCurrency() +
            ", isDefaultCurrency=" + getIsDefaultCurrency() +
            ", conversionMethod=" + getConversionMethod() +
            ", isActive=" + getIsActive() +
            ", sortOrder=" + getSortOrder() +
            "}";
    }
}
