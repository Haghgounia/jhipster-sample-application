package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Village;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Village entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VillageRepository extends JpaRepository<Village, Long>, JpaSpecificationExecutor<Village> {}
