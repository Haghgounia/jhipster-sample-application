package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.EmployeeContact;
import com.mycompany.myapp.repository.EmployeeContactRepository;
import com.mycompany.myapp.service.criteria.EmployeeContactCriteria;
import com.mycompany.myapp.service.dto.EmployeeContactDTO;
import com.mycompany.myapp.service.mapper.EmployeeContactMapper;
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
 * Service for executing complex queries for {@link EmployeeContact} entities in the database.
 * The main input is a {@link EmployeeContactCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EmployeeContactDTO} or a {@link Page} of {@link EmployeeContactDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EmployeeContactQueryService extends QueryService<EmployeeContact> {

    private final Logger log = LoggerFactory.getLogger(EmployeeContactQueryService.class);

    private final EmployeeContactRepository employeeContactRepository;

    private final EmployeeContactMapper employeeContactMapper;

    public EmployeeContactQueryService(EmployeeContactRepository employeeContactRepository, EmployeeContactMapper employeeContactMapper) {
        this.employeeContactRepository = employeeContactRepository;
        this.employeeContactMapper = employeeContactMapper;
    }

    /**
     * Return a {@link List} of {@link EmployeeContactDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EmployeeContactDTO> findByCriteria(EmployeeContactCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EmployeeContact> specification = createSpecification(criteria);
        return employeeContactMapper.toDto(employeeContactRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EmployeeContactDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EmployeeContactDTO> findByCriteria(EmployeeContactCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EmployeeContact> specification = createSpecification(criteria);
        return employeeContactRepository.findAll(specification, page).map(employeeContactMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EmployeeContactCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EmployeeContact> specification = createSpecification(criteria);
        return employeeContactRepository.count(specification);
    }

    /**
     * Function to convert {@link EmployeeContactCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EmployeeContact> createSpecification(EmployeeContactCriteria criteria) {
        Specification<EmployeeContact> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EmployeeContact_.id));
            }
            if (criteria.getEmployeeContactId() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getEmployeeContactId(), EmployeeContact_.employeeContactId));
            }
            if (criteria.getEmployeeId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEmployeeId(), EmployeeContact_.employeeId));
            }
            if (criteria.getContactType() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getContactType(), EmployeeContact_.contactType));
            }
            if (criteria.getContact() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContact(), EmployeeContact_.contact));
            }
        }
        return specification;
    }
}
