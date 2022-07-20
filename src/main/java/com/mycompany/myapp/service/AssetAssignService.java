package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.AssetAssignDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.AssetAssign}.
 */
public interface AssetAssignService {
    /**
     * Save a assetAssign.
     *
     * @param assetAssignDTO the entity to save.
     * @return the persisted entity.
     */
    AssetAssignDTO save(AssetAssignDTO assetAssignDTO);

    /**
     * Updates a assetAssign.
     *
     * @param assetAssignDTO the entity to update.
     * @return the persisted entity.
     */
    AssetAssignDTO update(AssetAssignDTO assetAssignDTO);

    /**
     * Partially updates a assetAssign.
     *
     * @param assetAssignDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AssetAssignDTO> partialUpdate(AssetAssignDTO assetAssignDTO);

    /**
     * Get all the assetAssigns.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AssetAssignDTO> findAll(Pageable pageable);

    /**
     * Get the "id" assetAssign.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AssetAssignDTO> findOne(Long id);

    /**
     * Delete the "id" assetAssign.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
