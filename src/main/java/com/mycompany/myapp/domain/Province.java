package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Province.
 */
@Entity
@Table(name = "province")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Province implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "province_id", nullable = false)
    private Integer provinceId;

    @NotNull
    @Column(name = "province_code", nullable = false, unique = true)
    private String provinceCode;

    @Column(name = "province_name")
    private String provinceName;

    @Column(name = "province_english_name")
    private String provinceEnglishName;

    @Column(name = "dialling_code")
    private String diallingCode;

    @ManyToOne
    @JsonIgnoreProperties(value = { "continent", "currency", "officialLanguage" }, allowSetters = true)
    private Country country;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Province id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getProvinceId() {
        return this.provinceId;
    }

    public Province provinceId(Integer provinceId) {
        this.setProvinceId(provinceId);
        return this;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceCode() {
        return this.provinceCode;
    }

    public Province provinceCode(String provinceCode) {
        this.setProvinceCode(provinceCode);
        return this;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return this.provinceName;
    }

    public Province provinceName(String provinceName) {
        this.setProvinceName(provinceName);
        return this;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceEnglishName() {
        return this.provinceEnglishName;
    }

    public Province provinceEnglishName(String provinceEnglishName) {
        this.setProvinceEnglishName(provinceEnglishName);
        return this;
    }

    public void setProvinceEnglishName(String provinceEnglishName) {
        this.provinceEnglishName = provinceEnglishName;
    }

    public String getDiallingCode() {
        return this.diallingCode;
    }

    public Province diallingCode(String diallingCode) {
        this.setDiallingCode(diallingCode);
        return this;
    }

    public void setDiallingCode(String diallingCode) {
        this.diallingCode = diallingCode;
    }

    public Country getCountry() {
        return this.country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Province country(Country country) {
        this.setCountry(country);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Province)) {
            return false;
        }
        return id != null && id.equals(((Province) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Province{" +
            "id=" + getId() +
            ", provinceId=" + getProvinceId() +
            ", provinceCode='" + getProvinceCode() + "'" +
            ", provinceName='" + getProvinceName() + "'" +
            ", provinceEnglishName='" + getProvinceEnglishName() + "'" +
            ", diallingCode='" + getDiallingCode() + "'" +
            "}";
    }
}
