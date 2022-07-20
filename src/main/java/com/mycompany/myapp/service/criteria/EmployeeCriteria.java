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
 * Criteria class for the {@link com.mycompany.myapp.domain.Employee} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.EmployeeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /employees?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class EmployeeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter employeeId;

    private StringFilter firstName;

    private StringFilter lastName;

    private IntegerFilter gender;

    private IntegerFilter maritalStatus;

    private IntegerFilter birthDate;

    private IntegerFilter hireDate;

    private IntegerFilter employmentStatus;

    private IntegerFilter managerId;

    private Boolean distinct;

    public EmployeeCriteria() {}

    public EmployeeCriteria(EmployeeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.employeeId = other.employeeId == null ? null : other.employeeId.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.gender = other.gender == null ? null : other.gender.copy();
        this.maritalStatus = other.maritalStatus == null ? null : other.maritalStatus.copy();
        this.birthDate = other.birthDate == null ? null : other.birthDate.copy();
        this.hireDate = other.hireDate == null ? null : other.hireDate.copy();
        this.employmentStatus = other.employmentStatus == null ? null : other.employmentStatus.copy();
        this.managerId = other.managerId == null ? null : other.managerId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EmployeeCriteria copy() {
        return new EmployeeCriteria(this);
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

    public IntegerFilter getEmployeeId() {
        return employeeId;
    }

    public IntegerFilter employeeId() {
        if (employeeId == null) {
            employeeId = new IntegerFilter();
        }
        return employeeId;
    }

    public void setEmployeeId(IntegerFilter employeeId) {
        this.employeeId = employeeId;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public StringFilter firstName() {
        if (firstName == null) {
            firstName = new StringFilter();
        }
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public StringFilter lastName() {
        if (lastName == null) {
            lastName = new StringFilter();
        }
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public IntegerFilter getGender() {
        return gender;
    }

    public IntegerFilter gender() {
        if (gender == null) {
            gender = new IntegerFilter();
        }
        return gender;
    }

    public void setGender(IntegerFilter gender) {
        this.gender = gender;
    }

    public IntegerFilter getMaritalStatus() {
        return maritalStatus;
    }

    public IntegerFilter maritalStatus() {
        if (maritalStatus == null) {
            maritalStatus = new IntegerFilter();
        }
        return maritalStatus;
    }

    public void setMaritalStatus(IntegerFilter maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public IntegerFilter getBirthDate() {
        return birthDate;
    }

    public IntegerFilter birthDate() {
        if (birthDate == null) {
            birthDate = new IntegerFilter();
        }
        return birthDate;
    }

    public void setBirthDate(IntegerFilter birthDate) {
        this.birthDate = birthDate;
    }

    public IntegerFilter getHireDate() {
        return hireDate;
    }

    public IntegerFilter hireDate() {
        if (hireDate == null) {
            hireDate = new IntegerFilter();
        }
        return hireDate;
    }

    public void setHireDate(IntegerFilter hireDate) {
        this.hireDate = hireDate;
    }

    public IntegerFilter getEmploymentStatus() {
        return employmentStatus;
    }

    public IntegerFilter employmentStatus() {
        if (employmentStatus == null) {
            employmentStatus = new IntegerFilter();
        }
        return employmentStatus;
    }

    public void setEmploymentStatus(IntegerFilter employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public IntegerFilter getManagerId() {
        return managerId;
    }

    public IntegerFilter managerId() {
        if (managerId == null) {
            managerId = new IntegerFilter();
        }
        return managerId;
    }

    public void setManagerId(IntegerFilter managerId) {
        this.managerId = managerId;
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
        final EmployeeCriteria that = (EmployeeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(employeeId, that.employeeId) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(gender, that.gender) &&
            Objects.equals(maritalStatus, that.maritalStatus) &&
            Objects.equals(birthDate, that.birthDate) &&
            Objects.equals(hireDate, that.hireDate) &&
            Objects.equals(employmentStatus, that.employmentStatus) &&
            Objects.equals(managerId, that.managerId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            employeeId,
            firstName,
            lastName,
            gender,
            maritalStatus,
            birthDate,
            hireDate,
            employmentStatus,
            managerId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (employeeId != null ? "employeeId=" + employeeId + ", " : "") +
            (firstName != null ? "firstName=" + firstName + ", " : "") +
            (lastName != null ? "lastName=" + lastName + ", " : "") +
            (gender != null ? "gender=" + gender + ", " : "") +
            (maritalStatus != null ? "maritalStatus=" + maritalStatus + ", " : "") +
            (birthDate != null ? "birthDate=" + birthDate + ", " : "") +
            (hireDate != null ? "hireDate=" + hireDate + ", " : "") +
            (employmentStatus != null ? "employmentStatus=" + employmentStatus + ", " : "") +
            (managerId != null ? "managerId=" + managerId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
