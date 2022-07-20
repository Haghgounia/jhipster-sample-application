package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.AssetCategoryDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.AssetCategory}.
 */
public interface AssetCategoryService {
    /**
     * Save a assetCategory.
     *
     * @param assetCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    AssetCategoryDTO save(AssetCategoryDTO assetCategoryDTO);

    /**
     * Updates a assetCategory.
     *
     * @param assetCategoryDTO the entity to update.
     * @return the persisted entity.
     */
    AssetCategoryDTO update(AssetCategoryDTO assetCategoryDTO);

    /**
     * Partially updates a assetCategory.
     *
     * @param assetCategoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AssetCategoryDTO> partialUpdate(AssetCategoryDTO assetCategoryDTO);

    /**
     * Get all the assetCategories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AssetCategoryDTO> findAll(Pageable pageable);

    /**
     * Get the "id" assetCategory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AssetCategoryDTO> findOne(Long id);

    /**
     * Delete the "id" assetCategory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
