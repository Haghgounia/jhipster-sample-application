package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Country} entity.
 */
public class CountryDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer countryId;

    @NotNull
    private String countryName;

    private String countryEnglishName;

    private String countryFullName;

    private String countryEnglishFullName;

    @NotNull
    private String countryIsoCode;

    private String nationality;

    private String englishNationality;

    private Integer isActive;

    private Integer sortOrder;

    private ContinentDTO continent;

    private CurrencyDTO currency;

    private LanguageDTO officialLanguage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryEnglishName() {
        return countryEnglishName;
    }

    public void setCountryEnglishName(String countryEnglishName) {
        this.countryEnglishName = countryEnglishName;
    }

    public String getCountryFullName() {
        return countryFullName;
    }

    public void setCountryFullName(String countryFullName) {
        this.countryFullName = countryFullName;
    }

    public String getCountryEnglishFullName() {
        return countryEnglishFullName;
    }

    public void setCountryEnglishFullName(String countryEnglishFullName) {
        this.countryEnglishFullName = countryEnglishFullName;
    }

    public String getCountryIsoCode() {
        return countryIsoCode;
    }

    public void setCountryIsoCode(String countryIsoCode) {
        this.countryIsoCode = countryIsoCode;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getEnglishNationality() {
        return englishNationality;
    }

    public void setEnglishNationality(String englishNationality) {
        this.englishNationality = englishNationality;
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

    public ContinentDTO getContinent() {
        return continent;
    }

    public void setContinent(ContinentDTO continent) {
        this.continent = continent;
    }

    public CurrencyDTO getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyDTO currency) {
        this.currency = currency;
    }

    public LanguageDTO getOfficialLanguage() {
        return officialLanguage;
    }

    public void setOfficialLanguage(LanguageDTO officialLanguage) {
        this.officialLanguage = officialLanguage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CountryDTO)) {
            return false;
        }

        CountryDTO countryDTO = (CountryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, countryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CountryDTO{" +
            "id=" + getId() +
            ", countryId=" + getCountryId() +
            ", countryName='" + getCountryName() + "'" +
            ", countryEnglishName='" + getCountryEnglishName() + "'" +
            ", countryFullName='" + getCountryFullName() + "'" +
            ", countryEnglishFullName='" + getCountryEnglishFullName() + "'" +
            ", countryIsoCode='" + getCountryIsoCode() + "'" +
            ", nationality='" + getNationality() + "'" +
            ", englishNationality='" + getEnglishNationality() + "'" +
            ", isActive=" + getIsActive() +
            ", sortOrder=" + getSortOrder() +
            ", continent=" + getContinent() +
            ", currency=" + getCurrency() +
            ", officialLanguage=" + getOfficialLanguage() +
            "}";
    }
}
