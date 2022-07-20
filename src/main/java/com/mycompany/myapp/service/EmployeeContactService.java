package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.EmployeeContactDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.EmployeeContact}.
 */
public interface EmployeeContactService {
    /**
     * Save a employeeContact.
     *
     * @param employeeContactDTO the entity to save.
     * @return the persisted entity.
     */
    EmployeeContactDTO save(EmployeeContactDTO employeeContactDTO);

    /**
     * Updates a employeeContact.
     *
     * @param employeeContactDTO the entity to update.
     * @return the persisted entity.
     */
    EmployeeContactDTO update(EmployeeContactDTO employeeContactDTO);

    /**
     * Partially updates a employeeContact.
     *
     * @param employeeContactDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EmployeeContactDTO> partialUpdate(EmployeeContactDTO employeeContactDTO);

    /**
     * Get all the employeeContacts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EmployeeContactDTO> findAll(Pageable pageable);

    /**
     * Get the "id" employeeContact.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EmployeeContactDTO> findOne(Long id);

    /**
     * Delete the "id" employeeContact.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
