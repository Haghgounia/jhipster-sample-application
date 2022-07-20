package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Candidate.
 */
@Entity
@Table(name = "candidate")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Candidate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "job_opening")
    private Integer jobOpening;

    @Column(name = "candidate_status")
    private Integer candidateStatus;

    @Column(name = "candidate_rating")
    private Integer candidateRating;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Candidate id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getJobOpening() {
        return this.jobOpening;
    }

    public Candidate jobOpening(Integer jobOpening) {
        this.setJobOpening(jobOpening);
        return this;
    }

    public void setJobOpening(Integer jobOpening) {
        this.jobOpening = jobOpening;
    }

    public Integer getCandidateStatus() {
        return this.candidateStatus;
    }

    public Candidate candidateStatus(Integer candidateStatus) {
        this.setCandidateStatus(candidateStatus);
        return this;
    }

    public void setCandidateStatus(Integer candidateStatus) {
        this.candidateStatus = candidateStatus;
    }

    public Integer getCandidateRating() {
        return this.candidateRating;
    }

    public Candidate candidateRating(Integer candidateRating) {
        this.setCandidateRating(candidateRating);
        return this;
    }

    public void setCandidateRating(Integer candidateRating) {
        this.candidateRating = candidateRating;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Candidate)) {
            return false;
        }
        return id != null && id.equals(((Candidate) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Candidate{" +
            "id=" + getId() +
            ", jobOpening=" + getJobOpening() +
            ", candidateStatus=" + getCandidateStatus() +
            ", candidateRating=" + getCandidateRating() +
            "}";
    }
}
