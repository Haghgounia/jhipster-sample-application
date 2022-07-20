package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Country;
import com.mycompany.myapp.repository.CountryRepository;
import com.mycompany.myapp.service.criteria.CountryCriteria;
import com.mycompany.myapp.service.dto.CountryDTO;
import com.mycompany.myapp.service.mapper.CountryMapper;
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
 * Service for executing complex queries for {@link Country} entities in the database.
 * The main input is a {@link CountryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CountryDTO} or a {@link Page} of {@link CountryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CountryQueryService extends QueryService<Country> {

    private final Logger log = LoggerFactory.getLogger(CountryQueryService.class);

    private final CountryRepository countryRepository;

    private final CountryMapper countryMapper;

    public CountryQueryService(CountryRepository countryRepository, CountryMapper countryMapper) {
        this.countryRepository = countryRepository;
        this.countryMapper = countryMapper;
    }

    /**
     * Return a {@link List} of {@link CountryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CountryDTO> findByCriteria(CountryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Country> specification = createSpecification(criteria);
        return countryMapper.toDto(countryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CountryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CountryDTO> findByCriteria(CountryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Country> specification = createSpecification(criteria);
        return countryRepository.findAll(specification, page).map(countryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CountryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Country> specification = createSpecification(criteria);
        return countryRepository.count(specification);
    }

    /**
     * Function to convert {@link CountryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Country> createSpecification(CountryCriteria criteria) {
        Specification<Country> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Country_.id));
            }
            if (criteria.getCountryId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCountryId(), Country_.countryId));
            }
            if (criteria.getCountryName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountryName(), Country_.countryName));
            }
            if (criteria.getCountryEnglishName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountryEnglishName(), Country_.countryEnglishName));
            }
            if (criteria.getCountryFullName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountryFullName(), Country_.countryFullName));
            }
            if (criteria.getCountryEnglishFullName() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getCountryEnglishFullName(), Country_.countryEnglishFullName));
            }
            if (criteria.getCountryIsoCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountryIsoCode(), Country_.countryIsoCode));
            }
            if (criteria.getNationality() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNationality(), Country_.nationality));
            }
            if (criteria.getEnglishNationality() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEnglishNationality(), Country_.englishNationality));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIsActive(), Country_.isActive));
            }
            if (criteria.getSortOrder() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSortOrder(), Country_.sortOrder));
            }
            if (criteria.getContinentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getContinentId(),
                            root -> root.join(Country_.continent, JoinType.LEFT).get(Continent_.id)
                        )
                    );
            }
            if (criteria.getCurrencyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCurrencyId(), root -> root.join(Country_.currency, JoinType.LEFT).get(Currency_.id))
                    );
            }
            if (criteria.getOfficialLanguageId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOfficialLanguageId(),
                            root -> root.join(Country_.officialLanguage, JoinType.LEFT).get(Language_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
