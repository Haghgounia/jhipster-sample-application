package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.County;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the County entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CountyRepository extends JpaRepository<County, Long>, JpaSpecificationExecutor<County> {}
