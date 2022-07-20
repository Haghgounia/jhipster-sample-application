package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Language.
 */
@Entity
@Table(name = "language")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Language implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "language_id", nullable = false)
    private Integer languageId;

    @NotNull
    @Column(name = "language_iso_code", nullable = false, unique = true)
    private String languageIsoCode;

    @Column(name = "language_name")
    private String languageName;

    @Column(name = "language_english_name")
    private String languageEnglishName;

    @Column(name = "is_active")
    private Integer isActive;

    @Column(name = "sort_order")
    private Integer sortOrder;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Language id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLanguageId() {
        return this.languageId;
    }

    public Language languageId(Integer languageId) {
        this.setLanguageId(languageId);
        return this;
    }

    public void setLanguageId(Integer languageId) {
        this.languageId = languageId;
    }

    public String getLanguageIsoCode() {
        return this.languageIsoCode;
    }

    public Language languageIsoCode(String languageIsoCode) {
        this.setLanguageIsoCode(languageIsoCode);
        return this;
    }

    public void setLanguageIsoCode(String languageIsoCode) {
        this.languageIsoCode = languageIsoCode;
    }

    public String getLanguageName() {
        return this.languageName;
    }

    public Language languageName(String languageName) {
        this.setLanguageName(languageName);
        return this;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public String getLanguageEnglishName() {
        return this.languageEnglishName;
    }

    public Language languageEnglishName(String languageEnglishName) {
        this.setLanguageEnglishName(languageEnglishName);
        return this;
    }

    public void setLanguageEnglishName(String languageEnglishName) {
        this.languageEnglishName = languageEnglishName;
    }

    public Integer getIsActive() {
        return this.isActive;
    }

    public Language isActive(Integer isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public Integer getSortOrder() {
        return this.sortOrder;
    }

    public Language sortOrder(Integer sortOrder) {
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
        if (!(o instanceof Language)) {
            return false;
        }
        return id != null && id.equals(((Language) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Language{" +
            "id=" + getId() +
            ", languageId=" + getLanguageId() +
            ", languageIsoCode='" + getLanguageIsoCode() + "'" +
            ", languageName='" + getLanguageName() + "'" +
            ", languageEnglishName='" + getLanguageEnglishName() + "'" +
            ", isActive=" + getIsActive() +
            ", sortOrder=" + getSortOrder() +
            "}";
    }
}
