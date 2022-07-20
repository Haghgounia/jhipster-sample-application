package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A County.
 */
@Entity
@Table(name = "county")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class County implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "county_id", nullable = false)
    private Integer countyId;

    @NotNull
    @Column(name = "county_code", nullable = false, unique = true)
    private String countyCode;

    @Column(name = "county_name")
    private String countyName;

    @Column(name = "county_english_name")
    private String countyEnglishName;

    @ManyToOne
    @JsonIgnoreProperties(value = { "country" }, allowSetters = true)
    private Province province;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public County id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCountyId() {
        return this.countyId;
    }

    public County countyId(Integer countyId) {
        this.setCountyId(countyId);
        return this;
    }

    public void setCountyId(Integer countyId) {
        this.countyId = countyId;
    }

    public String getCountyCode() {
        return this.countyCode;
    }

    public County countyCode(String countyCode) {
        this.setCountyCode(countyCode);
        return this;
    }

    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }

    public String getCountyName() {
        return this.countyName;
    }

    public County countyName(String countyName) {
        this.setCountyName(countyName);
        return this;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getCountyEnglishName() {
        return this.countyEnglishName;
    }

    public County countyEnglishName(String countyEnglishName) {
        this.setCountyEnglishName(countyEnglishName);
        return this;
    }

    public void setCountyEnglishName(String countyEnglishName) {
        this.countyEnglishName = countyEnglishName;
    }

    public Province getProvince() {
        return this.province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public County province(Province province) {
        this.setProvince(province);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof County)) {
            return false;
        }
        return id != null && id.equals(((County) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "County{" +
            "id=" + getId() +
            ", countyId=" + getCountyId() +
            ", countyCode='" + getCountyCode() + "'" +
            ", countyName='" + getCountyName() + "'" +
            ", countyEnglishName='" + getCountyEnglishName() + "'" +
            "}";
    }
}
