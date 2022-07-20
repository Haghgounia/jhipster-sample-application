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
 * Criteria class for the {@link com.mycompany.myapp.domain.JobOpening} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.JobOpeningResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /job-openings?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class JobOpeningCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter postingTitle;

    private IntegerFilter jobStatus;

    private IntegerFilter hiringLead;

    private IntegerFilter departmentId;

    private IntegerFilter employmentType;

    private IntegerFilter minimumExperience;

    private StringFilter jobDescription;

    private Boolean distinct;

    public JobOpeningCriteria() {}

    public JobOpeningCriteria(JobOpeningCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.postingTitle = other.postingTitle == null ? null : other.postingTitle.copy();
        this.jobStatus = other.jobStatus == null ? null : other.jobStatus.copy();
        this.hiringLead = other.hiringLead == null ? null : other.hiringLead.copy();
        this.departmentId = other.departmentId == null ? null : other.departmentId.copy();
        this.employmentType = other.employmentType == null ? null : other.employmentType.copy();
        this.minimumExperience = other.minimumExperience == null ? null : other.minimumExperience.copy();
        this.jobDescription = other.jobDescription == null ? null : other.jobDescription.copy();
        this.distinct = other.distinct;
    }

    @Override
    public JobOpeningCriteria copy() {
        return new JobOpeningCriteria(this);
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

    public StringFilter getPostingTitle() {
        return postingTitle;
    }

    public StringFilter postingTitle() {
        if (postingTitle == null) {
            postingTitle = new StringFilter();
        }
        return postingTitle;
    }

    public void setPostingTitle(StringFilter postingTitle) {
        this.postingTitle = postingTitle;
    }

    public IntegerFilter getJobStatus() {
        return jobStatus;
    }

    public IntegerFilter jobStatus() {
        if (jobStatus == null) {
            jobStatus = new IntegerFilter();
        }
        return jobStatus;
    }

    public void setJobStatus(IntegerFilter jobStatus) {
        this.jobStatus = jobStatus;
    }

    public IntegerFilter getHiringLead() {
        return hiringLead;
    }

    public IntegerFilter hiringLead() {
        if (hiringLead == null) {
            hiringLead = new IntegerFilter();
        }
        return hiringLead;
    }

    public void setHiringLead(IntegerFilter hiringLead) {
        this.hiringLead = hiringLead;
    }

    public IntegerFilter getDepartmentId() {
        return departmentId;
    }

    public IntegerFilter departmentId() {
        if (departmentId == null) {
            departmentId = new IntegerFilter();
        }
        return departmentId;
    }

    public void setDepartmentId(IntegerFilter departmentId) {
        this.departmentId = departmentId;
    }

    public IntegerFilter getEmploymentType() {
        return employmentType;
    }

    public IntegerFilter employmentType() {
        if (employmentType == null) {
            employmentType = new IntegerFilter();
        }
        return employmentType;
    }

    public void setEmploymentType(IntegerFilter employmentType) {
        this.employmentType = employmentType;
    }

    public IntegerFilter getMinimumExperience() {
        return minimumExperience;
    }

    public IntegerFilter minimumExperience() {
        if (minimumExperience == null) {
            minimumExperience = new IntegerFilter();
        }
        return minimumExperience;
    }

    public void setMinimumExperience(IntegerFilter minimumExperience) {
        this.minimumExperience = minimumExperience;
    }

    public StringFilter getJobDescription() {
        return jobDescription;
    }

    public StringFilter jobDescription() {
        if (jobDescription == null) {
            jobDescription = new StringFilter();
        }
        return jobDescription;
    }

    public void setJobDescription(StringFilter jobDescription) {
        this.jobDescription = jobDescription;
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
        final JobOpeningCriteria that = (JobOpeningCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(postingTitle, that.postingTitle) &&
            Objects.equals(jobStatus, that.jobStatus) &&
            Objects.equals(hiringLead, that.hiringLead) &&
            Objects.equals(departmentId, that.departmentId) &&
            Objects.equals(employmentType, that.employmentType) &&
            Objects.equals(minimumExperience, that.minimumExperience) &&
            Objects.equals(jobDescription, that.jobDescription) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            postingTitle,
            jobStatus,
            hiringLead,
            departmentId,
            employmentType,
            minimumExperience,
            jobDescription,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JobOpeningCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (postingTitle != null ? "postingTitle=" + postingTitle + ", " : "") +
            (jobStatus != null ? "jobStatus=" + jobStatus + ", " : "") +
            (hiringLead != null ? "hiringLead=" + hiringLead + ", " : "") +
            (departmentId != null ? "departmentId=" + departmentId + ", " : "") +
            (employmentType != null ? "employmentType=" + employmentType + ", " : "") +
            (minimumExperience != null ? "minimumExperience=" + minimumExperience + ", " : "") +
            (jobDescription != null ? "jobDescription=" + jobDescription + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
