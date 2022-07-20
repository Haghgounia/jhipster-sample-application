package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A JobOpening.
 */
@Entity
@Table(name = "job_opening")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class JobOpening implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "posting_title")
    private String postingTitle;

    @Column(name = "job_status")
    private Integer jobStatus;

    @Column(name = "hiring_lead")
    private Integer hiringLead;

    @Column(name = "department_id")
    private Integer departmentId;

    @Column(name = "employment_type")
    private Integer employmentType;

    @Column(name = "minimum_experience")
    private Integer minimumExperience;

    @Column(name = "job_description")
    private String jobDescription;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public JobOpening id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPostingTitle() {
        return this.postingTitle;
    }

    public JobOpening postingTitle(String postingTitle) {
        this.setPostingTitle(postingTitle);
        return this;
    }

    public void setPostingTitle(String postingTitle) {
        this.postingTitle = postingTitle;
    }

    public Integer getJobStatus() {
        return this.jobStatus;
    }

    public JobOpening jobStatus(Integer jobStatus) {
        this.setJobStatus(jobStatus);
        return this;
    }

    public void setJobStatus(Integer jobStatus) {
        this.jobStatus = jobStatus;
    }

    public Integer getHiringLead() {
        return this.hiringLead;
    }

    public JobOpening hiringLead(Integer hiringLead) {
        this.setHiringLead(hiringLead);
        return this;
    }

    public void setHiringLead(Integer hiringLead) {
        this.hiringLead = hiringLead;
    }

    public Integer getDepartmentId() {
        return this.departmentId;
    }

    public JobOpening departmentId(Integer departmentId) {
        this.setDepartmentId(departmentId);
        return this;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getEmploymentType() {
        return this.employmentType;
    }

    public JobOpening employmentType(Integer employmentType) {
        this.setEmploymentType(employmentType);
        return this;
    }

    public void setEmploymentType(Integer employmentType) {
        this.employmentType = employmentType;
    }

    public Integer getMinimumExperience() {
        return this.minimumExperience;
    }

    public JobOpening minimumExperience(Integer minimumExperience) {
        this.setMinimumExperience(minimumExperience);
        return this;
    }

    public void setMinimumExperience(Integer minimumExperience) {
        this.minimumExperience = minimumExperience;
    }

    public String getJobDescription() {
        return this.jobDescription;
    }

    public JobOpening jobDescription(String jobDescription) {
        this.setJobDescription(jobDescription);
        return this;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JobOpening)) {
            return false;
        }
        return id != null && id.equals(((JobOpening) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JobOpening{" +
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
