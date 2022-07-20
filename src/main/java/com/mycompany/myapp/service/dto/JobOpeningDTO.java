package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.JobOpening} entity.
 */
public class JobOpeningDTO implements Serializable {

    private Long id;

    private String postingTitle;

    private Integer jobStatus;

    private Integer hiringLead;

    private Integer departmentId;

    private Integer employmentType;

    private Integer minimumExperience;

    private String jobDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPostingTitle() {
        return postingTitle;
    }

    public void setPostingTitle(String postingTitle) {
        this.postingTitle = postingTitle;
    }

    public Integer getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(Integer jobStatus) {
        this.jobStatus = jobStatus;
    }

    public Integer getHiringLead() {
        return hiringLead;
    }

    public void setHiringLead(Integer hiringLead) {
        this.hiringLead = hiringLead;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(Integer employmentType) {
        this.employmentType = employmentType;
    }

    public Integer getMinimumExperience() {
        return minimumExperience;
    }

    public void setMinimumExperience(Integer minimumExperience) {
        this.minimumExperience = minimumExperience;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JobOpeningDTO)) {
            return false;
        }

        JobOpeningDTO jobOpeningDTO = (JobOpeningDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, jobOpeningDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JobOpeningDTO{" +
            "id=" + getId() +
            ", postingTitle='" + getPostingTitle() + "'" +
            ", jobStatus=" + getJobStatus() +
            ", hiringLead=" + getHiringLead() +
            ", departmentId=" + getDepartmentId() +
            ", employmentType=" + getEmploymentType() +
            ", minimumExperience=" + getMinimumExperience() +
            ", jobDescription='" + getJobDescription() + "'" +
            "}";
    }
}
