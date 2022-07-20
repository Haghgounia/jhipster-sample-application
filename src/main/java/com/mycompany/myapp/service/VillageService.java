package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.VillageDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Village}.
 */
public interface VillageService {
    /**
     * Save a village.
     *
     * @param villageDTO the entity to save.
     * @return the persisted entity.
     */
    VillageDTO save(VillageDTO villageDTO);

    /**
     * Updates a village.
     *
     * @param villageDTO the entity to update.
     * @return the persisted entity.
     */
    VillageDTO update(VillageDTO villageDTO);

    /**
     * Partially updates a village.
     *
     * @param villageDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<VillageDTO> partialUpdate(VillageDTO villageDTO);

    /**
     * Get all the villages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VillageDTO> findAll(Pageable pageable);

    /**
     * Get the "id" village.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VillageDTO> findOne(Long id);

    /**
     * Delete the "id" village.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
