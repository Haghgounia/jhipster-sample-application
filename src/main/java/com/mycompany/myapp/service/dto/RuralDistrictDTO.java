package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.RuralDistrict} entity.
 */
public class RuralDistrictDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer ruralDistrictId;

    @NotNull
    private String ruralDistrictCode;

    private String ruralDistrictName;

    private String ruralDistrictEnglishName;

    private DistrictDTO district;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRuralDistrictId() {
        return ruralDistrictId;
    }

    public void setRuralDistrictId(Integer ruralDistrictId) {
        this.ruralDistrictId = ruralDistrictId;
    }

    public String getRuralDistrictCode() {
        return ruralDistrictCode;
    }

    public void setRuralDistrictCode(String ruralDistrictCode) {
        this.ruralDistrictCode = ruralDistrictCode;
    }

    public String getRuralDistrictName() {
        return ruralDistrictName;
    }

    public void setRuralDistrictName(String ruralDistrictName) {
        this.ruralDistrictName = ruralDistrictName;
    }

    public String getRuralDistrictEnglishName() {
        return ruralDistrictEnglishName;
    }

    public void setRuralDistrictEnglishName(String ruralDistrictEnglishName) {
        this.ruralDistrictEnglishName = ruralDistrictEnglishName;
    }

    public DistrictDTO getDistrict() {
        return district;
    }

    public void setDistrict(DistrictDTO district) {
        this.district = district;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RuralDistrictDTO)) {
            return false;
        }

        RuralDistrictDTO ruralDistrictDTO = (RuralDistrictDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ruralDistrictDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RuralDistrictDTO{" +
            "id=" + getId() +
            ", ruralDistrictId=" + getRuralDistrictId() +
            ", ruralDistrictCode='" + getRuralDistrictCode() + "'" +
            ", ruralDistrictName='" + getRuralDistrictName() + "'" +
            ", ruralDistrictEnglishName='" + getRuralDistrictEnglishName() + "'" +
            ", district=" + getDistrict() +
            "}";
    }
}
