package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.AssetStatusRepository;
import com.mycompany.myapp.service.AssetStatusQueryService;
import com.mycompany.myapp.service.AssetStatusService;
import com.mycompany.myapp.service.criteria.AssetStatusCriteria;
import com.mycompany.myapp.service.dto.AssetStatusDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.AssetStatus}.
 */
@RestController
@RequestMapping("/api")
public class AssetStatusResource {

    private final Logger log = LoggerFactory.getLogger(AssetStatusResource.class);

    private static final String ENTITY_NAME = "jhipsterSampleApplicationAssetStatus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AssetStatusService assetStatusService;

    private final AssetStatusRepository assetStatusRepository;

    private final AssetStatusQueryService assetStatusQueryService;

    public AssetStatusResource(
        AssetStatusService assetStatusService,
        AssetStatusRepository assetStatusRepository,
        AssetStatusQueryService assetStatusQueryService
    ) {
        this.assetStatusService = assetStatusService;
        this.assetStatusRepository = assetStatusRepository;
        this.assetStatusQueryService = assetStatusQueryService;
    }

    /**
     * {@code POST  /asset-statuses} : Create a new assetStatus.
     *
     * @param assetStatusDTO the assetStatusDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new assetStatusDTO, or with status {@code 400 (Bad Request)} if the assetStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/asset-statuses")
    public ResponseEntity<AssetStatusDTO> createAssetStatus(@RequestBody AssetStatusDTO assetStatusDTO) throws URISyntaxException {
        log.debug("REST request to save AssetStatus : {}", assetStatusDTO);
        if (assetStatusDTO.getId() != null) {
            throw new BadRequestAlertException("A new assetStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AssetStatusDTO result = assetStatusService.save(assetStatusDTO);
        return ResponseEntity
            .created(new URI("/api/asset-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /asset-statuses/:id} : Updates an existing assetStatus.
     *
     * @param id the id of the assetStatusDTO to save.
     * @param assetStatusDTO the assetStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assetStatusDTO,
     * or with status {@code 400 (Bad Request)} if the assetStatusDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assetStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/asset-statuses/{id}")
    public ResponseEntity<AssetStatusDTO> updateAssetStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AssetStatusDTO assetStatusDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AssetStatus : {}, {}", id, assetStatusDTO);
        if (assetStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assetStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assetStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AssetStatusDTO result = assetStatusService.update(assetStatusDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assetStatusDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /asset-statuses/:id} : Partial updates given fields of an existing assetStatus, field will ignore if it is null
     *
     * @param id the id of the assetStatusDTO to save.
     * @param assetStatusDTO the assetStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assetStatusDTO,
     * or with status {@code 400 (Bad Request)} if the assetStatusDTO is not valid,
     * or with status {@code 404 (Not Found)} if the assetStatusDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the assetStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/asset-statuses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AssetStatusDTO> partialUpdateAssetStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AssetStatusDTO assetStatusDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AssetStatus partially : {}, {}", id, assetStatusDTO);
        if (assetStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assetStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assetStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AssetStatusDTO> result = assetStatusService.partialUpdate(assetStatusDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assetStatusDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /asset-statuses} : get all the assetStatuses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assetStatuses in body.
     */
    @GetMapping("/asset-statuses")
    public ResponseEntity<List<AssetStatusDTO>> getAllAssetStatuses(
        AssetStatusCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get AssetStatuses by criteria: {}", criteria);
        Page<AssetStatusDTO> page = assetStatusQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /asset-statuses/count} : count all the assetStatuses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/asset-statuses/count")
    public ResponseEntity<Long> countAssetStatuses(AssetStatusCriteria criteria) {
        log.debug("REST request to count AssetStatuses by criteria: {}", criteria);
        return ResponseEntity.ok().body(assetStatusQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /asset-statuses/:id} : get the "id" assetStatus.
     *
     * @param id the id of the assetStatusDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assetStatusDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/asset-statuses/{id}")
    public ResponseEntity<AssetStatusDTO> getAssetStatus(@PathVariable Long id) {
        log.debug("REST request to get AssetStatus : {}", id);
        Optional<AssetStatusDTO> assetStatusDTO = assetStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(assetStatusDTO);
    }

    /**
     * {@code DELETE  /asset-statuses/:id} : delete the "id" assetStatus.
     *
     * @param id the id of the assetStatusDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/asset-statuses/{id}")
    public ResponseEntity<Void> deleteAssetStatus(@PathVariable Long id) {
        log.debug("REST request to delete AssetStatus : {}", id);
        assetStatusService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
