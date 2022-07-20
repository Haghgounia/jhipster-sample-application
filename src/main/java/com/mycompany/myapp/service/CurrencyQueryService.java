package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Currency;
import com.mycompany.myapp.repository.CurrencyRepository;
import com.mycompany.myapp.service.criteria.CurrencyCriteria;
import com.mycompany.myapp.service.dto.CurrencyDTO;
import com.mycompany.myapp.service.mapper.CurrencyMapper;
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
 * Service for executing complex queries for {@link Currency} entities in the database.
 * The main input is a {@link CurrencyCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CurrencyDTO} or a {@link Page} of {@link CurrencyDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CurrencyQueryService extends QueryService<Currency> {

    private final Logger log = LoggerFactory.getLogger(CurrencyQueryService.class);

    private final CurrencyRepository currencyRepository;

    private final CurrencyMapper currencyMapper;

    public CurrencyQueryService(CurrencyRepository currencyRepository, CurrencyMapper currencyMapper) {
        this.currencyRepository = currencyRepository;
        this.currencyMapper = currencyMapper;
    }

    /**
     * Return a {@link List} of {@link CurrencyDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CurrencyDTO> findByCriteria(CurrencyCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Currency> specification = createSpecification(criteria);
        return currencyMapper.toDto(currencyRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CurrencyDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CurrencyDTO> findByCriteria(CurrencyCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Currency> specification = createSpecification(criteria);
        return currencyRepository.findAll(specification, page).map(currencyMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CurrencyCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Currency> specification = createSpecification(criteria);
        return currencyRepository.count(specification);
    }

    /**
     * Function to convert {@link CurrencyCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Currency> createSpecification(CurrencyCriteria criteria) {
        Specification<Currency> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Currency_.id));
            }
            if (criteria.getCurrencyId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCurrencyId(), Currency_.currencyId));
            }
            if (criteria.getCurrencyAlphabeticIso() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getCurrencyAlphabeticIso(), Currency_.currencyAlphabeticIso));
            }
            if (criteria.getCurrencyNumericIso() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCurrencyNumericIso(), Currency_.currencyNumericIso));
            }
            if (criteria.getCurrencyName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCurrencyName(), Currency_.currencyName));
            }
            if (criteria.getCurrencyEnglishName() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getCurrencyEnglishName(), Currency_.currencyEnglishName));
            }
            if (criteria.getCurrencySymbol() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCurrencySymbol(), Currency_.currencySymbol));
            }
            if (criteria.getFloatingPoint() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFloatingPoint(), Currency_.floatingPoint));
            }
            if (criteria.getIsBaseCurrency() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIsBaseCurrency(), Currency_.isBaseCurrency));
            }
            if (criteria.getIsDefaultCurrency() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIsDefaultCurrency(), Currency_.isDefaultCurrency));
            }
            if (criteria.getConversionMethod() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getConversionMethod(), Currency_.conversionMethod));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIsActive(), Currency_.isActive));
            }
            if (criteria.getSortOrder() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSortOrder(), Currency_.sortOrder));
            }
        }
        return specification;
    }
}
