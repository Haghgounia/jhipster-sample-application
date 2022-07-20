package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Candidate;
import com.mycompany.myapp.repository.CandidateRepository;
import com.mycompany.myapp.service.criteria.CandidateCriteria;
import com.mycompany.myapp.service.dto.CandidateDTO;
import com.mycompany.myapp.service.mapper.CandidateMapper;
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
 * Service for executing complex queries for {@link Candidate} entities in the database.
 * The main input is a {@link CandidateCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CandidateDTO} or a {@link Page} of {@link CandidateDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CandidateQueryService extends QueryService<Candidate> {

    private final Logger log = LoggerFactory.getLogger(CandidateQueryService.class);

    private final CandidateRepository candidateRepository;

    private final CandidateMapper candidateMapper;

    public CandidateQueryService(CandidateRepository candidateRepository, CandidateMapper candidateMapper) {
        this.candidateRepository = candidateRepository;
        this.candidateMapper = candidateMapper;
    }

    /**
     * Return a {@link List} of {@link CandidateDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CandidateDTO> findByCriteria(CandidateCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Candidate> specification = createSpecification(criteria);
        return candidateMapper.toDto(candidateRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CandidateDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CandidateDTO> findByCriteria(CandidateCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Candidate> specification = createSpecification(criteria);
        return candidateRepository.findAll(specification, page).map(candidateMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CandidateCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Candidate> specification = createSpecification(criteria);
        return candidateRepository.count(specification);
    }

    /**
     * Function to convert {@link CandidateCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Candidate> createSpecification(CandidateCriteria criteria) {
        Specification<Candidate> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Candidate_.id));
            }
            if (criteria.getJobOpening() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getJobOpening(), Candidate_.jobOpening));
            }
            if (criteria.getCandidateStatus() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCandidateStatus(), Candidate_.candidateStatus));
            }
            if (criteria.getCandidateRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCandidateRating(), Candidate_.candidateRating));
            }
        }
        return specification;
    }
}
