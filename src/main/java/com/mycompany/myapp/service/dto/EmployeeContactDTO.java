package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.EmployeeContact} entity.
 */
public class EmployeeContactDTO implements Serializable {

    private Long id;

    private Integer employeeContactId;

    private Integer employeeId;

    private Integer contactType;

    private String contact;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getEmployeeContactId() {
        return employeeContactId;
    }

    public void setEmployeeContactId(Integer employeeContactId) {
        this.employeeContactId = employeeContactId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getContactType() {
        return contactType;
    }

    public void setContactType(Integer contactType) {
        this.contactType = contactType;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmployeeContactDTO)) {
            return false;
        }

        EmployeeContactDTO employeeContactDTO = (EmployeeContactDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, employeeContactDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeeContactDTO{" +
            "id=" + getId() +
            ", employeeContactId=" + getEmployeeContactId() +
            ", employeeId=" + getEmployeeId() +
            ", contactType=" + getContactType() +
            ", contact='" + getContact() + "'" +
            "}";
    }
}
