package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.ContinentRepository;
import com.mycompany.myapp.service.ContinentQueryService;
import com.mycompany.myapp.service.ContinentService;
import com.mycompany.myapp.service.criteria.ContinentCriteria;
import com.mycompany.myapp.service.dto.ContinentDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Continent}.
 */
@RestController
@RequestMapping("/api")
public class ContinentResource {

    private final Logger log = LoggerFactory.getLogger(ContinentResource.class);

    private static final String ENTITY_NAME = "jhipsterSampleApplicationContinent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContinentService continentService;

    private final ContinentRepository continentRepository;

    private final ContinentQueryService continentQueryService;

    public ContinentResource(
        ContinentService continentService,
        ContinentRepository continentRepository,
        ContinentQueryService continentQueryService
    ) {
        this.continentService = continentService;
        this.continentRepository = continentRepository;
        this.continentQueryService = continentQueryService;
    }

    /**
     * {@code POST  /continents} : Create a new continent.
     *
     * @param continentDTO the continentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new continentDTO, or with status {@code 400 (Bad Request)} if the continent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/continents")
    public ResponseEntity<ContinentDTO> createContinent(@Valid @RequestBody ContinentDTO continentDTO) throws URISyntaxException {
        log.debug("REST request to save Continent : {}", continentDTO);
        if (continentDTO.getId() != null) {
            throw new BadRequestAlertException("A new continent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContinentDTO result = continentService.save(continentDTO);
        return ResponseEntity
            .created(new URI("/api/continents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /continents/:id} : Updates an existing continent.
     *
     * @param id the id of the continentDTO to save.
     * @param continentDTO the continentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated continentDTO,
     * or with status {@code 400 (Bad Request)} if the continentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the continentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/continents/{id}")
    public ResponseEntity<ContinentDTO> updateContinent(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContinentDTO continentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Continent : {}, {}", id, continentDTO);
        if (continentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, continentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!continentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ContinentDTO result = continentService.update(continentDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, continentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /continents/:id} : Partial updates given fields of an existing continent, field will ignore if it is null
     *
     * @param id the id of the continentDTO to save.
     * @param continentDTO the continentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated continentDTO,
     * or with status {@code 400 (Bad Request)} if the continentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the continentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the continentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/continents/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContinentDTO> partialUpdateContinent(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ContinentDTO continentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Continent partially : {}, {}", id, continentDTO);
        if (continentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, continentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!continentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContinentDTO> result = continentService.partialUpdate(continentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, continentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /continents} : get all the continents.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of continents in body.
     */
    @GetMapping("/continents")
    public ResponseEntity<List<ContinentDTO>> getAllContinents(
        ContinentCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Continents by criteria: {}", criteria);
        Page<ContinentDTO> page = continentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /continents/count} : count all the continents.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/continents/count")
    public ResponseEntity<Long> countContinents(ContinentCriteria criteria) {
        log.debug("REST request to count Continents by criteria: {}", criteria);
        return ResponseEntity.ok().body(continentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /continents/:id} : get the "id" continent.
     *
     * @param id the id of the continentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the continentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/continents/{id}")
    public ResponseEntity<ContinentDTO> getContinent(@PathVariable Long id) {
        log.debug("REST request to get Continent : {}", id);
        Optional<ContinentDTO> continentDTO = continentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(continentDTO);
    }

    /**
     * {@code DELETE  /continents/:id} : delete the "id" continent.
     *
     * @param id the id of the continentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/continents/{id}")
    public ResponseEntity<Void> deleteContinent(@PathVariable Long id) {
        log.debug("REST request to delete Continent : {}", id);
        continentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
