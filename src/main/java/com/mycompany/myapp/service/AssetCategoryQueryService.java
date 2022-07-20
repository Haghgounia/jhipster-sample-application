package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.AssetCategory;
import com.mycompany.myapp.repository.AssetCategoryRepository;
import com.mycompany.myapp.service.criteria.AssetCategoryCriteria;
import com.mycompany.myapp.service.dto.AssetCategoryDTO;
import com.mycompany.myapp.service.mapper.AssetCategoryMapper;
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
 * Service for executing complex queries for {@link AssetCategory} entities in the database.
 * The main input is a {@link AssetCategoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AssetCategoryDTO} or a {@link Page} of {@link AssetCategoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AssetCategoryQueryService extends QueryService<AssetCategory> {

    private final Logger log = LoggerFactory.getLogger(AssetCategoryQueryService.class);

    private final AssetCategoryRepository assetCategoryRepository;

    private final AssetCategoryMapper assetCategoryMapper;

    public AssetCategoryQueryService(AssetCategoryRepository assetCategoryRepository, AssetCategoryMapper assetCategoryMapper) {
        this.assetCategoryRepository = assetCategoryRepository;
        this.assetCategoryMapper = assetCategoryMapper;
    }

    /**
     * Return a {@link List} of {@link AssetCategoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AssetCategoryDTO> findByCriteria(AssetCategoryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AssetCategory> specification = createSpecification(criteria);
        return assetCategoryMapper.toDto(assetCategoryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AssetCategoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AssetCategoryDTO> findByCriteria(AssetCategoryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AssetCategory> specification = createSpecification(criteria);
        return assetCategoryRepository.findAll(specification, page).map(assetCategoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AssetCategoryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AssetCategory> specification = createSpecification(criteria);
        return assetCategoryRepository.count(specification);
    }

    /**
     * Function to convert {@link AssetCategoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AssetCategory> createSpecification(AssetCategoryCriteria criteria) {
        Specification<AssetCategory> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AssetCategory_.id));
            }
            if (criteria.getAssetCategoryId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAssetCategoryId(), AssetCategory_.assetCategoryId));
            }
            if (criteria.getAssetCategoryName() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getAssetCategoryName(), AssetCategory_.assetCategoryName));
            }
        }
        return specification;
    }
}
