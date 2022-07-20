package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Village;
import com.mycompany.myapp.repository.VillageRepository;
import com.mycompany.myapp.service.criteria.VillageCriteria;
import com.mycompany.myapp.service.dto.VillageDTO;
import com.mycompany.myapp.service.mapper.VillageMapper;
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
 * Service for executing complex queries for {@link Village} entities in the database.
 * The main input is a {@link VillageCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link VillageDTO} or a {@link Page} of {@link VillageDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VillageQueryService extends QueryService<Village> {

    private final Logger log = LoggerFactory.getLogger(VillageQueryService.class);

    private final VillageRepository villageRepository;

    private final VillageMapper villageMapper;

    public VillageQueryService(VillageRepository villageRepository, VillageMapper villageMapper) {
        this.villageRepository = villageRepository;
        this.villageMapper = villageMapper;
    }

    /**
     * Return a {@link List} of {@link VillageDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<VillageDTO> findByCriteria(VillageCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Village> specification = createSpecification(criteria);
        return villageMapper.toDto(villageRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link VillageDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<VillageDTO> findByCriteria(VillageCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Village> specification = createSpecification(criteria);
        return villageRepository.findAll(specification, page).map(villageMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(VillageCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Village> specification = createSpecification(criteria);
        return villageRepository.count(specification);
    }

    /**
     * Function to convert {@link VillageCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Village> createSpecification(VillageCriteria criteria) {
        Specification<Village> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Village_.id));
            }
            if (criteria.getVillageId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVillageId(), Village_.villageId));
            }
            if (criteria.getVillageCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVillageCode(), Village_.villageCode));
            }
            if (criteria.getVillageName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVillageName(), Village_.villageName));
            }
            if (criteria.getVillageEnglishName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVillageEnglishName(), Village_.villageEnglishName));
            }
            if (criteria.getRuralDistrictId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRuralDistrictId(),
                            root -> root.join(Village_.ruralDistrict, JoinType.LEFT).get(RuralDistrict_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
