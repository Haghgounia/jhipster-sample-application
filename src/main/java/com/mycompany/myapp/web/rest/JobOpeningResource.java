package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.JobOpeningRepository;
import com.mycompany.myapp.service.JobOpeningQueryService;
import com.mycompany.myapp.service.JobOpeningService;
import com.mycompany.myapp.service.criteria.JobOpeningCriteria;
import com.mycompany.myapp.service.dto.JobOpeningDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.JobOpening}.
 */
@RestController
@RequestMapping("/api")
public class JobOpeningResource {

    private final Logger log = LoggerFactory.getLogger(JobOpeningResource.class);

    private static final String ENTITY_NAME = "jhipsterSampleApplicationJobOpening";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JobOpeningService jobOpeningService;

    private final JobOpeningRepository jobOpeningRepository;

    private final JobOpeningQueryService jobOpeningQueryService;

    public JobOpeningResource(
        JobOpeningService jobOpeningService,
        JobOpeningRepository jobOpeningRepository,
        JobOpeningQueryService jobOpeningQueryService
    ) {
        this.jobOpeningService = jobOpeningService;
        this.jobOpeningRepository = jobOpeningRepository;
        this.jobOpeningQueryService = jobOpeningQueryService;
    }

    /**
     * {@code POST  /job-openings} : Create a new jobOpening.
     *
     * @param jobOpeningDTO the jobOpeningDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new jobOpeningDTO, or with status {@code 400 (Bad Request)} if the jobOpening has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/job-openings")
    public ResponseEntity<JobOpeningDTO> createJobOpening(@RequestBody JobOpeningDTO jobOpeningDTO) throws URISyntaxException {
        log.debug("REST request to save JobOpening : {}", jobOpeningDTO);
        if (jobOpeningDTO.getId() != null) {
            throw new BadRequestAlertException("A new jobOpening cannot already have an ID", ENTITY_NAME, "idexists");
        }
        JobOpeningDTO result = jobOpeningService.save(jobOpeningDTO);
        return ResponseEntity
            .created(new URI("/api/job-openings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /job-openings/:id} : Updates an existing jobOpening.
     *
     * @param id the id of the jobOpeningDTO to save.
     * @param jobOpeningDTO the jobOpeningDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jobOpeningDTO,
     * or with status {@code 400 (Bad Request)} if the jobOpeningDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the jobOpeningDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/job-openings/{id}")
    public ResponseEntity<JobOpeningDTO> updateJobOpening(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody JobOpeningDTO jobOpeningDTO
    ) throws URISyntaxException {
        log.debug("REST request to update JobOpening : {}, {}", id, jobOpeningDTO);
        if (jobOpeningDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, jobOpeningDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!jobOpeningRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        JobOpeningDTO result = jobOpeningService.update(jobOpeningDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, jobOpeningDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /job-openings/:id} : Partial updates given fields of an existing jobOpening, field will ignore if it is null
     *
     * @param id the id of the jobOpeningDTO to save.
     * @param jobOpeningDTO the jobOpeningDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jobOpeningDTO,
     * or with status {@code 400 (Bad Request)} if the jobOpeningDTO is not valid,
     * or with status {@code 404 (Not Found)} if the jobOpeningDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the jobOpeningDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/job-openings/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<JobOpeningDTO> partialUpdateJobOpening(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody JobOpeningDTO jobOpeningDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update JobOpening partially : {}, {}", id, jobOpeningDTO);
        if (jobOpeningDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, jobOpeningDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!jobOpeningRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<JobOpeningDTO> result = jobOpeningService.partialUpdate(jobOpeningDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, jobOpeningDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /job-openings} : get all the jobOpenings.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of jobOpenings in body.
     */
    @GetMapping("/job-openings")
    public ResponseEntity<List<JobOpeningDTO>> getAllJobOpenings(
        JobOpeningCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get JobOpenings by criteria: {}", criteria);
        Page<JobOpeningDTO> page = jobOpeningQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /job-openings/count} : count all the jobOpenings.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/job-openings/count")
    public ResponseEntity<Long> countJobOpenings(JobOpeningCriteria criteria) {
        log.debug("REST request to count JobOpenings by criteria: {}", criteria);
        return ResponseEntity.ok().body(jobOpeningQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /job-openings/:id} : get the "id" jobOpening.
     *
     * @param id the id of the jobOpeningDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the jobOpeningDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/job-openings/{id}")
    public ResponseEntity<JobOpeningDTO> getJobOpening(@PathVariable Long id) {
        log.debug("REST request to get JobOpening : {}", id);
        Optional<JobOpeningDTO> jobOpeningDTO = jobOpeningService.findOne(id);
        return ResponseUtil.wrapOrNotFound(jobOpeningDTO);
    }

    /**
     * {@code DELETE  /job-openings/:id} : delete the "id" jobOpening.
     *
     * @param id the id of the jobOpeningDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/job-openings/{id}")
    public ResponseEntity<Void> deleteJobOpening(@PathVariable Long id) {
        log.debug("REST request to delete JobOpening : {}", id);
        jobOpeningService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
