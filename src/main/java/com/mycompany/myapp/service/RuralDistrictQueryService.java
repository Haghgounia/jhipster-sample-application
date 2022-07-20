package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.RuralDistrict;
import com.mycompany.myapp.repository.RuralDistrictRepository;
import com.mycompany.myapp.service.criteria.RuralDistrictCriteria;
import com.mycompany.myapp.service.dto.RuralDistrictDTO;
import com.mycompany.myapp.service.mapper.RuralDistrictMapper;
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
 * Service for executing complex queries for {@link RuralDistrict} entities in the database.
 * The main input is a {@link RuralDistrictCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RuralDistrictDTO} or a {@link Page} of {@link RuralDistrictDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RuralDistrictQueryService extends QueryService<RuralDistrict> {

    private final Logger log = LoggerFactory.getLogger(RuralDistrictQueryService.class);

    private final RuralDistrictRepository ruralDistrictRepository;

    private final RuralDistrictMapper ruralDistrictMapper;

    public RuralDistrictQueryService(RuralDistrictRepository ruralDistrictRepository, RuralDistrictMapper ruralDistrictMapper) {
        this.ruralDistrictRepository = ruralDistrictRepository;
        this.ruralDistrictMapper = ruralDistrictMapper;
    }

    /**
     * Return a {@link List} of {@link RuralDistrictDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RuralDistrictDTO> findByCriteria(RuralDistrictCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<RuralDistrict> specification = createSpecification(criteria);
        return ruralDistrictMapper.toDto(ruralDistrictRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RuralDistrictDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RuralDistrictDTO> findByCriteria(RuralDistrictCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RuralDistrict> specification = createSpecification(criteria);
        return ruralDistrictRepository.findAll(specification, page).map(ruralDistrictMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RuralDistrictCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<RuralDistrict> specification = createSpecification(criteria);
        return ruralDistrictRepository.count(specification);
    }

    /**
     * Function to convert {@link RuralDistrictCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<RuralDistrict> createSpecification(RuralDistrictCriteria criteria) {
        Specification<RuralDistrict> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), RuralDistrict_.id));
            }
            if (criteria.getRuralDistrictId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRuralDistrictId(), RuralDistrict_.ruralDistrictId));
            }
            if (criteria.getRuralDistrictCode() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getRuralDistrictCode(), RuralDistrict_.ruralDistrictCode));
            }
            if (criteria.getRuralDistrictName() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getRuralDistrictName(), RuralDistrict_.ruralDistrictName));
            }
            if (criteria.getRuralDistrictEnglishName() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getRuralDistrictEnglishName(), RuralDistrict_.ruralDistrictEnglishName)
                    );
            }
            if (criteria.getDistrictId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDistrictId(),
                            root -> root.join(RuralDistrict_.district, JoinType.LEFT).get(District_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
