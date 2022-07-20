package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Continent;
import com.mycompany.myapp.repository.ContinentRepository;
import com.mycompany.myapp.service.criteria.ContinentCriteria;
import com.mycompany.myapp.service.dto.ContinentDTO;
import com.mycompany.myapp.service.mapper.ContinentMapper;
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
 * Service for executing complex queries for {@link Continent} entities in the database.
 * The main input is a {@link ContinentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ContinentDTO} or a {@link Page} of {@link ContinentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ContinentQueryService extends QueryService<Continent> {

    private final Logger log = LoggerFactory.getLogger(ContinentQueryService.class);

    private final ContinentRepository continentRepository;

    private final ContinentMapper continentMapper;

    public ContinentQueryService(ContinentRepository continentRepository, ContinentMapper continentMapper) {
        this.continentRepository = continentRepository;
        this.continentMapper = continentMapper;
    }

    /**
     * Return a {@link List} of {@link ContinentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ContinentDTO> findByCriteria(ContinentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Continent> specification = createSpecification(criteria);
        return continentMapper.toDto(continentRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ContinentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ContinentDTO> findByCriteria(ContinentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Continent> specification = createSpecification(criteria);
        return continentRepository.findAll(specification, page).map(continentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ContinentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Continent> specification = createSpecification(criteria);
        return continentRepository.count(specification);
    }

    /**
     * Function to convert {@link ContinentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Continent> createSpecification(ContinentCriteria criteria) {
        Specification<Continent> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Continent_.id));
            }
            if (criteria.getContinentId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getContinentId(), Continent_.continentId));
            }
            if (criteria.getContinentCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContinentCode(), Continent_.continentCode));
            }
            if (criteria.getContinentName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContinentName(), Continent_.continentName));
            }
            if (criteria.getContinentEnglishName() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getContinentEnglishName(), Continent_.continentEnglishName));
            }
        }
        return specification;
    }
}
