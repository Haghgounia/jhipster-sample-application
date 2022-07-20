package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.JobOpening;
import com.mycompany.myapp.repository.JobOpeningRepository;
import com.mycompany.myapp.service.criteria.JobOpeningCriteria;
import com.mycompany.myapp.service.dto.JobOpeningDTO;
import com.mycompany.myapp.service.mapper.JobOpeningMapper;
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
 * Service for executing complex queries for {@link JobOpening} entities in the database.
 * The main input is a {@link JobOpeningCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link JobOpeningDTO} or a {@link Page} of {@link JobOpeningDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class JobOpeningQueryService extends QueryService<JobOpening> {

    private final Logger log = LoggerFactory.getLogger(JobOpeningQueryService.class);

    private final JobOpeningRepository jobOpeningRepository;

    private final JobOpeningMapper jobOpeningMapper;

    public JobOpeningQueryService(JobOpeningRepository jobOpeningRepository, JobOpeningMapper jobOpeningMapper) {
        this.jobOpeningRepository = jobOpeningRepository;
        this.jobOpeningMapper = jobOpeningMapper;
    }

    /**
     * Return a {@link List} of {@link JobOpeningDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<JobOpeningDTO> findByCriteria(JobOpeningCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<JobOpening> specification = createSpecification(criteria);
        return jobOpeningMapper.toDto(jobOpeningRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link JobOpeningDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<JobOpeningDTO> findByCriteria(JobOpeningCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<JobOpening> specification = createSpecification(criteria);
        return jobOpeningRepository.findAll(specification, page).map(jobOpeningMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(JobOpeningCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<JobOpening> specification = createSpecification(criteria);
        return jobOpeningRepository.count(specification);
    }

    /**
     * Function to convert {@link JobOpeningCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<JobOpening> createSpecification(JobOpeningCriteria criteria) {
        Specification<JobOpening> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), JobOpening_.id));
            }
            if (criteria.getPostingTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPostingTitle(), JobOpening_.postingTitle));
            }
            if (criteria.getJobStatus() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getJobStatus(), JobOpening_.jobStatus));
            }
            if (criteria.getHiringLead() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHiringLead(), JobOpening_.hiringLead));
            }
            if (criteria.getDepartmentId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDepartmentId(), JobOpening_.departmentId));
            }
            if (criteria.getEmploymentType() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEmploymentType(), JobOpening_.employmentType));
            }
            if (criteria.getMinimumExperience() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMinimumExperience(), JobOpening_.minimumExperience));
            }
            if (criteria.getJobDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getJobDescription(), JobOpening_.jobDescription));
            }
        }
        return specification;
    }
}
