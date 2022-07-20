package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.AssetStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AssetStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AssetStatusRepository extends JpaRepository<AssetStatus, Long>, JpaSpecificationExecutor<AssetStatus> {}
