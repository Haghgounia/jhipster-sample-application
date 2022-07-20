package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EmployeeContact.
 */
@Entity
@Table(name = "employee_contact")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class EmployeeContact implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "employee_contact_id")
    private Integer employeeContactId;

    @Column(name = "employee_id")
    private Integer employeeId;

    @Column(name = "contact_type")
    private Integer contactType;

    @Column(name = "contact")
    private String contact;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EmployeeContact id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getEmployeeContactId() {
        return this.employeeContactId;
    }

    public EmployeeContact employeeContactId(Integer employeeContactId) {
        this.setEmployeeContactId(employeeContactId);
        return this;
    }

    public void setEmployeeContactId(Integer employeeContactId) {
        this.employeeContactId = employeeContactId;
    }

    public Integer getEmployeeId() {
        return this.employeeId;
    }

    public EmployeeContact employeeId(Integer employeeId) {
        this.setEmployeeId(employeeId);
        return this;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getContactType() {
        return this.contactType;
    }

    public EmployeeContact contactType(Integer contactType) {
        this.setContactType(contactType);
        return this;
    }

    public void setContactType(Integer contactType) {
        this.contactType = contactType;
    }

    public String getContact() {
        return this.contact;
    }

    public EmployeeContact contact(String contact) {
        this.setContact(contact);
        return this;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmployeeContact)) {
            return false;
        }
        return id != null && id.equals(((EmployeeContact) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeeContact{" +
            "id=" + getId() +
            ", employeeContactId=" + getEmployeeContactId() +
            ", employeeId=" + getEmployeeId() +
            ", contactType=" + getContactType() +
            ", contact='" + getContact() + "'" +
            "}";
    }
}
