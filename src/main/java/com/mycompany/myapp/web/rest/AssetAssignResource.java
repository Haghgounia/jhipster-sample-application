package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.AssetAssignRepository;
import com.mycompany.myapp.service.AssetAssignQueryService;
import com.mycompany.myapp.service.AssetAssignService;
import com.mycompany.myapp.service.criteria.AssetAssignCriteria;
import com.mycompany.myapp.service.dto.AssetAssignDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.AssetAssign}.
 */
@RestController
@RequestMapping("/api")
public class AssetAssignResource {

    private final Logger log = LoggerFactory.getLogger(AssetAssignResource.class);

    private static final String ENTITY_NAME = "jhipsterSampleApplicationAssetAssign";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AssetAssignService assetAssignService;

    private final AssetAssignRepository assetAssignRepository;

    private final AssetAssignQueryService assetAssignQueryService;

    public AssetAssignResource(
        AssetAssignService assetAssignService,
        AssetAssignRepository assetAssignRepository,
        AssetAssignQueryService assetAssignQueryService
    ) {
        this.assetAssignService = assetAssignService;
        this.assetAssignRepository = assetAssignRepository;
        this.assetAssignQueryService = assetAssignQueryService;
    }

    /**
     * {@code POST  /asset-assigns} : Create a new assetAssign.
     *
     * @param assetAssignDTO the assetAssignDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new assetAssignDTO, or with status {@code 400 (Bad Request)} if the assetAssign has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/asset-assigns")
    public ResponseEntity<AssetAssignDTO> createAssetAssign(@RequestBody AssetAssignDTO assetAssignDTO) throws URISyntaxException {
        log.debug("REST request to save AssetAssign : {}", assetAssignDTO);
        if (assetAssignDTO.getId() != null) {
            throw new BadRequestAlertException("A new assetAssign cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AssetAssignDTO result = assetAssignService.save(assetAssignDTO);
        return ResponseEntity
            .created(new URI("/api/asset-assigns/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /asset-assigns/:id} : Updates an existing assetAssign.
     *
     * @param id the id of the assetAssignDTO to save.
     * @param assetAssignDTO the assetAssignDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assetAssignDTO,
     * or with status {@code 400 (Bad Request)} if the assetAssignDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assetAssignDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/asset-assigns/{id}")
    public ResponseEntity<AssetAssignDTO> updateAssetAssign(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AssetAssignDTO assetAssignDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AssetAssign : {}, {}", id, assetAssignDTO);
        if (assetAssignDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assetAssignDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assetAssignRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AssetAssignDTO result = assetAssignService.update(assetAssignDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assetAssignDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /asset-assigns/:id} : Partial updates given fields of an existing assetAssign, field will ignore if it is null
     *
     * @param id the id of the assetAssignDTO to save.
     * @param assetAssignDTO the assetAssignDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assetAssignDTO,
     * or with status {@code 400 (Bad Request)} if the assetAssignDTO is not valid,
     * or with status {@code 404 (Not Found)} if the assetAssignDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the assetAssignDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/asset-assigns/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AssetAssignDTO> partialUpdateAssetAssign(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AssetAssignDTO assetAssignDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AssetAssign partially : {}, {}", id, assetAssignDTO);
        if (assetAssignDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assetAssignDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assetAssignRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AssetAssignDTO> result = assetAssignService.partialUpdate(assetAssignDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assetAssignDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /asset-assigns} : get all the assetAssigns.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assetAssigns in body.
     */
    @GetMapping("/asset-assigns")
    public ResponseEntity<List<AssetAssignDTO>> getAllAssetAssigns(
        AssetAssignCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get AssetAssigns by criteria: {}", criteria);
        Page<AssetAssignDTO> page = assetAssignQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /asset-assigns/count} : count all the assetAssigns.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/asset-assigns/count")
    public ResponseEntity<Long> countAssetAssigns(AssetAssignCriteria criteria) {
        log.debug("REST request to count AssetAssigns by criteria: {}", criteria);
        return ResponseEntity.ok().body(assetAssignQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /asset-assigns/:id} : get the "id" assetAssign.
     *
     * @param id the id of the assetAssignDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assetAssignDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/asset-assigns/{id}")
    public ResponseEntity<AssetAssignDTO> getAssetAssign(@PathVariable Long id) {
        log.debug("REST request to get AssetAssign : {}", id);
        Optional<AssetAssignDTO> assetAssignDTO = assetAssignService.findOne(id);
        return ResponseUtil.wrapOrNotFound(assetAssignDTO);
    }

    /**
     * {@code DELETE  /asset-assigns/:id} : delete the "id" assetAssign.
     *
     * @param id the id of the assetAssignDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/asset-assigns/{id}")
    public ResponseEntity<Void> deleteAssetAssign(@PathVariable Long id) {
        log.debug("REST request to delete AssetAssign : {}", id);
        assetAssignService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
