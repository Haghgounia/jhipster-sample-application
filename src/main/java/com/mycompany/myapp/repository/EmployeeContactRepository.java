package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.EmployeeContact;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the EmployeeContact entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployeeContactRepository extends JpaRepository<EmployeeContact, Long>, JpaSpecificationExecutor<EmployeeContact> {}
