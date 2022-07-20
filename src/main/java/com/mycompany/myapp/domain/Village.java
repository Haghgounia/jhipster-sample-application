package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Village.
 */
@Entity
@Table(name = "village")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Village implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "village_id", nullable = false)
    private Integer villageId;

    @NotNull
    @Column(name = "village_code", nullable = false, unique = true)
    private String villageCode;

    @Column(name = "village_name")
    private String villageName;

    @Column(name = "village_english_name")
    private String villageEnglishName;

    @ManyToOne
    @JsonIgnoreProperties(value = { "district" }, allowSetters = true)
    private RuralDistrict ruralDistrict;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Village id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVillageId() {
        return this.villageId;
    }

    public Village villageId(Integer villageId) {
        this.setVillageId(villageId);
        return this;
    }

    public void setVillageId(Integer villageId) {
        this.villageId = villageId;
    }

    public String getVillageCode() {
        return this.villageCode;
    }

    public Village villageCode(String villageCode) {
        this.setVillageCode(villageCode);
        return this;
    }

    public void setVillageCode(String villageCode) {
        this.villageCode = villageCode;
    }

    public String getVillageName() {
        return this.villageName;
    }

    public Village villageName(String villageName) {
        this.setVillageName(villageName);
        return this;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    public String getVillageEnglishName() {
        return this.villageEnglishName;
    }

    public Village villageEnglishName(String villageEnglishName) {
        this.setVillageEnglishName(villageEnglishName);
        return this;
    }

    public void setVillageEnglishName(String villageEnglishName) {
        this.villageEnglishName = villageEnglishName;
    }

    public RuralDistrict getRuralDistrict() {
        return this.ruralDistrict;
    }

    public void setRuralDistrict(RuralDistrict ruralDistrict) {
        this.ruralDistrict = ruralDistrict;
    }

    public Village ruralDistrict(RuralDistrict ruralDistrict) {
        this.setRuralDistrict(ruralDistrict);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Village)) {
            return false;
        }
        return id != null && id.equals(((Village) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Village{" +
            "id=" + getId() +
            ", villageId=" + getVillageId() +
            ", villageCode='" + getVillageCode() + "'" +
            ", villageName='" + getVillageName() + "'" +
            ", villageEnglishName='" + getVillageEnglishName() + "'" +
            "}";
    }
}
