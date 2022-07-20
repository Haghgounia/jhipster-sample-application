package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.AssetAssign;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AssetAssign entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AssetAssignRepository extends JpaRepository<AssetAssign, Long>, JpaSpecificationExecutor<AssetAssign> {}
