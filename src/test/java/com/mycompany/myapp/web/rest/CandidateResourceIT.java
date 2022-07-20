package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Candidate;
import com.mycompany.myapp.repository.CandidateRepository;
import com.mycompany.myapp.service.criteria.CandidateCriteria;
import com.mycompany.myapp.service.dto.CandidateDTO;
import com.mycompany.myapp.service.mapper.CandidateMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CandidateResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CandidateResourceIT {

    private static final Integer DEFAULT_JOB_OPENING = 1;
    private static final Integer UPDATED_JOB_OPENING = 2;
    private static final Integer SMALLER_JOB_OPENING = 1 - 1;

    private static final Integer DEFAULT_CANDIDATE_STATUS = 1;
    private static final Integer UPDATED_CANDIDATE_STATUS = 2;
    private static final Integer SMALLER_CANDIDATE_STATUS = 1 - 1;

    private static final Integer DEFAULT_CANDIDATE_RATING = 1;
    private static final Integer UPDATED_CANDIDATE_RATING = 2;
    private static final Integer SMALLER_CANDIDATE_RATING = 1 - 1;

    private static final String ENTITY_API_URL = "/api/candidates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private CandidateMapper candidateMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCandidateMockMvc;

    private Candidate candidate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Candidate createEntity(EntityManager em) {
        Candidate candidate = new Candidate()
            .jobOpening(DEFAULT_JOB_OPENING)
            .candidateStatus(DEFAULT_CANDIDATE_STATUS)
            .candidateRating(DEFAULT_CANDIDATE_RATING);
        return candidate;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Candidate createUpdatedEntity(EntityManager em) {
        Candidate candidate = new Candidate()
            .jobOpening(UPDATED_JOB_OPENING)
            .candidateStatus(UPDATED_CANDIDATE_STATUS)
            .candidateRating(UPDATED_CANDIDATE_RATING);
        return candidate;
    }

    @BeforeEach
    public void initTest() {
        candidate = createEntity(em);
    }

    @Test
    @Transactional
    void createCandidate() throws Exception {
        int databaseSizeBeforeCreate = candidateRepository.findAll().size();
        // Create the Candidate
        CandidateDTO candidateDTO = candidateMapper.toDto(candidate);
        restCandidateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(candidateDTO)))
            .andExpect(status().isCreated());

        // Validate the Candidate in the database
        List<Candidate> candidateList = candidateRepository.findAll();
        assertThat(candidateList).hasSize(databaseSizeBeforeCreate + 1);
        Candidate testCandidate = candidateList.get(candidateList.size() - 1);
        assertThat(testCandidate.getJobOpening()).isEqualTo(DEFAULT_JOB_OPENING);
        assertThat(testCandidate.getCandidateStatus()).isEqualTo(DEFAULT_CANDIDATE_STATUS);
        assertThat(testCandidate.getCandidateRating()).isEqualTo(DEFAULT_CANDIDATE_RATING);
    }

    @Test
    @Transactional
    void createCandidateWithExistingId() throws Exception {
        // Create the Candidate with an existing ID
        candidate.setId(1L);
        CandidateDTO candidateDTO = candidateMapper.toDto(candidate);

        int databaseSizeBeforeCreate = candidateRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCandidateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(candidateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Candidate in the database
        List<Candidate> candidateList = candidateRepository.findAll();
        assertThat(candidateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCandidates() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        // Get all the candidateList
        restCandidateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(candidate.getId().intValue())))
            .andExpect(jsonPath("$.[*].jobOpening").value(hasItem(DEFAULT_JOB_OPENING)))
            .andExpect(jsonPath("$.[*].candidateStatus").value(hasItem(DEFAULT_CANDIDATE_STATUS)))
            .andExpect(jsonPath("$.[*].candidateRating").value(hasItem(DEFAULT_CANDIDATE_RATING)));
    }

    @Test
    @Transactional
    void getCandidate() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        // Get the candidate
        restCandidateMockMvc
            .perform(get(ENTITY_API_URL_ID, candidate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(candidate.getId().intValue()))
            .andExpect(jsonPath("$.jobOpening").value(DEFAULT_JOB_OPENING))
            .andExpect(jsonPath("$.candidateStatus").value(DEFAULT_CANDIDATE_STATUS))
            .andExpect(jsonPath("$.candidateRating").value(DEFAULT_CANDIDATE_RATING));
    }

    @Test
    @Transactional
    void getCandidatesByIdFiltering() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        Long id = candidate.getId();

        defaultCandidateShouldBeFound("id.equals=" + id);
        defaultCandidateShouldNotBeFound("id.notEquals=" + id);

        defaultCandidateShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCandidateShouldNotBeFound("id.greaterThan=" + id);

        defaultCandidateShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCandidateShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCandidatesByJobOpeningIsEqualToSomething() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        // Get all the candidateList where jobOpening equals to DEFAULT_JOB_OPENING
        defaultCandidateShouldBeFound("jobOpening.equals=" + DEFAULT_JOB_OPENING);

        // Get all the candidateList where jobOpening equals to UPDATED_JOB_OPENING
        defaultCandidateShouldNotBeFound("jobOpening.equals=" + UPDATED_JOB_OPENING);
    }

    @Test
    @Transactional
    void getAllCandidatesByJobOpeningIsNotEqualToSomething() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        // Get all the candidateList where jobOpening not equals to DEFAULT_JOB_OPENING
        defaultCandidateShouldNotBeFound("jobOpening.notEquals=" + DEFAULT_JOB_OPENING);

        // Get all the candidateList where jobOpening not equals to UPDATED_JOB_OPENING
        defaultCandidateShouldBeFound("jobOpening.notEquals=" + UPDATED_JOB_OPENING);
    }

    @Test
    @Transactional
    void getAllCandidatesByJobOpeningIsInShouldWork() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        // Get all the candidateList where jobOpening in DEFAULT_JOB_OPENING or UPDATED_JOB_OPENING
        defaultCandidateShouldBeFound("jobOpening.in=" + DEFAULT_JOB_OPENING + "," + UPDATED_JOB_OPENING);

        // Get all the candidateList where jobOpening equals to UPDATED_JOB_OPENING
        defaultCandidateShouldNotBeFound("jobOpening.in=" + UPDATED_JOB_OPENING);
    }

    @Test
    @Transactional
    void getAllCandidatesByJobOpeningIsNullOrNotNull() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        // Get all the candidateList where jobOpening is not null
        defaultCandidateShouldBeFound("jobOpening.specified=true");

        // Get all the candidateList where jobOpening is null
        defaultCandidateShouldNotBeFound("jobOpening.specified=false");
    }

    @Test
    @Transactional
    void getAllCandidatesByJobOpeningIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        // Get all the candidateList where jobOpening is greater than or equal to DEFAULT_JOB_OPENING
        defaultCandidateShouldBeFound("jobOpening.greaterThanOrEqual=" + DEFAULT_JOB_OPENING);

        // Get all the candidateList where jobOpening is greater than or equal to UPDATED_JOB_OPENING
        defaultCandidateShouldNotBeFound("jobOpening.greaterThanOrEqual=" + UPDATED_JOB_OPENING);
    }

    @Test
    @Transactional
    void getAllCandidatesByJobOpeningIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        // Get all the candidateList where jobOpening is less than or equal to DEFAULT_JOB_OPENING
        defaultCandidateShouldBeFound("jobOpening.lessThanOrEqual=" + DEFAULT_JOB_OPENING);

        // Get all the candidateList where jobOpening is less than or equal to SMALLER_JOB_OPENING
        defaultCandidateShouldNotBeFound("jobOpening.lessThanOrEqual=" + SMALLER_JOB_OPENING);
    }

    @Test
    @Transactional
    void getAllCandidatesByJobOpeningIsLessThanSomething() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        // Get all the candidateList where jobOpening is less than DEFAULT_JOB_OPENING
        defaultCandidateShouldNotBeFound("jobOpening.lessThan=" + DEFAULT_JOB_OPENING);

        // Get all the candidateList where jobOpening is less than UPDATED_JOB_OPENING
        defaultCandidateShouldBeFound("jobOpening.lessThan=" + UPDATED_JOB_OPENING);
    }

    @Test
    @Transactional
    void getAllCandidatesByJobOpeningIsGreaterThanSomething() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        // Get all the candidateList where jobOpening is greater than DEFAULT_JOB_OPENING
        defaultCandidateShouldNotBeFound("jobOpening.greaterThan=" + DEFAULT_JOB_OPENING);

        // Get all the candidateList where jobOpening is greater than SMALLER_JOB_OPENING
        defaultCandidateShouldBeFound("jobOpening.greaterThan=" + SMALLER_JOB_OPENING);
    }

    @Test
    @Transactional
    void getAllCandidatesByCandidateStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        // Get all the candidateList where candidateStatus equals to DEFAULT_CANDIDATE_STATUS
        defaultCandidateShouldBeFound("candidateStatus.equals=" + DEFAULT_CANDIDATE_STATUS);

        // Get all the candidateList where candidateStatus equals to UPDATED_CANDIDATE_STATUS
        defaultCandidateShouldNotBeFound("candidateStatus.equals=" + UPDATED_CANDIDATE_STATUS);
    }

    @Test
    @Transactional
    void getAllCandidatesByCandidateStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        // Get all the candidateList where candidateStatus not equals to DEFAULT_CANDIDATE_STATUS
        defaultCandidateShouldNotBeFound("candidateStatus.notEquals=" + DEFAULT_CANDIDATE_STATUS);

        // Get all the candidateList where candidateStatus not equals to UPDATED_CANDIDATE_STATUS
        defaultCandidateShouldBeFound("candidateStatus.notEquals=" + UPDATED_CANDIDATE_STATUS);
    }

    @Test
    @Transactional
    void getAllCandidatesByCandidateStatusIsInShouldWork() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        // Get all the candidateList where candidateStatus in DEFAULT_CANDIDATE_STATUS or UPDATED_CANDIDATE_STATUS
        defaultCandidateShouldBeFound("candidateStatus.in=" + DEFAULT_CANDIDATE_STATUS + "," + UPDATED_CANDIDATE_STATUS);

        // Get all the candidateList where candidateStatus equals to UPDATED_CANDIDATE_STATUS
        defaultCandidateShouldNotBeFound("candidateStatus.in=" + UPDATED_CANDIDATE_STATUS);
    }

    @Test
    @Transactional
    void getAllCandidatesByCandidateStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        // Get all the candidateList where candidateStatus is not null
        defaultCandidateShouldBeFound("candidateStatus.specified=true");

        // Get all the candidateList where candidateStatus is null
        defaultCandidateShouldNotBeFound("candidateStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllCandidatesByCandidateStatusIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        // Get all the candidateList where candidateStatus is greater than or equal to DEFAULT_CANDIDATE_STATUS
        defaultCandidateShouldBeFound("candidateStatus.greaterThanOrEqual=" + DEFAULT_CANDIDATE_STATUS);

        // Get all the candidateList where candidateStatus is greater than or equal to UPDATED_CANDIDATE_STATUS
        defaultCandidateShouldNotBeFound("candidateStatus.greaterThanOrEqual=" + UPDATED_CANDIDATE_STATUS);
    }

    @Test
    @Transactional
    void getAllCandidatesByCandidateStatusIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        // Get all the candidateList where candidateStatus is less than or equal to DEFAULT_CANDIDATE_STATUS
        defaultCandidateShouldBeFound("candidateStatus.lessThanOrEqual=" + DEFAULT_CANDIDATE_STATUS);

        // Get all the candidateList where candidateStatus is less than or equal to SMALLER_CANDIDATE_STATUS
        defaultCandidateShouldNotBeFound("candidateStatus.lessThanOrEqual=" + SMALLER_CANDIDATE_STATUS);
    }

    @Test
    @Transactional
    void getAllCandidatesByCandidateStatusIsLessThanSomething() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        // Get all the candidateList where candidateStatus is less than DEFAULT_CANDIDATE_STATUS
        defaultCandidateShouldNotBeFound("candidateStatus.lessThan=" + DEFAULT_CANDIDATE_STATUS);

        // Get all the candidateList where candidateStatus is less than UPDATED_CANDIDATE_STATUS
        defaultCandidateShouldBeFound("candidateStatus.lessThan=" + UPDATED_CANDIDATE_STATUS);
    }

    @Test
    @Transactional
    void getAllCandidatesByCandidateStatusIsGreaterThanSomething() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        // Get all the candidateList where candidateStatus is greater than DEFAULT_CANDIDATE_STATUS
        defaultCandidateShouldNotBeFound("candidateStatus.greaterThan=" + DEFAULT_CANDIDATE_STATUS);

        // Get all the candidateList where candidateStatus is greater than SMALLER_CANDIDATE_STATUS
        defaultCandidateShouldBeFound("candidateStatus.greaterThan=" + SMALLER_CANDIDATE_STATUS);
    }

    @Test
    @Transactional
    void getAllCandidatesByCandidateRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        // Get all the candidateList where candidateRating equals to DEFAULT_CANDIDATE_RATING
        defaultCandidateShouldBeFound("candidateRating.equals=" + DEFAULT_CANDIDATE_RATING);

        // Get all the candidateList where candidateRating equals to UPDATED_CANDIDATE_RATING
        defaultCandidateShouldNotBeFound("candidateRating.equals=" + UPDATED_CANDIDATE_RATING);
    }

    @Test
    @Transactional
    void getAllCandidatesByCandidateRatingIsNotEqualToSomething() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        // Get all the candidateList where candidateRating not equals to DEFAULT_CANDIDATE_RATING
        defaultCandidateShouldNotBeFound("candidateRating.notEquals=" + DEFAULT_CANDIDATE_RATING);

        // Get all the candidateList where candidateRating not equals to UPDATED_CANDIDATE_RATING
        defaultCandidateShouldBeFound("candidateRating.notEquals=" + UPDATED_CANDIDATE_RATING);
    }

    @Test
    @Transactional
    void getAllCandidatesByCandidateRatingIsInShouldWork() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        // Get all the candidateList where candidateRating in DEFAULT_CANDIDATE_RATING or UPDATED_CANDIDATE_RATING
        defaultCandidateShouldBeFound("candidateRating.in=" + DEFAULT_CANDIDATE_RATING + "," + UPDATED_CANDIDATE_RATING);

        // Get all the candidateList where candidateRating equals to UPDATED_CANDIDATE_RATING
        defaultCandidateShouldNotBeFound("candidateRating.in=" + UPDATED_CANDIDATE_RATING);
    }

    @Test
    @Transactional
    void getAllCandidatesByCandidateRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        // Get all the candidateList where candidateRating is not null
        defaultCandidateShouldBeFound("candidateRating.specified=true");

        // Get all the candidateList where candidateRating is null
        defaultCandidateShouldNotBeFound("candidateRating.specified=false");
    }

    @Test
    @Transactional
    void getAllCandidatesByCandidateRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        // Get all the candidateList where candidateRating is greater than or equal to DEFAULT_CANDIDATE_RATING
        defaultCandidateShouldBeFound("candidateRating.greaterThanOrEqual=" + DEFAULT_CANDIDATE_RATING);

        // Get all the candidateList where candidateRating is greater than or equal to UPDATED_CANDIDATE_RATING
        defaultCandidateShouldNotBeFound("candidateRating.greaterThanOrEqual=" + UPDATED_CANDIDATE_RATING);
    }

    @Test
    @Transactional
    void getAllCandidatesByCandidateRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        // Get all the candidateList where candidateRating is less than or equal to DEFAULT_CANDIDATE_RATING
        defaultCandidateShouldBeFound("candidateRating.lessThanOrEqual=" + DEFAULT_CANDIDATE_RATING);

        // Get all the candidateList where candidateRating is less than or equal to SMALLER_CANDIDATE_RATING
        defaultCandidateShouldNotBeFound("candidateRating.lessThanOrEqual=" + SMALLER_CANDIDATE_RATING);
    }

    @Test
    @Transactional
    void getAllCandidatesByCandidateRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        // Get all the candidateList where candidateRating is less than DEFAULT_CANDIDATE_RATING
        defaultCandidateShouldNotBeFound("candidateRating.lessThan=" + DEFAULT_CANDIDATE_RATING);

        // Get all the candidateList where candidateRating is less than UPDATED_CANDIDATE_RATING
        defaultCandidateShouldBeFound("candidateRating.lessThan=" + UPDATED_CANDIDATE_RATING);
    }

    @Test
    @Transactional
    void getAllCandidatesByCandidateRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        // Get all the candidateList where candidateRating is greater than DEFAULT_CANDIDATE_RATING
        defaultCandidateShouldNotBeFound("candidateRating.greaterThan=" + DEFAULT_CANDIDATE_RATING);

        // Get all the candidateList where candidateRating is greater than SMALLER_CANDIDATE_RATING
        defaultCandidateShouldBeFound("candidateRating.greaterThan=" + SMALLER_CANDIDATE_RATING);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCandidateShouldBeFound(String filter) throws Exception {
        restCandidateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(candidate.getId().intValue())))
            .andExpect(jsonPath("$.[*].jobOpening").value(hasItem(DEFAULT_JOB_OPENING)))
            .andExpect(jsonPath("$.[*].candidateStatus").value(hasItem(DEFAULT_CANDIDATE_STATUS)))
            .andExpect(jsonPath("$.[*].candidateRating").value(hasItem(DEFAULT_CANDIDATE_RATING)));

        // Check, that the count call also returns 1
        restCandidateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCandidateShouldNotBeFound(String filter) throws Exception {
        restCandidateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCandidateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCandidate() throws Exception {
        // Get the candidate
        restCandidateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCandidate() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        int databaseSizeBeforeUpdate = candidateRepository.findAll().size();

        // Update the candidate
        Candidate updatedCandidate = candidateRepository.findById(candidate.getId()).get();
        // Disconnect from session so that the updates on updatedCandidate are not directly saved in db
        em.detach(updatedCandidate);
        updatedCandidate
            .jobOpening(UPDATED_JOB_OPENING)
            .candidateStatus(UPDATED_CANDIDATE_STATUS)
            .candidateRating(UPDATED_CANDIDATE_RATING);
        CandidateDTO candidateDTO = candidateMapper.toDto(updatedCandidate);

        restCandidateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, candidateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(candidateDTO))
            )
            .andExpect(status().isOk());

        // Validate the Candidate in the database
        List<Candidate> candidateList = candidateRepository.findAll();
        assertThat(candidateList).hasSize(databaseSizeBeforeUpdate);
        Candidate testCandidate = candidateList.get(candidateList.size() - 1);
        assertThat(testCandidate.getJobOpening()).isEqualTo(UPDATED_JOB_OPENING);
        assertThat(testCandidate.getCandidateStatus()).isEqualTo(UPDATED_CANDIDATE_STATUS);
        assertThat(testCandidate.getCandidateRating()).isEqualTo(UPDATED_CANDIDATE_RATING);
    }

    @Test
    @Transactional
    void putNonExistingCandidate() throws Exception {
        int databaseSizeBeforeUpdate = candidateRepository.findAll().size();
        candidate.setId(count.incrementAndGet());

        // Create the Candidate
        CandidateDTO candidateDTO = candidateMapper.toDto(candidate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCandidateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, candidateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(candidateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Candidate in the database
        List<Candidate> candidateList = candidateRepository.findAll();
        assertThat(candidateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCandidate() throws Exception {
        int databaseSizeBeforeUpdate = candidateRepository.findAll().size();
        candidate.setId(count.incrementAndGet());

        // Create the Candidate
        CandidateDTO candidateDTO = candidateMapper.toDto(candidate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCandidateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(candidateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Candidate in the database
        List<Candidate> candidateList = candidateRepository.findAll();
        assertThat(candidateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCandidate() throws Exception {
        int databaseSizeBeforeUpdate = candidateRepository.findAll().size();
        candidate.setId(count.incrementAndGet());

        // Create the Candidate
        CandidateDTO candidateDTO = candidateMapper.toDto(candidate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCandidateMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(candidateDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Candidate in the database
        List<Candidate> candidateList = candidateRepository.findAll();
        assertThat(candidateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCandidateWithPatch() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        int databaseSizeBeforeUpdate = candidateRepository.findAll().size();

        // Update the candidate using partial update
        Candidate partialUpdatedCandidate = new Candidate();
        partialUpdatedCandidate.setId(candidate.getId());

        partialUpdatedCandidate.jobOpening(UPDATED_JOB_OPENING);

        restCandidateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCandidate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCandidate))
            )
            .andExpect(status().isOk());

        // Validate the Candidate in the database
        List<Candidate> candidateList = candidateRepository.findAll();
        assertThat(candidateList).hasSize(databaseSizeBeforeUpdate);
        Candidate testCandidate = candidateList.get(candidateList.size() - 1);
        assertThat(testCandidate.getJobOpening()).isEqualTo(UPDATED_JOB_OPENING);
        assertThat(testCandidate.getCandidateStatus()).isEqualTo(DEFAULT_CANDIDATE_STATUS);
        assertThat(testCandidate.getCandidateRating()).isEqualTo(DEFAULT_CANDIDATE_RATING);
    }

    @Test
    @Transactional
    void fullUpdateCandidateWithPatch() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        int databaseSizeBeforeUpdate = candidateRepository.findAll().size();

        // Update the candidate using partial update
        Candidate partialUpdatedCandidate = new Candidate();
        partialUpdatedCandidate.setId(candidate.getId());

        partialUpdatedCandidate
            .jobOpening(UPDATED_JOB_OPENING)
            .candidateStatus(UPDATED_CANDIDATE_STATUS)
            .candidateRating(UPDATED_CANDIDATE_RATING);

        restCandidateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCandidate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCandidate))
            )
            .andExpect(status().isOk());

        // Validate the Candidate in the database
        List<Candidate> candidateList = candidateRepository.findAll();
        assertThat(candidateList).hasSize(databaseSizeBeforeUpdate);
        Candidate testCandidate = candidateList.get(candidateList.size() - 1);
        assertThat(testCandidate.getJobOpening()).isEqualTo(UPDATED_JOB_OPENING);
        assertThat(testCandidate.getCandidateStatus()).isEqualTo(UPDATED_CANDIDATE_STATUS);
        assertThat(testCandidate.getCandidateRating()).isEqualTo(UPDATED_CANDIDATE_RATING);
    }

    @Test
    @Transactional
    void patchNonExistingCandidate() throws Exception {
        int databaseSizeBeforeUpdate = candidateRepository.findAll().size();
        candidate.setId(count.incrementAndGet());

        // Create the Candidate
        CandidateDTO candidateDTO = candidateMapper.toDto(candidate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCandidateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, candidateDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(candidateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Candidate in the database
        List<Candidate> candidateList = candidateRepository.findAll();
        assertThat(candidateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCandidate() throws Exception {
        int databaseSizeBeforeUpdate = candidateRepository.findAll().size();
        candidate.setId(count.incrementAndGet());

        // Create the Candidate
        CandidateDTO candidateDTO = candidateMapper.toDto(candidate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCandidateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(candidateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Candidate in the database
        List<Candidate> candidateList = candidateRepository.findAll();
        assertThat(candidateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCandidate() throws Exception {
        int databaseSizeBeforeUpdate = candidateRepository.findAll().size();
        candidate.setId(count.incrementAndGet());

        // Create the Candidate
        CandidateDTO candidateDTO = candidateMapper.toDto(candidate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCandidateMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(candidateDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Candidate in the database
        List<Candidate> candidateList = candidateRepository.findAll();
        assertThat(candidateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCandidate() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        int databaseSizeBeforeDelete = candidateRepository.findAll().size();

        // Delete the candidate
        restCandidateMockMvc
            .perform(delete(ENTITY_API_URL_ID, candidate.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Candidate> candidateList = candidateRepository.findAll();
        assertThat(candidateList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
