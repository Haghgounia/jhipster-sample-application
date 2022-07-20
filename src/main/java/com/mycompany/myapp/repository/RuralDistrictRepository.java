package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.RuralDistrict;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RuralDistrict entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RuralDistrictRepository extends JpaRepository<RuralDistrict, Long>, JpaSpecificationExecutor<RuralDistrict> {}
