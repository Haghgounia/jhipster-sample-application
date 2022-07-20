package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Continent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Continent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContinentRepository extends JpaRepository<Continent, Long>, JpaSpecificationExecutor<Continent> {}
