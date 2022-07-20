package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.RuralDistrictRepository;
import com.mycompany.myapp.service.RuralDistrictQueryService;
import com.mycompany.myapp.service.RuralDistrictService;
import com.mycompany.myapp.service.criteria.RuralDistrictCriteria;
import com.mycompany.myapp.service.dto.RuralDistrictDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.RuralDistrict}.
 */
@RestController
@RequestMapping("/api")
public class RuralDistrictResource {

    private final Logger log = LoggerFactory.getLogger(RuralDistrictResource.class);

    private static final String ENTITY_NAME = "jhipsterSampleApplicationRuralDistrict";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RuralDistrictService ruralDistrictService;

    private final RuralDistrictRepository ruralDistrictRepository;

    private final RuralDistrictQueryService ruralDistrictQueryService;

    public RuralDistrictResource(
        RuralDistrictService ruralDistrictService,
        RuralDistrictRepository ruralDistrictRepository,
        RuralDistrictQueryService ruralDistrictQueryService
    ) {
        this.ruralDistrictService = ruralDistrictService;
        this.ruralDistrictRepository = ruralDistrictRepository;
        this.ruralDistrictQueryService = ruralDistrictQueryService;
    }

    /**
     * {@code POST  /rural-districts} : Create a new ruralDistrict.
     *
     * @param ruralDistrictDTO the ruralDistrictDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ruralDistrictDTO, or with status {@code 400 (Bad Request)} if the ruralDistrict has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rural-districts")
    public ResponseEntity<RuralDistrictDTO> createRuralDistrict(@Valid @RequestBody RuralDistrictDTO ruralDistrictDTO)
        throws URISyntaxException {
        log.debug("REST request to save RuralDistrict : {}", ruralDistrictDTO);
        if (ruralDistrictDTO.getId() != null) {
            throw new BadRequestAlertException("A new ruralDistrict cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RuralDistrictDTO result = ruralDistrictService.save(ruralDistrictDTO);
        return ResponseEntity
            .created(new URI("/api/rural-districts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rural-districts/:id} : Updates an existing ruralDistrict.
     *
     * @param id the id of the ruralDistrictDTO to save.
     * @param ruralDistrictDTO the ruralDistrictDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ruralDistrictDTO,
     * or with status {@code 400 (Bad Request)} if the ruralDistrictDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ruralDistrictDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rural-districts/{id}")
    public ResponseEntity<RuralDistrictDTO> updateRuralDistrict(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RuralDistrictDTO ruralDistrictDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RuralDistrict : {}, {}", id, ruralDistrictDTO);
        if (ruralDistrictDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ruralDistrictDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ruralDistrictRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RuralDistrictDTO result = ruralDistrictService.update(ruralDistrictDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ruralDistrictDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /rural-districts/:id} : Partial updates given fields of an existing ruralDistrict, field will ignore if it is null
     *
     * @param id the id of the ruralDistrictDTO to save.
     * @param ruralDistrictDTO the ruralDistrictDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ruralDistrictDTO,
     * or with status {@code 400 (Bad Request)} if the ruralDistrictDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ruralDistrictDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ruralDistrictDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rural-districts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RuralDistrictDTO> partialUpdateRuralDistrict(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RuralDistrictDTO ruralDistrictDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RuralDistrict partially : {}, {}", id, ruralDistrictDTO);
        if (ruralDistrictDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ruralDistrictDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ruralDistrictRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RuralDistrictDTO> result = ruralDistrictService.partialUpdate(ruralDistrictDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ruralDistrictDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /rural-districts} : get all the ruralDistricts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ruralDistricts in body.
     */
    @GetMapping("/rural-districts")
    public ResponseEntity<List<RuralDistrictDTO>> getAllRuralDistricts(
        RuralDistrictCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get RuralDistricts by criteria: {}", criteria);
        Page<RuralDistrictDTO> page = ruralDistrictQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rural-districts/count} : count all the ruralDistricts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/rural-districts/count")
    public ResponseEntity<Long> countRuralDistricts(RuralDistrictCriteria criteria) {
        log.debug("REST request to count RuralDistricts by criteria: {}", criteria);
        return ResponseEntity.ok().body(ruralDistrictQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /rural-districts/:id} : get the "id" ruralDistrict.
     *
     * @param id the id of the ruralDistrictDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ruralDistrictDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rural-districts/{id}")
    public ResponseEntity<RuralDistrictDTO> getRuralDistrict(@PathVariable Long id) {
        log.debug("REST request to get RuralDistrict : {}", id);
        Optional<RuralDistrictDTO> ruralDistrictDTO = ruralDistrictService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ruralDistrictDTO);
    }

    /**
     * {@code DELETE  /rural-districts/:id} : delete the "id" ruralDistrict.
     *
     * @param id the id of the ruralDistrictDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rural-districts/{id}")
    public ResponseEntity<Void> deleteRuralDistrict(@PathVariable Long id) {
        log.debug("REST request to delete RuralDistrict : {}", id);
        ruralDistrictService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
