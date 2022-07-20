package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Country.
 */
@Entity
@Table(name = "country")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Country implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "country_id", nullable = false)
    private Integer countryId;

    @NotNull
    @Column(name = "country_name", nullable = false, unique = true)
    private String countryName;

    @Column(name = "country_english_name")
    private String countryEnglishName;

    @Column(name = "country_full_name")
    private String countryFullName;

    @Column(name = "country_english_full_name")
    private String countryEnglishFullName;

    @NotNull
    @Column(name = "country_iso_code", nullable = false, unique = true)
    private String countryIsoCode;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "english_nationality")
    private String englishNationality;

    @Column(name = "is_active")
    private Integer isActive;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @ManyToOne
    private Continent continent;

    @ManyToOne
    private Currency currency;

    @ManyToOne
    private Language officialLanguage;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Country id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCountryId() {
        return this.countryId;
    }

    public Country countryId(Integer countryId) {
        this.setCountryId(countryId);
        return this;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return this.countryName;
    }

    public Country countryName(String countryName) {
        this.setCountryName(countryName);
        return this;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryEnglishName() {
        return this.countryEnglishName;
    }

    public Country countryEnglishName(String countryEnglishName) {
        this.setCountryEnglishName(countryEnglishName);
        return this;
    }

    public void setCountryEnglishName(String countryEnglishName) {
        this.countryEnglishName = countryEnglishName;
    }

    public String getCountryFullName() {
        return this.countryFullName;
    }

    public Country countryFullName(String countryFullName) {
        this.setCountryFullName(countryFullName);
        return this;
    }

    public void setCountryFullName(String countryFullName) {
        this.countryFullName = countryFullName;
    }

    public String getCountryEnglishFullName() {
        return this.countryEnglishFullName;
    }

    public Country countryEnglishFullName(String countryEnglishFullName) {
        this.setCountryEnglishFullName(countryEnglishFullName);
        return this;
    }

    public void setCountryEnglishFullName(String countryEnglishFullName) {
        this.countryEnglishFullName = countryEnglishFullName;
    }

    public String getCountryIsoCode() {
        return this.countryIsoCode;
    }

    public Country countryIsoCode(String countryIsoCode) {
        this.setCountryIsoCode(countryIsoCode);
        return this;
    }

    public void setCountryIsoCode(String countryIsoCode) {
        this.countryIsoCode = countryIsoCode;
    }

    public String getNationality() {
        return this.nationality;
    }

    public Country nationality(String nationality) {
        this.setNationality(nationality);
        return this;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getEnglishNationality() {
        return this.englishNationality;
    }

    public Country englishNationality(String englishNationality) {
        this.setEnglishNationality(englishNationality);
        return this;
    }

    public void setEnglishNationality(String englishNationality) {
        this.englishNationality = englishNationality;
    }

    public Integer getIsActive() {
        return this.isActive;
    }

    public Country isActive(Integer isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public Integer getSortOrder() {
        return this.sortOrder;
    }

    public Country sortOrder(Integer sortOrder) {
        this.setSortOrder(sortOrder);
        return this;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Continent getContinent() {
        return this.continent;
    }

    public void setContinent(Continent continent) {
        this.continent = continent;
    }

    public Country continent(Continent continent) {
        this.setContinent(continent);
        return this;
    }

    public Currency getCurrency() {
        return this.currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Country currency(Currency currency) {
        this.setCurrency(currency);
        return this;
    }

    public Language getOfficialLanguage() {
        return this.officialLanguage;
    }

    public void setOfficialLanguage(Language language) {
        this.officialLanguage = language;
    }

    public Country officialLanguage(Language language) {
        this.setOfficialLanguage(language);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Country)) {
            return false;
        }
        return id != null && id.equals(((Country) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Country{" +
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
            "}";
    }
}
