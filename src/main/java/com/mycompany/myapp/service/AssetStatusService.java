package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.AssetStatusDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.AssetStatus}.
 */
public interface AssetStatusService {
    /**
     * Save a assetStatus.
     *
     * @param assetStatusDTO the entity to save.
     * @return the persisted entity.
     */
    AssetStatusDTO save(AssetStatusDTO assetStatusDTO);

    /**
     * Updates a assetStatus.
     *
     * @param assetStatusDTO the entity to update.
     * @return the persisted entity.
     */
    AssetStatusDTO update(AssetStatusDTO assetStatusDTO);

    /**
     * Partially updates a assetStatus.
     *
     * @param assetStatusDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AssetStatusDTO> partialUpdate(AssetStatusDTO assetStatusDTO);

    /**
     * Get all the assetStatuses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AssetStatusDTO> findAll(Pageable pageable);

    /**
     * Get the "id" assetStatus.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AssetStatusDTO> findOne(Long id);

    /**
     * Delete the "id" assetStatus.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
