package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Continent} entity.
 */
public class ContinentDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer continentId;

    @NotNull
    private String continentCode;

    @NotNull
    private String continentName;

    private String continentEnglishName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getContinentId() {
        return continentId;
    }

    public void setContinentId(Integer continentId) {
        this.continentId = continentId;
    }

    public String getContinentCode() {
        return continentCode;
    }

    public void setContinentCode(String continentCode) {
        this.continentCode = continentCode;
    }

    public String getContinentName() {
        return continentName;
    }

    public void setContinentName(String continentName) {
        this.continentName = continentName;
    }

    public String getContinentEnglishName() {
        return continentEnglishName;
    }

    public void setContinentEnglishName(String continentEnglishName) {
        this.continentEnglishName = continentEnglishName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContinentDTO)) {
            return false;
        }

        ContinentDTO continentDTO = (ContinentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, continentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContinentDTO{" +
            "id=" + getId() +
            ", continentId=" + getContinentId() +
            ", continentCode='" + getContinentCode() + "'" +
            ", continentName='" + getContinentName() + "'" +
            ", continentEnglishName='" + getContinentEnglishName() + "'" +
            "}";
    }
}
