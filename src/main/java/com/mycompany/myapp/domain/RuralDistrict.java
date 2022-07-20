package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RuralDistrict.
 */
@Entity
@Table(name = "rural_district")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RuralDistrict implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "rural_district_id", nullable = false)
    private Integer ruralDistrictId;

    @NotNull
    @Column(name = "rural_district_code", nullable = false, unique = true)
    private String ruralDistrictCode;

    @Column(name = "rural_district_name")
    private String ruralDistrictName;

    @Column(name = "rural_district_english_name")
    private String ruralDistrictEnglishName;

    @ManyToOne
    @JsonIgnoreProperties(value = { "county" }, allowSetters = true)
    private District district;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RuralDistrict id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRuralDistrictId() {
        return this.ruralDistrictId;
    }

    public RuralDistrict ruralDistrictId(Integer ruralDistrictId) {
        this.setRuralDistrictId(ruralDistrictId);
        return this;
    }

    public void setRuralDistrictId(Integer ruralDistrictId) {
        this.ruralDistrictId = ruralDistrictId;
    }

    public String getRuralDistrictCode() {
        return this.ruralDistrictCode;
    }

    public RuralDistrict ruralDistrictCode(String ruralDistrictCode) {
        this.setRuralDistrictCode(ruralDistrictCode);
        return this;
    }

    public void setRuralDistrictCode(String ruralDistrictCode) {
        this.ruralDistrictCode = ruralDistrictCode;
    }

    public String getRuralDistrictName() {
        return this.ruralDistrictName;
    }

    public RuralDistrict ruralDistrictName(String ruralDistrictName) {
        this.setRuralDistrictName(ruralDistrictName);
        return this;
    }

    public void setRuralDistrictName(String ruralDistrictName) {
        this.ruralDistrictName = ruralDistrictName;
    }

    public String getRuralDistrictEnglishName() {
        return this.ruralDistrictEnglishName;
    }

    public RuralDistrict ruralDistrictEnglishName(String ruralDistrictEnglishName) {
        this.setRuralDistrictEnglishName(ruralDistrictEnglishName);
        return this;
    }

    public void setRuralDistrictEnglishName(String ruralDistrictEnglishName) {
        this.ruralDistrictEnglishName = ruralDistrictEnglishName;
    }

    public District getDistrict() {
        return this.district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public RuralDistrict district(District district) {
        this.setDistrict(district);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RuralDistrict)) {
            return false;
        }
        return id != null && id.equals(((RuralDistrict) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RuralDistrict{" +
            "id=" + getId() +
            ", ruralDistrictId=" + getRuralDistrictId() +
            ", ruralDistrictCode='" + getRuralDistrictCode() + "'" +
            ", ruralDistrictName='" + getRuralDistrictName() + "'" +
            ", ruralDistrictEnglishName='" + getRuralDistrictEnglishName() + "'" +
            "}";
    }
}
