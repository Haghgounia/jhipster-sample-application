package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.RuralDistrictDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.RuralDistrict}.
 */
public interface RuralDistrictService {
    /**
     * Save a ruralDistrict.
     *
     * @param ruralDistrictDTO the entity to save.
     * @return the persisted entity.
     */
    RuralDistrictDTO save(RuralDistrictDTO ruralDistrictDTO);

    /**
     * Updates a ruralDistrict.
     *
     * @param ruralDistrictDTO the entity to update.
     * @return the persisted entity.
     */
    RuralDistrictDTO update(RuralDistrictDTO ruralDistrictDTO);

    /**
     * Partially updates a ruralDistrict.
     *
     * @param ruralDistrictDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RuralDistrictDTO> partialUpdate(RuralDistrictDTO ruralDistrictDTO);

    /**
     * Get all the ruralDistricts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RuralDistrictDTO> findAll(Pageable pageable);

    /**
     * Get the "id" ruralDistrict.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RuralDistrictDTO> findOne(Long id);

    /**
     * Delete the "id" ruralDistrict.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
