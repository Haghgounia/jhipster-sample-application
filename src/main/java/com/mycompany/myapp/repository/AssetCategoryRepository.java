package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.AssetCategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AssetCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AssetCategoryRepository extends JpaRepository<AssetCategory, Long>, JpaSpecificationExecutor<AssetCategory> {}
