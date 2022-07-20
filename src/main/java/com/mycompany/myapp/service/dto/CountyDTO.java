package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.County} entity.
 */
public class CountyDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer countyId;

    @NotNull
    private String countyCode;

    private String countyName;

    private String countyEnglishName;

    private ProvinceDTO province;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCountyId() {
        return countyId;
    }

    public void setCountyId(Integer countyId) {
        this.countyId = countyId;
    }

    public String getCountyCode() {
        return countyCode;
    }

    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getCountyEnglishName() {
        return countyEnglishName;
    }

    public void setCountyEnglishName(String countyEnglishName) {
        this.countyEnglishName = countyEnglishName;
    }

    public ProvinceDTO getProvince() {
        return province;
    }

    public void setProvince(ProvinceDTO province) {
        this.province = province;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CountyDTO)) {
            return false;
        }

        CountyDTO countyDTO = (CountyDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, countyDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CountyDTO{" +
            "id=" + getId() +
            ", countyId=" + getCountyId() +
            ", countyCode='" + getCountyCode() + "'" +
            ", countyName='" + getCountyName() + "'" +
            ", countyEnglishName='" + getCountyEnglishName() + "'" +
            ", province=" + getProvince() +
            "}";
    }
}
