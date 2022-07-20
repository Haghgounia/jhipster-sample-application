package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.JobOpeningDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.JobOpening}.
 */
public interface JobOpeningService {
    /**
     * Save a jobOpening.
     *
     * @param jobOpeningDTO the entity to save.
     * @return the persisted entity.
     */
    JobOpeningDTO save(JobOpeningDTO jobOpeningDTO);

    /**
     * Updates a jobOpening.
     *
     * @param jobOpeningDTO the entity to update.
     * @return the persisted entity.
     */
    JobOpeningDTO update(JobOpeningDTO jobOpeningDTO);

    /**
     * Partially updates a jobOpening.
     *
     * @param jobOpeningDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<JobOpeningDTO> partialUpdate(JobOpeningDTO jobOpeningDTO);

    /**
     * Get all the jobOpenings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<JobOpeningDTO> findAll(Pageable pageable);

    /**
     * Get the "id" jobOpening.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<JobOpeningDTO> findOne(Long id);

    /**
     * Delete the "id" jobOpening.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
