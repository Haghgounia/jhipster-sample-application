package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.County;
import com.mycompany.myapp.repository.CountyRepository;
import com.mycompany.myapp.service.criteria.CountyCriteria;
import com.mycompany.myapp.service.dto.CountyDTO;
import com.mycompany.myapp.service.mapper.CountyMapper;
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
 * Service for executing complex queries for {@link County} entities in the database.
 * The main input is a {@link CountyCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CountyDTO} or a {@link Page} of {@link CountyDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CountyQueryService extends QueryService<County> {

    private final Logger log = LoggerFactory.getLogger(CountyQueryService.class);

    private final CountyRepository countyRepository;

    private final CountyMapper countyMapper;

    public CountyQueryService(CountyRepository countyRepository, CountyMapper countyMapper) {
        this.countyRepository = countyRepository;
        this.countyMapper = countyMapper;
    }

    /**
     * Return a {@link List} of {@link CountyDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CountyDTO> findByCriteria(CountyCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<County> specification = createSpecification(criteria);
        return countyMapper.toDto(countyRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CountyDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CountyDTO> findByCriteria(CountyCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<County> specification = createSpecification(criteria);
        return countyRepository.findAll(specification, page).map(countyMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CountyCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<County> specification = createSpecification(criteria);
        return countyRepository.count(specification);
    }

    /**
     * Function to convert {@link CountyCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<County> createSpecification(CountyCriteria criteria) {
        Specification<County> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), County_.id));
            }
            if (criteria.getCountyId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCountyId(), County_.countyId));
            }
            if (criteria.getCountyCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountyCode(), County_.countyCode));
            }
            if (criteria.getCountyName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountyName(), County_.countyName));
            }
            if (criteria.getCountyEnglishName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountyEnglishName(), County_.countyEnglishName));
            }
            if (criteria.getProvinceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getProvinceId(), root -> root.join(County_.province, JoinType.LEFT).get(Province_.id))
                    );
            }
        }
        return specification;
    }
}
