package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.ContinentDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Continent}.
 */
public interface ContinentService {
    /**
     * Save a continent.
     *
     * @param continentDTO the entity to save.
     * @return the persisted entity.
     */
    ContinentDTO save(ContinentDTO continentDTO);

    /**
     * Updates a continent.
     *
     * @param continentDTO the entity to update.
     * @return the persisted entity.
     */
    ContinentDTO update(ContinentDTO continentDTO);

    /**
     * Partially updates a continent.
     *
     * @param continentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ContinentDTO> partialUpdate(ContinentDTO continentDTO);

    /**
     * Get all the continents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ContinentDTO> findAll(Pageable pageable);

    /**
     * Get the "id" continent.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ContinentDTO> findOne(Long id);

    /**
     * Delete the "id" continent.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
