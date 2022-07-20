package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Candidate} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.CandidateResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /candidates?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class CandidateCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter jobOpening;

    private IntegerFilter candidateStatus;

    private IntegerFilter candidateRating;

    private Boolean distinct;

    public CandidateCriteria() {}

    public CandidateCriteria(CandidateCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.jobOpening = other.jobOpening == null ? null : other.jobOpening.copy();
        this.candidateStatus = other.candidateStatus == null ? null : other.candidateStatus.copy();
        this.candidateRating = other.candidateRating == null ? null : other.candidateRating.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CandidateCriteria copy() {
        return new CandidateCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getJobOpening() {
        return jobOpening;
    }

    public IntegerFilter jobOpening() {
        if (jobOpening == null) {
            jobOpening = new IntegerFilter();
        }
        return jobOpening;
    }

    public void setJobOpening(IntegerFilter jobOpening) {
        this.jobOpening = jobOpening;
    }

    public IntegerFilter getCandidateStatus() {
        return candidateStatus;
    }

    public IntegerFilter candidateStatus() {
        if (candidateStatus == null) {
            candidateStatus = new IntegerFilter();
        }
        return candidateStatus;
    }

    public void setCandidateStatus(IntegerFilter candidateStatus) {
        this.candidateStatus = candidateStatus;
    }

    public IntegerFilter getCandidateRating() {
        return candidateRating;
    }

    public IntegerFilter candidateRating() {
        if (candidateRating == null) {
            candidateRating = new IntegerFilter();
        }
        return candidateRating;
    }

    public void setCandidateRating(IntegerFilter candidateRating) {
        this.candidateRating = candidateRating;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CandidateCriteria that = (CandidateCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(jobOpening, that.jobOpening) &&
            Objects.equals(candidateStatus, that.candidateStatus) &&
            Objects.equals(candidateRating, that.candidateRating) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, jobOpening, candidateStatus, candidateRating, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CandidateCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (jobOpening != null ? "jobOpening=" + jobOpening + ", " : "") +
            (candidateStatus != null ? "candidateStatus=" + candidateStatus + ", " : "") +
            (candidateRating != null ? "candidateRating=" + candidateRating + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
