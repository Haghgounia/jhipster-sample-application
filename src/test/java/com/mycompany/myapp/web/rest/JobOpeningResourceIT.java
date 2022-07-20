package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.JobOpening;
import com.mycompany.myapp.repository.JobOpeningRepository;
import com.mycompany.myapp.service.criteria.JobOpeningCriteria;
import com.mycompany.myapp.service.dto.JobOpeningDTO;
import com.mycompany.myapp.service.mapper.JobOpeningMapper;
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
 * Integration tests for the {@link JobOpeningResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class JobOpeningResourceIT {

    private static final String DEFAULT_POSTING_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_POSTING_TITLE = "BBBBBBBBBB";

    private static final Integer DEFAULT_JOB_STATUS = 1;
    private static final Integer UPDATED_JOB_STATUS = 2;
    private static final Integer SMALLER_JOB_STATUS = 1 - 1;

    private static final Integer DEFAULT_HIRING_LEAD = 1;
    private static final Integer UPDATED_HIRING_LEAD = 2;
    private static final Integer SMALLER_HIRING_LEAD = 1 - 1;

    private static final Integer DEFAULT_DEPARTMENT_ID = 1;
    private static final Integer UPDATED_DEPARTMENT_ID = 2;
    private static final Integer SMALLER_DEPARTMENT_ID = 1 - 1;

    private static final Integer DEFAULT_EMPLOYMENT_TYPE = 1;
    private static final Integer UPDATED_EMPLOYMENT_TYPE = 2;
    private static final Integer SMALLER_EMPLOYMENT_TYPE = 1 - 1;

    private static final Integer DEFAULT_MINIMUM_EXPERIENCE = 1;
    private static final Integer UPDATED_MINIMUM_EXPERIENCE = 2;
    private static final Integer SMALLER_MINIMUM_EXPERIENCE = 1 - 1;

    private static final String DEFAULT_JOB_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_JOB_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/job-openings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private JobOpeningRepository jobOpeningRepository;

    @Autowired
    private JobOpeningMapper jobOpeningMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restJobOpeningMockMvc;

    private JobOpening jobOpening;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobOpening createEntity(EntityManager em) {
        JobOpening jobOpening = new JobOpening()
            .postingTitle(DEFAULT_POSTING_TITLE)
            .jobStatus(DEFAULT_JOB_STATUS)
            .hiringLead(DEFAULT_HIRING_LEAD)
            .departmentId(DEFAULT_DEPARTMENT_ID)
            .employmentType(DEFAULT_EMPLOYMENT_TYPE)
            .minimumExperience(DEFAULT_MINIMUM_EXPERIENCE)
            .jobDescription(DEFAULT_JOB_DESCRIPTION);
        return jobOpening;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobOpening createUpdatedEntity(EntityManager em) {
        JobOpening jobOpening = new JobOpening()
            .postingTitle(UPDATED_POSTING_TITLE)
            .jobStatus(UPDATED_JOB_STATUS)
            .hiringLead(UPDATED_HIRING_LEAD)
            .departmentId(UPDATED_DEPARTMENT_ID)
            .employmentType(UPDATED_EMPLOYMENT_TYPE)
            .minimumExperience(UPDATED_MINIMUM_EXPERIENCE)
            .jobDescription(UPDATED_JOB_DESCRIPTION);
        return jobOpening;
    }

    @BeforeEach
    public void initTest() {
        jobOpening = createEntity(em);
    }

    @Test
    @Transactional
    void createJobOpening() throws Exception {
        int databaseSizeBeforeCreate = jobOpeningRepository.findAll().size();
        // Create the JobOpening
        JobOpeningDTO jobOpeningDTO = jobOpeningMapper.toDto(jobOpening);
        restJobOpeningMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobOpeningDTO)))
            .andExpect(status().isCreated());

        // Validate the JobOpening in the database
        List<JobOpening> jobOpeningList = jobOpeningRepository.findAll();
        assertThat(jobOpeningList).hasSize(databaseSizeBeforeCreate + 1);
        JobOpening testJobOpening = jobOpeningList.get(jobOpeningList.size() - 1);
        assertThat(testJobOpening.getPostingTitle()).isEqualTo(DEFAULT_POSTING_TITLE);
        assertThat(testJobOpening.getJobStatus()).isEqualTo(DEFAULT_JOB_STATUS);
        assertThat(testJobOpening.getHiringLead()).isEqualTo(DEFAULT_HIRING_LEAD);
        assertThat(testJobOpening.getDepartmentId()).isEqualTo(DEFAULT_DEPARTMENT_ID);
        assertThat(testJobOpening.getEmploymentType()).isEqualTo(DEFAULT_EMPLOYMENT_TYPE);
        assertThat(testJobOpening.getMinimumExperience()).isEqualTo(DEFAULT_MINIMUM_EXPERIENCE);
        assertThat(testJobOpening.getJobDescription()).isEqualTo(DEFAULT_JOB_DESCRIPTION);
    }

    @Test
    @Transactional
    void createJobOpeningWithExistingId() throws Exception {
        // Create the JobOpening with an existing ID
        jobOpening.setId(1L);
        JobOpeningDTO jobOpeningDTO = jobOpeningMapper.toDto(jobOpening);

        int databaseSizeBeforeCreate = jobOpeningRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobOpeningMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobOpeningDTO)))
            .andExpect(status().isBadRequest());

        // Validate the JobOpening in the database
        List<JobOpening> jobOpeningList = jobOpeningRepository.findAll();
        assertThat(jobOpeningList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllJobOpenings() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList
        restJobOpeningMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobOpening.getId().intValue())))
            .andExpect(jsonPath("$.[*].postingTitle").value(hasItem(DEFAULT_POSTING_TITLE)))
            .andExpect(jsonPath("$.[*].jobStatus").value(hasItem(DEFAULT_JOB_STATUS)))
            .andExpect(jsonPath("$.[*].hiringLead").value(hasItem(DEFAULT_HIRING_LEAD)))
            .andExpect(jsonPath("$.[*].departmentId").value(hasItem(DEFAULT_DEPARTMENT_ID)))
            .andExpect(jsonPath("$.[*].employmentType").value(hasItem(DEFAULT_EMPLOYMENT_TYPE)))
            .andExpect(jsonPath("$.[*].minimumExperience").value(hasItem(DEFAULT_MINIMUM_EXPERIENCE)))
            .andExpect(jsonPath("$.[*].jobDescription").value(hasItem(DEFAULT_JOB_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getJobOpening() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get the jobOpening
        restJobOpeningMockMvc
            .perform(get(ENTITY_API_URL_ID, jobOpening.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(jobOpening.getId().intValue()))
            .andExpect(jsonPath("$.postingTitle").value(DEFAULT_POSTING_TITLE))
            .andExpect(jsonPath("$.jobStatus").value(DEFAULT_JOB_STATUS))
            .andExpect(jsonPath("$.hiringLead").value(DEFAULT_HIRING_LEAD))
            .andExpect(jsonPath("$.departmentId").value(DEFAULT_DEPARTMENT_ID))
            .andExpect(jsonPath("$.employmentType").value(DEFAULT_EMPLOYMENT_TYPE))
            .andExpect(jsonPath("$.minimumExperience").value(DEFAULT_MINIMUM_EXPERIENCE))
            .andExpect(jsonPath("$.jobDescription").value(DEFAULT_JOB_DESCRIPTION));
    }

    @Test
    @Transactional
    void getJobOpeningsByIdFiltering() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        Long id = jobOpening.getId();

        defaultJobOpeningShouldBeFound("id.equals=" + id);
        defaultJobOpeningShouldNotBeFound("id.notEquals=" + id);

        defaultJobOpeningShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultJobOpeningShouldNotBeFound("id.greaterThan=" + id);

        defaultJobOpeningShouldBeFound("id.lessThanOrEqual=" + id);
        defaultJobOpeningShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllJobOpeningsByPostingTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList where postingTitle equals to DEFAULT_POSTING_TITLE
        defaultJobOpeningShouldBeFound("postingTitle.equals=" + DEFAULT_POSTING_TITLE);

        // Get all the jobOpeningList where postingTitle equals to UPDATED_POSTING_TITLE
        defaultJobOpeningShouldNotBeFound("postingTitle.equals=" + UPDATED_POSTING_TITLE);
    }

    @Test
    @Transactional
    void getAllJobOpeningsByPostingTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList where postingTitle not equals to DEFAULT_POSTING_TITLE
        defaultJobOpeningShouldNotBeFound("postingTitle.notEquals=" + DEFAULT_POSTING_TITLE);

        // Get all the jobOpeningList where postingTitle not equals to UPDATED_POSTING_TITLE
        defaultJobOpeningShouldBeFound("postingTitle.notEquals=" + UPDATED_POSTING_TITLE);
    }

    @Test
    @Transactional
    void getAllJobOpeningsByPostingTitleIsInShouldWork() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList where postingTitle in DEFAULT_POSTING_TITLE or UPDATED_POSTING_TITLE
        defaultJobOpeningShouldBeFound("postingTitle.in=" + DEFAULT_POSTING_TITLE + "," + UPDATED_POSTING_TITLE);

        // Get all the jobOpeningList where postingTitle equals to UPDATED_POSTING_TITLE
        defaultJobOpeningShouldNotBeFound("postingTitle.in=" + UPDATED_POSTING_TITLE);
    }

    @Test
    @Transactional
    void getAllJobOpeningsByPostingTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList where postingTitle is not null
        defaultJobOpeningShouldBeFound("postingTitle.specified=true");

        // Get all the jobOpeningList where postingTitle is null
        defaultJobOpeningShouldNotBeFound("postingTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllJobOpeningsByPostingTitleContainsSomething() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList where postingTitle contains DEFAULT_POSTING_TITLE
        defaultJobOpeningShouldBeFound("postingTitle.contains=" + DEFAULT_POSTING_TITLE);

        // Get all the jobOpeningList where postingTitle contains UPDATED_POSTING_TITLE
        defaultJobOpeningShouldNotBeFound("postingTitle.contains=" + UPDATED_POSTING_TITLE);
    }

    @Test
    @Transactional
    void getAllJobOpeningsByPostingTitleNotContainsSomething() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList where postingTitle does not contain DEFAULT_POSTING_TITLE
        defaultJobOpeningShouldNotBeFound("postingTitle.doesNotContain=" + DEFAULT_POSTING_TITLE);

        // Get all the jobOpeningList where postingTitle does not contain UPDATED_POSTING_TITLE
        defaultJobOpeningShouldBeFound("postingTitle.doesNotContain=" + UPDATED_POSTING_TITLE);
    }

    @Test
    @Transactional
    void getAllJobOpeningsByJobStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList where jobStatus equals to DEFAULT_JOB_STATUS
        defaultJobOpeningShouldBeFound("jobStatus.equals=" + DEFAULT_JOB_STATUS);

        // Get all the jobOpeningList where jobStatus equals to UPDATED_JOB_STATUS
        defaultJobOpeningShouldNotBeFound("jobStatus.equals=" + UPDATED_JOB_STATUS);
    }

    @Test
    @Transactional
    void getAllJobOpeningsByJobStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList where jobStatus not equals to DEFAULT_JOB_STATUS
        defaultJobOpeningShouldNotBeFound("jobStatus.notEquals=" + DEFAULT_JOB_STATUS);

        // Get all the jobOpeningList where jobStatus not equals to UPDATED_JOB_STATUS
        defaultJobOpeningShouldBeFound("jobStatus.notEquals=" + UPDATED_JOB_STATUS);
    }

    @Test
    @Transactional
    void getAllJobOpeningsByJobStatusIsInShouldWork() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList where jobStatus in DEFAULT_JOB_STATUS or UPDATED_JOB_STATUS
        defaultJobOpeningShouldBeFound("jobStatus.in=" + DEFAULT_JOB_STATUS + "," + UPDATED_JOB_STATUS);

        // Get all the jobOpeningList where jobStatus equals to UPDATED_JOB_STATUS
        defaultJobOpeningShouldNotBeFound("jobStatus.in=" + UPDATED_JOB_STATUS);
    }

    @Test
    @Transactional
    void getAllJobOpeningsByJobStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList where jobStatus is not null
        defaultJobOpeningShouldBeFound("jobStatus.specified=true");

        // Get all the jobOpeningList where jobStatus is null
        defaultJobOpeningShouldNotBeFound("jobStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllJobOpeningsByJobStatusIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList where jobStatus is greater than or equal to DEFAULT_JOB_STATUS
        defaultJobOpeningShouldBeFound("jobStatus.greaterThanOrEqual=" + DEFAULT_JOB_STATUS);

        // Get all the jobOpeningList where jobStatus is greater than or equal to UPDATED_JOB_STATUS
        defaultJobOpeningShouldNotBeFound("jobStatus.greaterThanOrEqual=" + UPDATED_JOB_STATUS);
    }

    @Test
    @Transactional
    void getAllJobOpeningsByJobStatusIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList where jobStatus is less than or equal to DEFAULT_JOB_STATUS
        defaultJobOpeningShouldBeFound("jobStatus.lessThanOrEqual=" + DEFAULT_JOB_STATUS);

        // Get all the jobOpeningList where jobStatus is less than or equal to SMALLER_JOB_STATUS
        defaultJobOpeningShouldNotBeFound("jobStatus.lessThanOrEqual=" + SMALLER_JOB_STATUS);
    }

    @Test
    @Transactional
    void getAllJobOpeningsByJobStatusIsLessThanSomething() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList where jobStatus is less than DEFAULT_JOB_STATUS
        defaultJobOpeningShouldNotBeFound("jobStatus.lessThan=" + DEFAULT_JOB_STATUS);

        // Get all the jobOpeningList where jobStatus is less than UPDATED_JOB_STATUS
        defaultJobOpeningShouldBeFound("jobStatus.lessThan=" + UPDATED_JOB_STATUS);
    }

    @Test
    @Transactional
    void getAllJobOpeningsByJobStatusIsGreaterThanSomething() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList where jobStatus is greater than DEFAULT_JOB_STATUS
        defaultJobOpeningShouldNotBeFound("jobStatus.greaterThan=" + DEFAULT_JOB_STATUS);

        // Get all the jobOpeningList where jobStatus is greater than SMALLER_JOB_STATUS
        defaultJobOpeningShouldBeFound("jobStatus.greaterThan=" + SMALLER_JOB_STATUS);
    }

    @Test
    @Transactional
    void getAllJobOpeningsByHiringLeadIsEqualToSomething() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList where hiringLead equals to DEFAULT_HIRING_LEAD
        defaultJobOpeningShouldBeFound("hiringLead.equals=" + DEFAULT_HIRING_LEAD);

        // Get all the jobOpeningList where hiringLead equals to UPDATED_HIRING_LEAD
        defaultJobOpeningShouldNotBeFound("hiringLead.equals=" + UPDATED_HIRING_LEAD);
    }

    @Test
    @Transactional
    void getAllJobOpeningsByHiringLeadIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList where hiringLead not equals to DEFAULT_HIRING_LEAD
        defaultJobOpeningShouldNotBeFound("hiringLead.notEquals=" + DEFAULT_HIRING_LEAD);

        // Get all the jobOpeningList where hiringLead not equals to UPDATED_HIRING_LEAD
        defaultJobOpeningShouldBeFound("hiringLead.notEquals=" + UPDATED_HIRING_LEAD);
    }

    @Test
    @Transactional
    void getAllJobOpeningsByHiringLeadIsInShouldWork() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList where hiringLead in DEFAULT_HIRING_LEAD or UPDATED_HIRING_LEAD
        defaultJobOpeningShouldBeFound("hiringLead.in=" + DEFAULT_HIRING_LEAD + "," + UPDATED_HIRING_LEAD);

        // Get all the jobOpeningList where hiringLead equals to UPDATED_HIRING_LEAD
        defaultJobOpeningShouldNotBeFound("hiringLead.in=" + UPDATED_HIRING_LEAD);
    }

    @Test
    @Transactional
    void getAllJobOpeningsByHiringLeadIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList where hiringLead is not null
        defaultJobOpeningShouldBeFound("hiringLead.specified=true");

        // Get all the jobOpeningList where hiringLead is null
        defaultJobOpeningShouldNotBeFound("hiringLead.specified=false");
    }

    @Test
    @Transactional
    void getAllJobOpeningsByHiringLeadIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList where hiringLead is greater than or equal to DEFAULT_HIRING_LEAD
        defaultJobOpeningShouldBeFound("hiringLead.greaterThanOrEqual=" + DEFAULT_HIRING_LEAD);

        // Get all the jobOpeningList where hiringLead is greater than or equal to UPDATED_HIRING_LEAD
        defaultJobOpeningShouldNotBeFound("hiringLead.greaterThanOrEqual=" + UPDATED_HIRING_LEAD);
    }

    @Test
    @Transactional
    void getAllJobOpeningsByHiringLeadIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList where hiringLead is less than or equal to DEFAULT_HIRING_LEAD
        defaultJobOpeningShouldBeFound("hiringLead.lessThanOrEqual=" + DEFAULT_HIRING_LEAD);

        // Get all the jobOpeningList where hiringLead is less than or equal to SMALLER_HIRING_LEAD
        defaultJobOpeningShouldNotBeFound("hiringLead.lessThanOrEqual=" + SMALLER_HIRING_LEAD);
    }

    @Test
    @Transactional
    void getAllJobOpeningsByHiringLeadIsLessThanSomething() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList where hiringLead is less than DEFAULT_HIRING_LEAD
        defaultJobOpeningShouldNotBeFound("hiringLead.lessThan=" + DEFAULT_HIRING_LEAD);

        // Get all the jobOpeningList where hiringLead is less than UPDATED_HIRING_LEAD
        defaultJobOpeningShouldBeFound("hiringLead.lessThan=" + UPDATED_HIRING_LEAD);
    }

    @Test
    @Transactional
    void getAllJobOpeningsByHiringLeadIsGreaterThanSomething() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList where hiringLead is greater than DEFAULT_HIRING_LEAD
        defaultJobOpeningShouldNotBeFound("hiringLead.greaterThan=" + DEFAULT_HIRING_LEAD);

        // Get all the jobOpeningList where hiringLead is greater than SMALLER_HIRING_LEAD
        defaultJobOpeningShouldBeFound("hiringLead.greaterThan=" + SMALLER_HIRING_LEAD);
    }

    @Test
    @Transactional
    void getAllJobOpeningsByDepartmentIdIsEqualToSomething() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList where departmentId equals to DEFAULT_DEPARTMENT_ID
        defaultJobOpeningShouldBeFound("departmentId.equals=" + DEFAULT_DEPARTMENT_ID);

        // Get all the jobOpeningList where departmentId equals to UPDATED_DEPARTMENT_ID
        defaultJobOpeningShouldNotBeFound("departmentId.equals=" + UPDATED_DEPARTMENT_ID);
    }

    @Test
    @Transactional
    void getAllJobOpeningsByDepartmentIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList where departmentId not equals to DEFAULT_DEPARTMENT_ID
        defaultJobOpeningShouldNotBeFound("departmentId.notEquals=" + DEFAULT_DEPARTMENT_ID);

        // Get all the jobOpeningList where departmentId not equals to UPDATED_DEPARTMENT_ID
        defaultJobOpeningShouldBeFound("departmentId.notEquals=" + UPDATED_DEPARTMENT_ID);
    }

    @Test
    @Transactional
    void getAllJobOpeningsByDepartmentIdIsInShouldWork() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList where departmentId in DEFAULT_DEPARTMENT_ID or UPDATED_DEPARTMENT_ID
        defaultJobOpeningShouldBeFound("departmentId.in=" + DEFAULT_DEPARTMENT_ID + "," + UPDATED_DEPARTMENT_ID);

        // Get all the jobOpeningList where departmentId equals to UPDATED_DEPARTMENT_ID
        defaultJobOpeningShouldNotBeFound("departmentId.in=" + UPDATED_DEPARTMENT_ID);
    }

    @Test
    @Transactional
    void getAllJobOpeningsByDepartmentIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList where departmentId is not null
        defaultJobOpeningShouldBeFound("departmentId.specified=true");

        // Get all the jobOpeningList where departmentId is null
        defaultJobOpeningShouldNotBeFound("departmentId.specified=false");
    }

    @Test
    @Transactional
    void getAllJobOpeningsByDepartmentIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList where departmentId is greater than or equal to DEFAULT_DEPARTMENT_ID
        defaultJobOpeningShouldBeFound("departmentId.greaterThanOrEqual=" + DEFAULT_DEPARTMENT_ID);

        // Get all the jobOpeningList where departmentId is greater than or equal to UPDATED_DEPARTMENT_ID
        defaultJobOpeningShouldNotBeFound("departmentId.greaterThanOrEqual=" + UPDATED_DEPARTMENT_ID);
    }

    @Test
    @Transactional
    void getAllJobOpeningsByDepartmentIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList where departmentId is less than or equal to DEFAULT_DEPARTMENT_ID
        defaultJobOpeningShouldBeFound("departmentId.lessThanOrEqual=" + DEFAULT_DEPARTMENT_ID);

        // Get all the jobOpeningList where departmentId is less than or equal to SMALLER_DEPARTMENT_ID
        defaultJobOpeningShouldNotBeFound("departmentId.lessThanOrEqual=" + SMALLER_DEPARTMENT_ID);
    }

    @Test
    @Transactional
    void getAllJobOpeningsByDepartmentIdIsLessThanSomething() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList where departmentId is less than DEFAULT_DEPARTMENT_ID
        defaultJobOpeningShouldNotBeFound("departmentId.lessThan=" + DEFAULT_DEPARTMENT_ID);

        // Get all the jobOpeningList where departmentId is less than UPDATED_DEPARTMENT_ID
        defaultJobOpeningShouldBeFound("departmentId.lessThan=" + UPDATED_DEPARTMENT_ID);
    }

    @Test
    @Transactional
    void getAllJobOpeningsByDepartmentIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList where departmentId is greater than DEFAULT_DEPARTMENT_ID
        defaultJobOpeningShouldNotBeFound("departmentId.greaterThan=" + DEFAULT_DEPARTMENT_ID);

        // Get all the jobOpeningList where departmentId is greater than SMALLER_DEPARTMENT_ID
        defaultJobOpeningShouldBeFound("departmentId.greaterThan=" + SMALLER_DEPARTMENT_ID);
    }

    @Test
    @Transactional
    void getAllJobOpeningsByEmploymentTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList where employmentType equals to DEFAULT_EMPLOYMENT_TYPE
        defaultJobOpeningShouldBeFound("employmentType.equals=" + DEFAULT_EMPLOYMENT_TYPE);

        // Get all the jobOpeningList where employmentType equals to UPDATED_EMPLOYMENT_TYPE
        defaultJobOpeningShouldNotBeFound("employmentType.equals=" + UPDATED_EMPLOYMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllJobOpeningsByEmploymentTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList where employmentType not equals to DEFAULT_EMPLOYMENT_TYPE
        defaultJobOpeningShouldNotBeFound("employmentType.notEquals=" + DEFAULT_EMPLOYMENT_TYPE);

        // Get all the jobOpeningList where employmentType not equals to UPDATED_EMPLOYMENT_TYPE
        defaultJobOpeningShouldBeFound("employmentType.notEquals=" + UPDATED_EMPLOYMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllJobOpeningsByEmploymentTypeIsInShouldWork() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList where employmentType in DEFAULT_EMPLOYMENT_TYPE or UPDATED_EMPLOYMENT_TYPE
        defaultJobOpeningShouldBeFound("employmentType.in=" + DEFAULT_EMPLOYMENT_TYPE + "," + UPDATED_EMPLOYMENT_TYPE);

        // Get all the jobOpeningList where employmentType equals to UPDATED_EMPLOYMENT_TYPE
        defaultJobOpeningShouldNotBeFound("employmentType.in=" + UPDATED_EMPLOYMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllJobOpeningsByEmploymentTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList where employmentType is not null
        defaultJobOpeningShouldBeFound("employmentType.specified=true");

        // Get all the jobOpeningList where employmentType is null
        defaultJobOpeningShouldNotBeFound("employmentType.specified=false");
    }

    @Test
    @Transactional
    void getAllJobOpeningsByEmploymentTypeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList where employmentType is greater than or equal to DEFAULT_EMPLOYMENT_TYPE
        defaultJobOpeningShouldBeFound("employmentType.greaterThanOrEqual=" + DEFAULT_EMPLOYMENT_TYPE);

        // Get all the jobOpeningList where employmentType is greater than or equal to UPDATED_EMPLOYMENT_TYPE
        defaultJobOpeningShouldNotBeFound("employmentType.greaterThanOrEqual=" + UPDATED_EMPLOYMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllJobOpeningsByEmploymentTypeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList where employmentType is less than or equal to DEFAULT_EMPLOYMENT_TYPE
        defaultJobOpeningShouldBeFound("employmentType.lessThanOrEqual=" + DEFAULT_EMPLOYMENT_TYPE);

        // Get all the jobOpeningList where employmentType is less than or equal to SMALLER_EMPLOYMENT_TYPE
        defaultJobOpeningShouldNotBeFound("employmentType.lessThanOrEqual=" + SMALLER_EMPLOYMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllJobOpeningsByEmploymentTypeIsLessThanSomething() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList where employmentType is less than DEFAULT_EMPLOYMENT_TYPE
        defaultJobOpeningShouldNotBeFound("employmentType.lessThan=" + DEFAULT_EMPLOYMENT_TYPE);

        // Get all the jobOpeningList where employmentType is less than UPDATED_EMPLOYMENT_TYPE
        defaultJobOpeningShouldBeFound("employmentType.lessThan=" + UPDATED_EMPLOYMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllJobOpeningsByEmploymentTypeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList where employmentType is greater than DEFAULT_EMPLOYMENT_TYPE
        defaultJobOpeningShouldNotBeFound("employmentType.greaterThan=" + DEFAULT_EMPLOYMENT_TYPE);

        // Get all the jobOpeningList where employmentType is greater than SMALLER_EMPLOYMENT_TYPE
        defaultJobOpeningShouldBeFound("employmentType.greaterThan=" + SMALLER_EMPLOYMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllJobOpeningsByMinimumExperienceIsEqualToSomething() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList where minimumExperience equals to DEFAULT_MINIMUM_EXPERIENCE
        defaultJobOpeningShouldBeFound("minimumExperience.equals=" + DEFAULT_MINIMUM_EXPERIENCE);

        // Get all the jobOpeningList where minimumExperience equals to UPDATED_MINIMUM_EXPERIENCE
        defaultJobOpeningShouldNotBeFound("minimumExperience.equals=" + UPDATED_MINIMUM_EXPERIENCE);
    }

    @Test
    @Transactional
    void getAllJobOpeningsByMinimumExperienceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList where minimumExperience not equals to DEFAULT_MINIMUM_EXPERIENCE
        defaultJobOpeningShouldNotBeFound("minimumExperience.notEquals=" + DEFAULT_MINIMUM_EXPERIENCE);

        // Get all the jobOpeningList where minimumExperience not equals to UPDATED_MINIMUM_EXPERIENCE
        defaultJobOpeningShouldBeFound("minimumExperience.notEquals=" + UPDATED_MINIMUM_EXPERIENCE);
    }

    @Test
    @Transactional
    void getAllJobOpeningsByMinimumExperienceIsInShouldWork() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList where minimumExperience in DEFAULT_MINIMUM_EXPERIENCE or UPDATED_MINIMUM_EXPERIENCE
        defaultJobOpeningShouldBeFound("minimumExperience.in=" + DEFAULT_MINIMUM_EXPERIENCE + "," + UPDATED_MINIMUM_EXPERIENCE);

        // Get all the jobOpeningList where minimumExperience equals to UPDATED_MINIMUM_EXPERIENCE
        defaultJobOpeningShouldNotBeFound("minimumExperience.in=" + UPDATED_MINIMUM_EXPERIENCE);
    }

    @Test
    @Transactional
    void getAllJobOpeningsByMinimumExperienceIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList where minimumExperience is not null
        defaultJobOpeningShouldBeFound("minimumExperience.specified=true");

        // Get all the jobOpeningList where minimumExperience is null
        defaultJobOpeningShouldNotBeFound("minimumExperience.specified=false");
    }

    @Test
    @Transactional
    void getAllJobOpeningsByMinimumExperienceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList where minimumExperience is greater than or equal to DEFAULT_MINIMUM_EXPERIENCE
        defaultJobOpeningShouldBeFound("minimumExperience.greaterThanOrEqual=" + DEFAULT_MINIMUM_EXPERIENCE);

        // Get all the jobOpeningList where minimumExperience is greater than or equal to UPDATED_MINIMUM_EXPERIENCE
        defaultJobOpeningShouldNotBeFound("minimumExperience.greaterThanOrEqual=" + UPDATED_MINIMUM_EXPERIENCE);
    }

    @Test
    @Transactional
    void getAllJobOpeningsByMinimumExperienceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList where minimumExperience is less than or equal to DEFAULT_MINIMUM_EXPERIENCE
        defaultJobOpeningShouldBeFound("minimumExperience.lessThanOrEqual=" + DEFAULT_MINIMUM_EXPERIENCE);

        // Get all the jobOpeningList where minimumExperience is less than or equal to SMALLER_MINIMUM_EXPERIENCE
        defaultJobOpeningShouldNotBeFound("minimumExperience.lessThanOrEqual=" + SMALLER_MINIMUM_EXPERIENCE);
    }

    @Test
    @Transactional
    void getAllJobOpeningsByMinimumExperienceIsLessThanSomething() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList where minimumExperience is less than DEFAULT_MINIMUM_EXPERIENCE
        defaultJobOpeningShouldNotBeFound("minimumExperience.lessThan=" + DEFAULT_MINIMUM_EXPERIENCE);

        // Get all the jobOpeningList where minimumExperience is less than UPDATED_MINIMUM_EXPERIENCE
        defaultJobOpeningShouldBeFound("minimumExperience.lessThan=" + UPDATED_MINIMUM_EXPERIENCE);
    }

    @Test
    @Transactional
    void getAllJobOpeningsByMinimumExperienceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList where minimumExperience is greater than DEFAULT_MINIMUM_EXPERIENCE
        defaultJobOpeningShouldNotBeFound("minimumExperience.greaterThan=" + DEFAULT_MINIMUM_EXPERIENCE);

        // Get all the jobOpeningList where minimumExperience is greater than SMALLER_MINIMUM_EXPERIENCE
        defaultJobOpeningShouldBeFound("minimumExperience.greaterThan=" + SMALLER_MINIMUM_EXPERIENCE);
    }

    @Test
    @Transactional
    void getAllJobOpeningsByJobDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList where jobDescription equals to DEFAULT_JOB_DESCRIPTION
        defaultJobOpeningShouldBeFound("jobDescription.equals=" + DEFAULT_JOB_DESCRIPTION);

        // Get all the jobOpeningList where jobDescription equals to UPDATED_JOB_DESCRIPTION
        defaultJobOpeningShouldNotBeFound("jobDescription.equals=" + UPDATED_JOB_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllJobOpeningsByJobDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList where jobDescription not equals to DEFAULT_JOB_DESCRIPTION
        defaultJobOpeningShouldNotBeFound("jobDescription.notEquals=" + DEFAULT_JOB_DESCRIPTION);

        // Get all the jobOpeningList where jobDescription not equals to UPDATED_JOB_DESCRIPTION
        defaultJobOpeningShouldBeFound("jobDescription.notEquals=" + UPDATED_JOB_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllJobOpeningsByJobDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList where jobDescription in DEFAULT_JOB_DESCRIPTION or UPDATED_JOB_DESCRIPTION
        defaultJobOpeningShouldBeFound("jobDescription.in=" + DEFAULT_JOB_DESCRIPTION + "," + UPDATED_JOB_DESCRIPTION);

        // Get all the jobOpeningList where jobDescription equals to UPDATED_JOB_DESCRIPTION
        defaultJobOpeningShouldNotBeFound("jobDescription.in=" + UPDATED_JOB_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllJobOpeningsByJobDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList where jobDescription is not null
        defaultJobOpeningShouldBeFound("jobDescription.specified=true");

        // Get all the jobOpeningList where jobDescription is null
        defaultJobOpeningShouldNotBeFound("jobDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllJobOpeningsByJobDescriptionContainsSomething() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList where jobDescription contains DEFAULT_JOB_DESCRIPTION
        defaultJobOpeningShouldBeFound("jobDescription.contains=" + DEFAULT_JOB_DESCRIPTION);

        // Get all the jobOpeningList where jobDescription contains UPDATED_JOB_DESCRIPTION
        defaultJobOpeningShouldNotBeFound("jobDescription.contains=" + UPDATED_JOB_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllJobOpeningsByJobDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        // Get all the jobOpeningList where jobDescription does not contain DEFAULT_JOB_DESCRIPTION
        defaultJobOpeningShouldNotBeFound("jobDescription.doesNotContain=" + DEFAULT_JOB_DESCRIPTION);

        // Get all the jobOpeningList where jobDescription does not contain UPDATED_JOB_DESCRIPTION
        defaultJobOpeningShouldBeFound("jobDescription.doesNotContain=" + UPDATED_JOB_DESCRIPTION);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultJobOpeningShouldBeFound(String filter) throws Exception {
        restJobOpeningMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobOpening.getId().intValue())))
            .andExpect(jsonPath("$.[*].postingTitle").value(hasItem(DEFAULT_POSTING_TITLE)))
            .andExpect(jsonPath("$.[*].jobStatus").value(hasItem(DEFAULT_JOB_STATUS)))
            .andExpect(jsonPath("$.[*].hiringLead").value(hasItem(DEFAULT_HIRING_LEAD)))
            .andExpect(jsonPath("$.[*].departmentId").value(hasItem(DEFAULT_DEPARTMENT_ID)))
            .andExpect(jsonPath("$.[*].employmentType").value(hasItem(DEFAULT_EMPLOYMENT_TYPE)))
            .andExpect(jsonPath("$.[*].minimumExperience").value(hasItem(DEFAULT_MINIMUM_EXPERIENCE)))
            .andExpect(jsonPath("$.[*].jobDescription").value(hasItem(DEFAULT_JOB_DESCRIPTION)));

        // Check, that the count call also returns 1
        restJobOpeningMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultJobOpeningShouldNotBeFound(String filter) throws Exception {
        restJobOpeningMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restJobOpeningMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingJobOpening() throws Exception {
        // Get the jobOpening
        restJobOpeningMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewJobOpening() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        int databaseSizeBeforeUpdate = jobOpeningRepository.findAll().size();

        // Update the jobOpening
        JobOpening updatedJobOpening = jobOpeningRepository.findById(jobOpening.getId()).get();
        // Disconnect from session so that the updates on updatedJobOpening are not directly saved in db
        em.detach(updatedJobOpening);
        updatedJobOpening
            .postingTitle(UPDATED_POSTING_TITLE)
            .jobStatus(UPDATED_JOB_STATUS)
            .hiringLead(UPDATED_HIRING_LEAD)
            .departmentId(UPDATED_DEPARTMENT_ID)
            .employmentType(UPDATED_EMPLOYMENT_TYPE)
            .minimumExperience(UPDATED_MINIMUM_EXPERIENCE)
            .jobDescription(UPDATED_JOB_DESCRIPTION);
        JobOpeningDTO jobOpeningDTO = jobOpeningMapper.toDto(updatedJobOpening);

        restJobOpeningMockMvc
            .perform(
                put(ENTITY_API_URL_ID, jobOpeningDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jobOpeningDTO))
            )
            .andExpect(status().isOk());

        // Validate the JobOpening in the database
        List<JobOpening> jobOpeningList = jobOpeningRepository.findAll();
        assertThat(jobOpeningList).hasSize(databaseSizeBeforeUpdate);
        JobOpening testJobOpening = jobOpeningList.get(jobOpeningList.size() - 1);
        assertThat(testJobOpening.getPostingTitle()).isEqualTo(UPDATED_POSTING_TITLE);
        assertThat(testJobOpening.getJobStatus()).isEqualTo(UPDATED_JOB_STATUS);
        assertThat(testJobOpening.getHiringLead()).isEqualTo(UPDATED_HIRING_LEAD);
        assertThat(testJobOpening.getDepartmentId()).isEqualTo(UPDATED_DEPARTMENT_ID);
        assertThat(testJobOpening.getEmploymentType()).isEqualTo(UPDATED_EMPLOYMENT_TYPE);
        assertThat(testJobOpening.getMinimumExperience()).isEqualTo(UPDATED_MINIMUM_EXPERIENCE);
        assertThat(testJobOpening.getJobDescription()).isEqualTo(UPDATED_JOB_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingJobOpening() throws Exception {
        int databaseSizeBeforeUpdate = jobOpeningRepository.findAll().size();
        jobOpening.setId(count.incrementAndGet());

        // Create the JobOpening
        JobOpeningDTO jobOpeningDTO = jobOpeningMapper.toDto(jobOpening);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJobOpeningMockMvc
            .perform(
                put(ENTITY_API_URL_ID, jobOpeningDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jobOpeningDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobOpening in the database
        List<JobOpening> jobOpeningList = jobOpeningRepository.findAll();
        assertThat(jobOpeningList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchJobOpening() throws Exception {
        int databaseSizeBeforeUpdate = jobOpeningRepository.findAll().size();
        jobOpening.setId(count.incrementAndGet());

        // Create the JobOpening
        JobOpeningDTO jobOpeningDTO = jobOpeningMapper.toDto(jobOpening);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobOpeningMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jobOpeningDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobOpening in the database
        List<JobOpening> jobOpeningList = jobOpeningRepository.findAll();
        assertThat(jobOpeningList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamJobOpening() throws Exception {
        int databaseSizeBeforeUpdate = jobOpeningRepository.findAll().size();
        jobOpening.setId(count.incrementAndGet());

        // Create the JobOpening
        JobOpeningDTO jobOpeningDTO = jobOpeningMapper.toDto(jobOpening);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobOpeningMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobOpeningDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the JobOpening in the database
        List<JobOpening> jobOpeningList = jobOpeningRepository.findAll();
        assertThat(jobOpeningList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateJobOpeningWithPatch() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        int databaseSizeBeforeUpdate = jobOpeningRepository.findAll().size();

        // Update the jobOpening using partial update
        JobOpening partialUpdatedJobOpening = new JobOpening();
        partialUpdatedJobOpening.setId(jobOpening.getId());

        partialUpdatedJobOpening
            .jobStatus(UPDATED_JOB_STATUS)
            .departmentId(UPDATED_DEPARTMENT_ID)
            .minimumExperience(UPDATED_MINIMUM_EXPERIENCE)
            .jobDescription(UPDATED_JOB_DESCRIPTION);

        restJobOpeningMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJobOpening.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJobOpening))
            )
            .andExpect(status().isOk());

        // Validate the JobOpening in the database
        List<JobOpening> jobOpeningList = jobOpeningRepository.findAll();
        assertThat(jobOpeningList).hasSize(databaseSizeBeforeUpdate);
        JobOpening testJobOpening = jobOpeningList.get(jobOpeningList.size() - 1);
        assertThat(testJobOpening.getPostingTitle()).isEqualTo(DEFAULT_POSTING_TITLE);
        assertThat(testJobOpening.getJobStatus()).isEqualTo(UPDATED_JOB_STATUS);
        assertThat(testJobOpening.getHiringLead()).isEqualTo(DEFAULT_HIRING_LEAD);
        assertThat(testJobOpening.getDepartmentId()).isEqualTo(UPDATED_DEPARTMENT_ID);
        assertThat(testJobOpening.getEmploymentType()).isEqualTo(DEFAULT_EMPLOYMENT_TYPE);
        assertThat(testJobOpening.getMinimumExperience()).isEqualTo(UPDATED_MINIMUM_EXPERIENCE);
        assertThat(testJobOpening.getJobDescription()).isEqualTo(UPDATED_JOB_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateJobOpeningWithPatch() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        int databaseSizeBeforeUpdate = jobOpeningRepository.findAll().size();

        // Update the jobOpening using partial update
        JobOpening partialUpdatedJobOpening = new JobOpening();
        partialUpdatedJobOpening.setId(jobOpening.getId());

        partialUpdatedJobOpening
            .postingTitle(UPDATED_POSTING_TITLE)
            .jobStatus(UPDATED_JOB_STATUS)
            .hiringLead(UPDATED_HIRING_LEAD)
            .departmentId(UPDATED_DEPARTMENT_ID)
            .employmentType(UPDATED_EMPLOYMENT_TYPE)
            .minimumExperience(UPDATED_MINIMUM_EXPERIENCE)
            .jobDescription(UPDATED_JOB_DESCRIPTION);

        restJobOpeningMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJobOpening.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJobOpening))
            )
            .andExpect(status().isOk());

        // Validate the JobOpening in the database
        List<JobOpening> jobOpeningList = jobOpeningRepository.findAll();
        assertThat(jobOpeningList).hasSize(databaseSizeBeforeUpdate);
        JobOpening testJobOpening = jobOpeningList.get(jobOpeningList.size() - 1);
        assertThat(testJobOpening.getPostingTitle()).isEqualTo(UPDATED_POSTING_TITLE);
        assertThat(testJobOpening.getJobStatus()).isEqualTo(UPDATED_JOB_STATUS);
        assertThat(testJobOpening.getHiringLead()).isEqualTo(UPDATED_HIRING_LEAD);
        assertThat(testJobOpening.getDepartmentId()).isEqualTo(UPDATED_DEPARTMENT_ID);
        assertThat(testJobOpening.getEmploymentType()).isEqualTo(UPDATED_EMPLOYMENT_TYPE);
        assertThat(testJobOpening.getMinimumExperience()).isEqualTo(UPDATED_MINIMUM_EXPERIENCE);
        assertThat(testJobOpening.getJobDescription()).isEqualTo(UPDATED_JOB_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingJobOpening() throws Exception {
        int databaseSizeBeforeUpdate = jobOpeningRepository.findAll().size();
        jobOpening.setId(count.incrementAndGet());

        // Create the JobOpening
        JobOpeningDTO jobOpeningDTO = jobOpeningMapper.toDto(jobOpening);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJobOpeningMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, jobOpeningDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(jobOpeningDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobOpening in the database
        List<JobOpening> jobOpeningList = jobOpeningRepository.findAll();
        assertThat(jobOpeningList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchJobOpening() throws Exception {
        int databaseSizeBeforeUpdate = jobOpeningRepository.findAll().size();
        jobOpening.setId(count.incrementAndGet());

        // Create the JobOpening
        JobOpeningDTO jobOpeningDTO = jobOpeningMapper.toDto(jobOpening);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobOpeningMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(jobOpeningDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobOpening in the database
        List<JobOpening> jobOpeningList = jobOpeningRepository.findAll();
        assertThat(jobOpeningList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamJobOpening() throws Exception {
        int databaseSizeBeforeUpdate = jobOpeningRepository.findAll().size();
        jobOpening.setId(count.incrementAndGet());

        // Create the JobOpening
        JobOpeningDTO jobOpeningDTO = jobOpeningMapper.toDto(jobOpening);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobOpeningMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(jobOpeningDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the JobOpening in the database
        List<JobOpening> jobOpeningList = jobOpeningRepository.findAll();
        assertThat(jobOpeningList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteJobOpening() throws Exception {
        // Initialize the database
        jobOpeningRepository.saveAndFlush(jobOpening);

        int databaseSizeBeforeDelete = jobOpeningRepository.findAll().size();

        // Delete the jobOpening
        restJobOpeningMockMvc
            .perform(delete(ENTITY_API_URL_ID, jobOpening.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<JobOpening> jobOpeningList = jobOpeningRepository.findAll();
        assertThat(jobOpeningList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
