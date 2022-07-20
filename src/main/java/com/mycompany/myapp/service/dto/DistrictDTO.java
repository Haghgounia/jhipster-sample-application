package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.District} entity.
 */
public class DistrictDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer districtId;

    @NotNull
    private String districtCode;

    private String districtName;

    private String districtEnglishName;

    private CountyDTO county;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getDistrictEnglishName() {
        return districtEnglishName;
    }

    public void setDistrictEnglishName(String districtEnglishName) {
        this.districtEnglishName = districtEnglishName;
    }

    public CountyDTO getCounty() {
        return county;
    }

    public void setCounty(CountyDTO county) {
        this.county = county;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DistrictDTO)) {
            return false;
        }

        DistrictDTO districtDTO = (DistrictDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, districtDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DistrictDTO{" +
            "id=" + getId() +
            ", districtId=" + getDistrictId() +
            ", districtCode='" + getDistrictCode() + "'" +
            ", districtName='" + getDistrictName() + "'" +
            ", districtEnglishName='" + getDistrictEnglishName() + "'" +
            ", county=" + getCounty() +
            "}";
    }
}
