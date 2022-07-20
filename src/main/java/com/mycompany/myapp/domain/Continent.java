package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Continent.
 */
@Entity
@Table(name = "continent")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Continent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "continent_id", nullable = false)
    private Integer continentId;

    @NotNull
    @Column(name = "continent_code", nullable = false)
    private String continentCode;

    @NotNull
    @Column(name = "continent_name", nullable = false)
    private String continentName;

    @Column(name = "continent_english_name")
    private String continentEnglishName;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Continent id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getContinentId() {
        return this.continentId;
    }

    public Continent continentId(Integer continentId) {
        this.setContinentId(continentId);
        return this;
    }

    public void setContinentId(Integer continentId) {
        this.continentId = continentId;
    }

    public String getContinentCode() {
        return this.continentCode;
    }

    public Continent continentCode(String continentCode) {
        this.setContinentCode(continentCode);
        return this;
    }

    public void setContinentCode(String continentCode) {
        this.continentCode = continentCode;
    }

    public String getContinentName() {
        return this.continentName;
    }

    public Continent continentName(String continentName) {
        this.setContinentName(continentName);
        return this;
    }

    public void setContinentName(String continentName) {
        this.continentName = continentName;
    }

    public String getContinentEnglishName() {
        return this.continentEnglishName;
    }

    public Continent continentEnglishName(String continentEnglishName) {
        this.setContinentEnglishName(continentEnglishName);
        return this;
    }

    public void setContinentEnglishName(String continentEnglishName) {
        this.continentEnglishName = continentEnglishName;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Continent)) {
            return false;
        }
        return id != null && id.equals(((Continent) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Continent{" +
            "id=" + getId() +
            ", continentId=" + getContinentId() +
            ", continentCode='" + getContinentCode() + "'" +
            ", continentName='" + getContinentName() + "'" +
            ", continentEnglishName='" + getContinentEnglishName() + "'" +
            "}";
    }
}
