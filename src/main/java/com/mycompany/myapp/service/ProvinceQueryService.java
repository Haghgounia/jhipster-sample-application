package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Province;
import com.mycompany.myapp.repository.ProvinceRepository;
import com.mycompany.myapp.service.criteria.ProvinceCriteria;
import com.mycompany.myapp.service.dto.ProvinceDTO;
import com.mycompany.myapp.service.mapper.ProvinceMapper;
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
 * Service for executing complex queries for {@link Province} entities in the database.
 * The main input is a {@link ProvinceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProvinceDTO} or a {@link Page} of {@link ProvinceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProvinceQueryService extends QueryService<Province> {

    private final Logger log = LoggerFactory.getLogger(ProvinceQueryService.class);

    private final ProvinceRepository provinceRepository;

    private final ProvinceMapper provinceMapper;

    public ProvinceQueryService(ProvinceRepository provinceRepository, ProvinceMapper provinceMapper) {
        this.provinceRepository = provinceRepository;
        this.provinceMapper = provinceMapper;
    }

    /**
     * Return a {@link List} of {@link ProvinceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProvinceDTO> findByCriteria(ProvinceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Province> specification = createSpecification(criteria);
        return provinceMapper.toDto(provinceRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProvinceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProvinceDTO> findByCriteria(ProvinceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Province> specification = createSpecification(criteria);
        return provinceRepository.findAll(specification, page).map(provinceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProvinceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Province> specification = createSpecification(criteria);
        return provinceRepository.count(specification);
    }

    /**
     * Function to convert {@link ProvinceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Province> createSpecification(ProvinceCriteria criteria) {
        Specification<Province> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Province_.id));
            }
            if (criteria.getProvinceId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getProvinceId(), Province_.provinceId));
            }
            if (criteria.getProvinceCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProvinceCode(), Province_.provinceCode));
            }
            if (criteria.getProvinceName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProvinceName(), Province_.provinceName));
            }
            if (criteria.getProvinceEnglishName() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getProvinceEnglishName(), Province_.provinceEnglishName));
            }
            if (criteria.getDiallingCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDiallingCode(), Province_.diallingCode));
            }
            if (criteria.getCountryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCountryId(), root -> root.join(Province_.country, JoinType.LEFT).get(Country_.id))
                    );
            }
        }
        return specification;
    }
}
