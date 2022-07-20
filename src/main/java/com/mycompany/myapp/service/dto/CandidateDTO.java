package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Candidate} entity.
 */
public class CandidateDTO implements Serializable {

    private Long id;

    private Integer jobOpening;

    private Integer candidateStatus;

    private Integer candidateRating;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getJobOpening() {
        return jobOpening;
    }

    public void setJobOpening(Integer jobOpening) {
        this.jobOpening = jobOpening;
    }

    public Integer getCandidateStatus() {
        return candidateStatus;
    }

    public void setCandidateStatus(Integer candidateStatus) {
        this.candidateStatus = candidateStatus;
    }

    public Integer getCandidateRating() {
        return candidateRating;
    }

    public void setCandidateRating(Integer candidateRating) {
        this.candidateRating = candidateRating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CandidateDTO)) {
            return false;
        }

        CandidateDTO candidateDTO = (CandidateDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, candidateDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CandidateDTO{" +
            "id=" + getId() +
            ", jobOpening=" + getJobOpening() +
            ", candidateStatus=" + getCandidateStatus() +
            ", candidateRating=" + getCandidateRating() +
            "}";
    }
}
