package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.EmployeeContactRepository;
import com.mycompany.myapp.service.EmployeeContactQueryService;
import com.mycompany.myapp.service.EmployeeContactService;
import com.mycompany.myapp.service.criteria.EmployeeContactCriteria;
import com.mycompany.myapp.service.dto.EmployeeContactDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.EmployeeContact}.
 */
@RestController
@RequestMapping("/api")
public class EmployeeContactResource {

    private final Logger log = LoggerFactory.getLogger(EmployeeContactResource.class);

    private static final String ENTITY_NAME = "jhipsterSampleApplicationEmployeeContact";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmployeeContactService employeeContactService;

    private final EmployeeContactRepository employeeContactRepository;

    private final EmployeeContactQueryService employeeContactQueryService;

    public EmployeeContactResource(
        EmployeeContactService employeeContactService,
        EmployeeContactRepository employeeContactRepository,
        EmployeeContactQueryService employeeContactQueryService
    ) {
        this.employeeContactService = employeeContactService;
        this.employeeContactRepository = employeeContactRepository;
        this.employeeContactQueryService = employeeContactQueryService;
    }

    /**
     * {@code POST  /employee-contacts} : Create a new employeeContact.
     *
     * @param employeeContactDTO the employeeContactDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new employeeContactDTO, or with status {@code 400 (Bad Request)} if the employeeContact has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/employee-contacts")
    public ResponseEntity<EmployeeContactDTO> createEmployeeContact(@RequestBody EmployeeContactDTO employeeContactDTO)
        throws URISyntaxException {
        log.debug("REST request to save EmployeeContact : {}", employeeContactDTO);
        if (employeeContactDTO.getId() != null) {
            throw new BadRequestAlertException("A new employeeContact cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EmployeeContactDTO result = employeeContactService.save(employeeContactDTO);
        return ResponseEntity
            .created(new URI("/api/employee-contacts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /employee-contacts/:id} : Updates an existing employeeContact.
     *
     * @param id the id of the employeeContactDTO to save.
     * @param employeeContactDTO the employeeContactDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeeContactDTO,
     * or with status {@code 400 (Bad Request)} if the employeeContactDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the employeeContactDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/employee-contacts/{id}")
    public ResponseEntity<EmployeeContactDTO> updateEmployeeContact(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EmployeeContactDTO employeeContactDTO
    ) throws URISyntaxException {
        log.debug("REST request to update EmployeeContact : {}, {}", id, employeeContactDTO);
        if (employeeContactDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employeeContactDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employeeContactRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EmployeeContactDTO result = employeeContactService.update(employeeContactDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employeeContactDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /employee-contacts/:id} : Partial updates given fields of an existing employeeContact, field will ignore if it is null
     *
     * @param id the id of the employeeContactDTO to save.
     * @param employeeContactDTO the employeeContactDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeeContactDTO,
     * or with status {@code 400 (Bad Request)} if the employeeContactDTO is not valid,
     * or with status {@code 404 (Not Found)} if the employeeContactDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the employeeContactDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/employee-contacts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EmployeeContactDTO> partialUpdateEmployeeContact(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EmployeeContactDTO employeeContactDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update EmployeeContact partially : {}, {}", id, employeeContactDTO);
        if (employeeContactDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employeeContactDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employeeContactRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmployeeContactDTO> result = employeeContactService.partialUpdate(employeeContactDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employeeContactDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /employee-contacts} : get all the employeeContacts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employeeContacts in body.
     */
    @GetMapping("/employee-contacts")
    public ResponseEntity<List<EmployeeContactDTO>> getAllEmployeeContacts(
        EmployeeContactCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get EmployeeContacts by criteria: {}", criteria);
        Page<EmployeeContactDTO> page = employeeContactQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /employee-contacts/count} : count all the employeeContacts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/employee-contacts/count")
    public ResponseEntity<Long> countEmployeeContacts(EmployeeContactCriteria criteria) {
        log.debug("REST request to count EmployeeContacts by criteria: {}", criteria);
        return ResponseEntity.ok().body(employeeContactQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /employee-contacts/:id} : get the "id" employeeContact.
     *
     * @param id the id of the employeeContactDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the employeeContactDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/employee-contacts/{id}")
    public ResponseEntity<EmployeeContactDTO> getEmployeeContact(@PathVariable Long id) {
        log.debug("REST request to get EmployeeContact : {}", id);
        Optional<EmployeeContactDTO> employeeContactDTO = employeeContactService.findOne(id);
        return ResponseUtil.wrapOrNotFound(employeeContactDTO);
    }

    /**
     * {@code DELETE  /employee-contacts/:id} : delete the "id" employeeContact.
     *
     * @param id the id of the employeeContactDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/employee-contacts/{id}")
    public ResponseEntity<Void> deleteEmployeeContact(@PathVariable Long id) {
        log.debug("REST request to delete EmployeeContact : {}", id);
        employeeContactService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
