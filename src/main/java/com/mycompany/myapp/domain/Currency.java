package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Currency.
 */
@Entity
@Table(name = "currency")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Currency implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "currency_id", nullable = false)
    private Integer currencyId;

    @NotNull
    @Column(name = "currency_alphabetic_iso", nullable = false, unique = true)
    private String currencyAlphabeticIso;

    @NotNull
    @Column(name = "currency_numeric_iso", nullable = false, unique = true)
    private String currencyNumericIso;

    @NotNull
    @Column(name = "currency_name", nullable = false)
    private String currencyName;

    @Column(name = "currency_english_name")
    private String currencyEnglishName;

    @Column(name = "currency_symbol")
    private String currencySymbol;

    @Column(name = "floating_point")
    private Integer floatingPoint;

    @Column(name = "is_base_currency")
    private Integer isBaseCurrency;

    @Column(name = "is_default_currency")
    private Integer isDefaultCurrency;

    @Column(name = "conversion_method")
    private Integer conversionMethod;

    @Column(name = "is_active")
    private Integer isActive;

    @Column(name = "sort_order")
    private Integer sortOrder;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Currency id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCurrencyId() {
        return this.currencyId;
    }

    public Currency currencyId(Integer currencyId) {
        this.setCurrencyId(currencyId);
        return this;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public String getCurrencyAlphabeticIso() {
        return this.currencyAlphabeticIso;
    }

    public Currency currencyAlphabeticIso(String currencyAlphabeticIso) {
        this.setCurrencyAlphabeticIso(currencyAlphabeticIso);
        return this;
    }

    public void setCurrencyAlphabeticIso(String currencyAlphabeticIso) {
        this.currencyAlphabeticIso = currencyAlphabeticIso;
    }

    public String getCurrencyNumericIso() {
        return this.currencyNumericIso;
    }

    public Currency currencyNumericIso(String currencyNumericIso) {
        this.setCurrencyNumericIso(currencyNumericIso);
        return this;
    }

    public void setCurrencyNumericIso(String currencyNumericIso) {
        this.currencyNumericIso = currencyNumericIso;
    }

    public String getCurrencyName() {
        return this.currencyName;
    }

    public Currency currencyName(String currencyName) {
        this.setCurrencyName(currencyName);
        return this;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCurrencyEnglishName() {
        return this.currencyEnglishName;
    }

    public Currency currencyEnglishName(String currencyEnglishName) {
        this.setCurrencyEnglishName(currencyEnglishName);
        return this;
    }

    public void setCurrencyEnglishName(String currencyEnglishName) {
        this.currencyEnglishName = currencyEnglishName;
    }

    public String getCurrencySymbol() {
        return this.currencySymbol;
    }

    public Currency currencySymbol(String currencySymbol) {
        this.setCurrencySymbol(currencySymbol);
        return this;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public Integer getFloatingPoint() {
        return this.floatingPoint;
    }

    public Currency floatingPoint(Integer floatingPoint) {
        this.setFloatingPoint(floatingPoint);
        return this;
    }

    public void setFloatingPoint(Integer floatingPoint) {
        this.floatingPoint = floatingPoint;
    }

    public Integer getIsBaseCurrency() {
        return this.isBaseCurrency;
    }

    public Currency isBaseCurrency(Integer isBaseCurrency) {
        this.setIsBaseCurrency(isBaseCurrency);
        return this;
    }

    public void setIsBaseCurrency(Integer isBaseCurrency) {
        this.isBaseCurrency = isBaseCurrency;
    }

    public Integer getIsDefaultCurrency() {
        return this.isDefaultCurrency;
    }

    public Currency isDefaultCurrency(Integer isDefaultCurrency) {
        this.setIsDefaultCurrency(isDefaultCurrency);
        return this;
    }

    public void setIsDefaultCurrency(Integer isDefaultCurrency) {
        this.isDefaultCurrency = isDefaultCurrency;
    }

    public Integer getConversionMethod() {
        return this.conversionMethod;
    }

    public Currency conversionMethod(Integer conversionMethod) {
        this.setConversionMethod(conversionMethod);
        return this;
    }

    public void setConversionMethod(Integer conversionMethod) {
        this.conversionMethod = conversionMethod;
    }

    public Integer getIsActive() {
        return this.isActive;
    }

    public Currency isActive(Integer isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public Integer getSortOrder() {
        return this.sortOrder;
    }

    public Currency sortOrder(Integer sortOrder) {
        this.setSortOrder(sortOrder);
        return this;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Currency)) {
            return false;
        }
        return id != null && id.equals(((Currency) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Currency{" +
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
