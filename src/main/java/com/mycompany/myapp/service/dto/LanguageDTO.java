package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Language} entity.
 */
public class LanguageDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer languageId;

    @NotNull
    private String languageIsoCode;

    private String languageName;

    private String languageEnglishName;

    private Integer isActive;

    private Integer sortOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Integer languageId) {
        this.languageId = languageId;
    }

    public String getLanguageIsoCode() {
        return languageIsoCode;
    }

    public void setLanguageIsoCode(String languageIsoCode) {
        this.languageIsoCode = languageIsoCode;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public String getLanguageEnglishName() {
        return languageEnglishName;
    }

    public void setLanguageEnglishName(String languageEnglishName) {
        this.languageEnglishName = languageEnglishName;
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
        if (!(o instanceof LanguageDTO)) {
            return false;
        }

        LanguageDTO languageDTO = (LanguageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, languageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LanguageDTO{" +
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
