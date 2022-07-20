package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.JobOpening;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the JobOpening entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JobOpeningRepository extends JpaRepository<JobOpening, Long>, JpaSpecificationExecutor<JobOpening> {}
