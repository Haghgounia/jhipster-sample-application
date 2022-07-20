package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Village} entity.
 */
public class VillageDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer villageId;

    @NotNull
    private String villageCode;

    private String villageName;

    private String villageEnglishName;

    private RuralDistrictDTO ruralDistrict;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVillageId() {
        return villageId;
    }

    public void setVillageId(Integer villageId) {
        this.villageId = villageId;
    }

    public String getVillageCode() {
        return villageCode;
    }

    public void setVillageCode(String villageCode) {
        this.villageCode = villageCode;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    public String getVillageEnglishName() {
        return villageEnglishName;
    }

    public void setVillageEnglishName(String villageEnglishName) {
        this.villageEnglishName = villageEnglishName;
    }

    public RuralDistrictDTO getRuralDistrict() {
        return ruralDistrict;
    }

    public void setRuralDistrict(RuralDistrictDTO ruralDistrict) {
        this.ruralDistrict = ruralDistrict;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VillageDTO)) {
            return false;
        }

        VillageDTO villageDTO = (VillageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, villageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VillageDTO{" +
            "id=" + getId() +
            ", villageId=" + getVillageId() +
            ", villageCode='" + getVillageCode() + "'" +
            ", villageName='" + getVillageName() + "'" +
            ", villageEnglishName='" + getVillageEnglishName() + "'" +
            ", ruralDistrict=" + getRuralDistrict() +
            "}";
    }
}
