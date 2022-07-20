package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.AssetStatus;
import com.mycompany.myapp.repository.AssetStatusRepository;
import com.mycompany.myapp.service.criteria.AssetStatusCriteria;
import com.mycompany.myapp.service.dto.AssetStatusDTO;
import com.mycompany.myapp.service.mapper.AssetStatusMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link AssetStatus} entities in the database.
 * The main input is a {@link AssetStatusCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AssetStatusDTO} or a {@link Page} of {@link AssetStatusDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AssetStatusQueryService extends QueryService<AssetStatus> {

    private final Logger log = LoggerFactory.getLogger(AssetStatusQueryService.class);

    private final AssetStatusRepository assetStatusRepository;

    private final AssetStatusMapper assetStatusMapper;

    public AssetStatusQueryService(AssetStatusRepository assetStatusRepository, AssetStatusMapper assetStatusMapper) {
        this.assetStatusRepository = assetStatusRepository;
        this.assetStatusMapper = assetStatusMapper;
    }

    /**
     * Return a {@link List} of {@link AssetStatusDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AssetStatusDTO> findByCriteria(AssetStatusCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AssetStatus> specification = createSpecification(criteria);
        return assetStatusMapper.toDto(assetStatusRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AssetStatusDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AssetStatusDTO> findByCriteria(AssetStatusCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AssetStatus> specification = createSpecification(criteria);
        return assetStatusRepository.findAll(specification, page).map(assetStatusMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AssetStatusCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AssetStatus> specification = createSpecification(criteria);
        return assetStatusRepository.count(specification);
    }

    /**
     * Function to convert {@link AssetStatusCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AssetStatus> createSpecification(AssetStatusCriteria criteria) {
        Specification<AssetStatus> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AssetStatus_.id));
            }
            if (criteria.getAssetStatusId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAssetStatusId(), AssetStatus_.assetStatusId));
            }
            if (criteria.getAssetStatusName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAssetStatusName(), AssetStatus_.assetStatusName));
            }
        }
        return specification;
    }
}
