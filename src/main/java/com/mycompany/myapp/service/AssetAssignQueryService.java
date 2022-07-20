package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.AssetAssign;
import com.mycompany.myapp.repository.AssetAssignRepository;
import com.mycompany.myapp.service.criteria.AssetAssignCriteria;
import com.mycompany.myapp.service.dto.AssetAssignDTO;
import com.mycompany.myapp.service.mapper.AssetAssignMapper;
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
 * Service for executing complex queries for {@link AssetAssign} entities in the database.
 * The main input is a {@link AssetAssignCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AssetAssignDTO} or a {@link Page} of {@link AssetAssignDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AssetAssignQueryService extends QueryService<AssetAssign> {

    private final Logger log = LoggerFactory.getLogger(AssetAssignQueryService.class);

    private final AssetAssignRepository assetAssignRepository;

    private final AssetAssignMapper assetAssignMapper;

    public AssetAssignQueryService(AssetAssignRepository assetAssignRepository, AssetAssignMapper assetAssignMapper) {
        this.assetAssignRepository = assetAssignRepository;
        this.assetAssignMapper = assetAssignMapper;
    }

    /**
     * Return a {@link List} of {@link AssetAssignDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AssetAssignDTO> findByCriteria(AssetAssignCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AssetAssign> specification = createSpecification(criteria);
        return assetAssignMapper.toDto(assetAssignRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AssetAssignDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AssetAssignDTO> findByCriteria(AssetAssignCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AssetAssign> specification = createSpecification(criteria);
        return assetAssignRepository.findAll(specification, page).map(assetAssignMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AssetAssignCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AssetAssign> specification = createSpecification(criteria);
        return assetAssignRepository.count(specification);
    }

    /**
     * Function to convert {@link AssetAssignCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AssetAssign> createSpecification(AssetAssignCriteria criteria) {
        Specification<AssetAssign> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AssetAssign_.id));
            }
            if (criteria.getAssetId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAssetId(), AssetAssign_.assetId));
            }
            if (criteria.getEmployeeId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEmployeeId(), AssetAssign_.employeeId));
            }
            if (criteria.getAssignDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAssignDate(), AssetAssign_.assignDate));
            }
            if (criteria.getReturnDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReturnDate(), AssetAssign_.returnDate));
            }
        }
        return specification;
    }
}
