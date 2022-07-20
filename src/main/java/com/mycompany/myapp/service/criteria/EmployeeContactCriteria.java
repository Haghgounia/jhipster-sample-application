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
 * Criteria class for the {@link com.mycompany.myapp.domain.EmployeeContact} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.EmployeeContactResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /employee-contacts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class EmployeeContactCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter employeeContactId;

    private IntegerFilter employeeId;

    private IntegerFilter contactType;

    private StringFilter contact;

    private Boolean distinct;

    public EmployeeContactCriteria() {}

    public EmployeeContactCriteria(EmployeeContactCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.employeeContactId = other.employeeContactId == null ? null : other.employeeContactId.copy();
        this.employeeId = other.employeeId == null ? null : other.employeeId.copy();
        this.contactType = other.contactType == null ? null : other.contactType.copy();
        this.contact = other.contact == null ? null : other.contact.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EmployeeContactCriteria copy() {
        return new EmployeeContactCriteria(this);
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

    public IntegerFilter getEmployeeContactId() {
        return employeeContactId;
    }

    public IntegerFilter employeeContactId() {
        if (employeeContactId == null) {
            employeeContactId = new IntegerFilter();
        }
        return employeeContactId;
    }

    public void setEmployeeContactId(IntegerFilter employeeContactId) {
        this.employeeContactId = employeeContactId;
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

    public IntegerFilter getContactType() {
        return contactType;
    }

    public IntegerFilter contactType() {
        if (contactType == null) {
            contactType = new IntegerFilter();
        }
        return contactType;
    }

    public void setContactType(IntegerFilter contactType) {
        this.contactType = contactType;
    }

    public StringFilter getContact() {
        return contact;
    }

    public StringFilter contact() {
        if (contact == null) {
            contact = new StringFilter();
        }
        return contact;
    }

    public void setContact(StringFilter contact) {
        this.contact = contact;
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
        final EmployeeContactCriteria that = (EmployeeContactCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(employeeContactId, that.employeeContactId) &&
            Objects.equals(employeeId, that.employeeId) &&
            Objects.equals(contactType, that.contactType) &&
            Objects.equals(contact, that.contact) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, employeeContactId, employeeId, contactType, contact, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeeContactCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (employeeContactId != null ? "employeeContactId=" + employeeContactId + ", " : "") +
            (employeeId != null ? "employeeId=" + employeeId + ", " : "") +
            (contactType != null ? "contactType=" + contactType + ", " : "") +
            (contact != null ? "contact=" + contact + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
