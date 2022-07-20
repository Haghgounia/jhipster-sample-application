package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.CountyDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.County}.
 */
public interface CountyService {
    /**
     * Save a county.
     *
     * @param countyDTO the entity to save.
     * @return the persisted entity.
     */
    CountyDTO save(CountyDTO countyDTO);

    /**
     * Updates a county.
     *
     * @param countyDTO the entity to update.
     * @return the persisted entity.
     */
    CountyDTO update(CountyDTO countyDTO);

    /**
     * Partially updates a county.
     *
     * @param countyDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CountyDTO> partialUpdate(CountyDTO countyDTO);

    /**
     * Get all the counties.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CountyDTO> findAll(Pageable pageable);

    /**
     * Get the "id" county.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CountyDTO> findOne(Long id);

    /**
     * Delete the "id" county.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
