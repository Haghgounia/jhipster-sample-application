package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.County;
import com.mycompany.myapp.domain.Province;
import com.mycompany.myapp.repository.CountyRepository;
import com.mycompany.myapp.service.criteria.CountyCriteria;
import com.mycompany.myapp.service.dto.CountyDTO;
import com.mycompany.myapp.service.mapper.CountyMapper;
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
 * Integration tests for the {@link CountyResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CountyResourceIT {

    private static final Integer DEFAULT_COUNTY_ID = 1;
    private static final Integer UPDATED_COUNTY_ID = 2;
    private static final Integer SMALLER_COUNTY_ID = 1 - 1;

    private static final String DEFAULT_COUNTY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_COUNTY_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COUNTY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTY_ENGLISH_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COUNTY_ENGLISH_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/counties";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CountyRepository countyRepository;

    @Autowired
    private CountyMapper countyMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCountyMockMvc;

    private County county;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static County createEntity(EntityManager em) {
        County county = new County()
            .countyId(DEFAULT_COUNTY_ID)
            .countyCode(DEFAULT_COUNTY_CODE)
            .countyName(DEFAULT_COUNTY_NAME)
            .countyEnglishName(DEFAULT_COUNTY_ENGLISH_NAME);
        return county;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static County createUpdatedEntity(EntityManager em) {
        County county = new County()
            .countyId(UPDATED_COUNTY_ID)
            .countyCode(UPDATED_COUNTY_CODE)
            .countyName(UPDATED_COUNTY_NAME)
            .countyEnglishName(UPDATED_COUNTY_ENGLISH_NAME);
        return county;
    }

    @BeforeEach
    public void initTest() {
        county = createEntity(em);
    }

    @Test
    @Transactional
    void createCounty() throws Exception {
        int databaseSizeBeforeCreate = countyRepository.findAll().size();
        // Create the County
        CountyDTO countyDTO = countyMapper.toDto(county);
        restCountyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countyDTO)))
            .andExpect(status().isCreated());

        // Validate the County in the database
        List<County> countyList = countyRepository.findAll();
        assertThat(countyList).hasSize(databaseSizeBeforeCreate + 1);
        County testCounty = countyList.get(countyList.size() - 1);
        assertThat(testCounty.getCountyId()).isEqualTo(DEFAULT_COUNTY_ID);
        assertThat(testCounty.getCountyCode()).isEqualTo(DEFAULT_COUNTY_CODE);
        assertThat(testCounty.getCountyName()).isEqualTo(DEFAULT_COUNTY_NAME);
        assertThat(testCounty.getCountyEnglishName()).isEqualTo(DEFAULT_COUNTY_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void createCountyWithExistingId() throws Exception {
        // Create the County with an existing ID
        county.setId(1L);
        CountyDTO countyDTO = countyMapper.toDto(county);

        int databaseSizeBeforeCreate = countyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCountyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the County in the database
        List<County> countyList = countyRepository.findAll();
        assertThat(countyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCountyIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = countyRepository.findAll().size();
        // set the field null
        county.setCountyId(null);

        // Create the County, which fails.
        CountyDTO countyDTO = countyMapper.toDto(county);

        restCountyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countyDTO)))
            .andExpect(status().isBadRequest());

        List<County> countyList = countyRepository.findAll();
        assertThat(countyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCountyCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = countyRepository.findAll().size();
        // set the field null
        county.setCountyCode(null);

        // Create the County, which fails.
        CountyDTO countyDTO = countyMapper.toDto(county);

        restCountyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countyDTO)))
            .andExpect(status().isBadRequest());

        List<County> countyList = countyRepository.findAll();
        assertThat(countyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCounties() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList
        restCountyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(county.getId().intValue())))
            .andExpect(jsonPath("$.[*].countyId").value(hasItem(DEFAULT_COUNTY_ID)))
            .andExpect(jsonPath("$.[*].countyCode").value(hasItem(DEFAULT_COUNTY_CODE)))
            .andExpect(jsonPath("$.[*].countyName").value(hasItem(DEFAULT_COUNTY_NAME)))
            .andExpect(jsonPath("$.[*].countyEnglishName").value(hasItem(DEFAULT_COUNTY_ENGLISH_NAME)));
    }

    @Test
    @Transactional
    void getCounty() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get the county
        restCountyMockMvc
            .perform(get(ENTITY_API_URL_ID, county.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(county.getId().intValue()))
            .andExpect(jsonPath("$.countyId").value(DEFAULT_COUNTY_ID))
            .andExpect(jsonPath("$.countyCode").value(DEFAULT_COUNTY_CODE))
            .andExpect(jsonPath("$.countyName").value(DEFAULT_COUNTY_NAME))
            .andExpect(jsonPath("$.countyEnglishName").value(DEFAULT_COUNTY_ENGLISH_NAME));
    }

    @Test
    @Transactional
    void getCountiesByIdFiltering() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        Long id = county.getId();

        defaultCountyShouldBeFound("id.equals=" + id);
        defaultCountyShouldNotBeFound("id.notEquals=" + id);

        defaultCountyShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCountyShouldNotBeFound("id.greaterThan=" + id);

        defaultCountyShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCountyShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCountiesByCountyIdIsEqualToSomething() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where countyId equals to DEFAULT_COUNTY_ID
        defaultCountyShouldBeFound("countyId.equals=" + DEFAULT_COUNTY_ID);

        // Get all the countyList where countyId equals to UPDATED_COUNTY_ID
        defaultCountyShouldNotBeFound("countyId.equals=" + UPDATED_COUNTY_ID);
    }

    @Test
    @Transactional
    void getAllCountiesByCountyIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where countyId not equals to DEFAULT_COUNTY_ID
        defaultCountyShouldNotBeFound("countyId.notEquals=" + DEFAULT_COUNTY_ID);

        // Get all the countyList where countyId not equals to UPDATED_COUNTY_ID
        defaultCountyShouldBeFound("countyId.notEquals=" + UPDATED_COUNTY_ID);
    }

    @Test
    @Transactional
    void getAllCountiesByCountyIdIsInShouldWork() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where countyId in DEFAULT_COUNTY_ID or UPDATED_COUNTY_ID
        defaultCountyShouldBeFound("countyId.in=" + DEFAULT_COUNTY_ID + "," + UPDATED_COUNTY_ID);

        // Get all the countyList where countyId equals to UPDATED_COUNTY_ID
        defaultCountyShouldNotBeFound("countyId.in=" + UPDATED_COUNTY_ID);
    }

    @Test
    @Transactional
    void getAllCountiesByCountyIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where countyId is not null
        defaultCountyShouldBeFound("countyId.specified=true");

        // Get all the countyList where countyId is null
        defaultCountyShouldNotBeFound("countyId.specified=false");
    }

    @Test
    @Transactional
    void getAllCountiesByCountyIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where countyId is greater than or equal to DEFAULT_COUNTY_ID
        defaultCountyShouldBeFound("countyId.greaterThanOrEqual=" + DEFAULT_COUNTY_ID);

        // Get all the countyList where countyId is greater than or equal to UPDATED_COUNTY_ID
        defaultCountyShouldNotBeFound("countyId.greaterThanOrEqual=" + UPDATED_COUNTY_ID);
    }

    @Test
    @Transactional
    void getAllCountiesByCountyIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where countyId is less than or equal to DEFAULT_COUNTY_ID
        defaultCountyShouldBeFound("countyId.lessThanOrEqual=" + DEFAULT_COUNTY_ID);

        // Get all the countyList where countyId is less than or equal to SMALLER_COUNTY_ID
        defaultCountyShouldNotBeFound("countyId.lessThanOrEqual=" + SMALLER_COUNTY_ID);
    }

    @Test
    @Transactional
    void getAllCountiesByCountyIdIsLessThanSomething() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where countyId is less than DEFAULT_COUNTY_ID
        defaultCountyShouldNotBeFound("countyId.lessThan=" + DEFAULT_COUNTY_ID);

        // Get all the countyList where countyId is less than UPDATED_COUNTY_ID
        defaultCountyShouldBeFound("countyId.lessThan=" + UPDATED_COUNTY_ID);
    }

    @Test
    @Transactional
    void getAllCountiesByCountyIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where countyId is greater than DEFAULT_COUNTY_ID
        defaultCountyShouldNotBeFound("countyId.greaterThan=" + DEFAULT_COUNTY_ID);

        // Get all the countyList where countyId is greater than SMALLER_COUNTY_ID
        defaultCountyShouldBeFound("countyId.greaterThan=" + SMALLER_COUNTY_ID);
    }

    @Test
    @Transactional
    void getAllCountiesByCountyCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where countyCode equals to DEFAULT_COUNTY_CODE
        defaultCountyShouldBeFound("countyCode.equals=" + DEFAULT_COUNTY_CODE);

        // Get all the countyList where countyCode equals to UPDATED_COUNTY_CODE
        defaultCountyShouldNotBeFound("countyCode.equals=" + UPDATED_COUNTY_CODE);
    }

    @Test
    @Transactional
    void getAllCountiesByCountyCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where countyCode not equals to DEFAULT_COUNTY_CODE
        defaultCountyShouldNotBeFound("countyCode.notEquals=" + DEFAULT_COUNTY_CODE);

        // Get all the countyList where countyCode not equals to UPDATED_COUNTY_CODE
        defaultCountyShouldBeFound("countyCode.notEquals=" + UPDATED_COUNTY_CODE);
    }

    @Test
    @Transactional
    void getAllCountiesByCountyCodeIsInShouldWork() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where countyCode in DEFAULT_COUNTY_CODE or UPDATED_COUNTY_CODE
        defaultCountyShouldBeFound("countyCode.in=" + DEFAULT_COUNTY_CODE + "," + UPDATED_COUNTY_CODE);

        // Get all the countyList where countyCode equals to UPDATED_COUNTY_CODE
        defaultCountyShouldNotBeFound("countyCode.in=" + UPDATED_COUNTY_CODE);
    }

    @Test
    @Transactional
    void getAllCountiesByCountyCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where countyCode is not null
        defaultCountyShouldBeFound("countyCode.specified=true");

        // Get all the countyList where countyCode is null
        defaultCountyShouldNotBeFound("countyCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCountiesByCountyCodeContainsSomething() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where countyCode contains DEFAULT_COUNTY_CODE
        defaultCountyShouldBeFound("countyCode.contains=" + DEFAULT_COUNTY_CODE);

        // Get all the countyList where countyCode contains UPDATED_COUNTY_CODE
        defaultCountyShouldNotBeFound("countyCode.contains=" + UPDATED_COUNTY_CODE);
    }

    @Test
    @Transactional
    void getAllCountiesByCountyCodeNotContainsSomething() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where countyCode does not contain DEFAULT_COUNTY_CODE
        defaultCountyShouldNotBeFound("countyCode.doesNotContain=" + DEFAULT_COUNTY_CODE);

        // Get all the countyList where countyCode does not contain UPDATED_COUNTY_CODE
        defaultCountyShouldBeFound("countyCode.doesNotContain=" + UPDATED_COUNTY_CODE);
    }

    @Test
    @Transactional
    void getAllCountiesByCountyNameIsEqualToSomething() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where countyName equals to DEFAULT_COUNTY_NAME
        defaultCountyShouldBeFound("countyName.equals=" + DEFAULT_COUNTY_NAME);

        // Get all the countyList where countyName equals to UPDATED_COUNTY_NAME
        defaultCountyShouldNotBeFound("countyName.equals=" + UPDATED_COUNTY_NAME);
    }

    @Test
    @Transactional
    void getAllCountiesByCountyNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where countyName not equals to DEFAULT_COUNTY_NAME
        defaultCountyShouldNotBeFound("countyName.notEquals=" + DEFAULT_COUNTY_NAME);

        // Get all the countyList where countyName not equals to UPDATED_COUNTY_NAME
        defaultCountyShouldBeFound("countyName.notEquals=" + UPDATED_COUNTY_NAME);
    }

    @Test
    @Transactional
    void getAllCountiesByCountyNameIsInShouldWork() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where countyName in DEFAULT_COUNTY_NAME or UPDATED_COUNTY_NAME
        defaultCountyShouldBeFound("countyName.in=" + DEFAULT_COUNTY_NAME + "," + UPDATED_COUNTY_NAME);

        // Get all the countyList where countyName equals to UPDATED_COUNTY_NAME
        defaultCountyShouldNotBeFound("countyName.in=" + UPDATED_COUNTY_NAME);
    }

    @Test
    @Transactional
    void getAllCountiesByCountyNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where countyName is not null
        defaultCountyShouldBeFound("countyName.specified=true");

        // Get all the countyList where countyName is null
        defaultCountyShouldNotBeFound("countyName.specified=false");
    }

    @Test
    @Transactional
    void getAllCountiesByCountyNameContainsSomething() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where countyName contains DEFAULT_COUNTY_NAME
        defaultCountyShouldBeFound("countyName.contains=" + DEFAULT_COUNTY_NAME);

        // Get all the countyList where countyName contains UPDATED_COUNTY_NAME
        defaultCountyShouldNotBeFound("countyName.contains=" + UPDATED_COUNTY_NAME);
    }

    @Test
    @Transactional
    void getAllCountiesByCountyNameNotContainsSomething() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where countyName does not contain DEFAULT_COUNTY_NAME
        defaultCountyShouldNotBeFound("countyName.doesNotContain=" + DEFAULT_COUNTY_NAME);

        // Get all the countyList where countyName does not contain UPDATED_COUNTY_NAME
        defaultCountyShouldBeFound("countyName.doesNotContain=" + UPDATED_COUNTY_NAME);
    }

    @Test
    @Transactional
    void getAllCountiesByCountyEnglishNameIsEqualToSomething() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where countyEnglishName equals to DEFAULT_COUNTY_ENGLISH_NAME
        defaultCountyShouldBeFound("countyEnglishName.equals=" + DEFAULT_COUNTY_ENGLISH_NAME);

        // Get all the countyList where countyEnglishName equals to UPDATED_COUNTY_ENGLISH_NAME
        defaultCountyShouldNotBeFound("countyEnglishName.equals=" + UPDATED_COUNTY_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void getAllCountiesByCountyEnglishNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where countyEnglishName not equals to DEFAULT_COUNTY_ENGLISH_NAME
        defaultCountyShouldNotBeFound("countyEnglishName.notEquals=" + DEFAULT_COUNTY_ENGLISH_NAME);

        // Get all the countyList where countyEnglishName not equals to UPDATED_COUNTY_ENGLISH_NAME
        defaultCountyShouldBeFound("countyEnglishName.notEquals=" + UPDATED_COUNTY_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void getAllCountiesByCountyEnglishNameIsInShouldWork() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where countyEnglishName in DEFAULT_COUNTY_ENGLISH_NAME or UPDATED_COUNTY_ENGLISH_NAME
        defaultCountyShouldBeFound("countyEnglishName.in=" + DEFAULT_COUNTY_ENGLISH_NAME + "," + UPDATED_COUNTY_ENGLISH_NAME);

        // Get all the countyList where countyEnglishName equals to UPDATED_COUNTY_ENGLISH_NAME
        defaultCountyShouldNotBeFound("countyEnglishName.in=" + UPDATED_COUNTY_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void getAllCountiesByCountyEnglishNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where countyEnglishName is not null
        defaultCountyShouldBeFound("countyEnglishName.specified=true");

        // Get all the countyList where countyEnglishName is null
        defaultCountyShouldNotBeFound("countyEnglishName.specified=false");
    }

    @Test
    @Transactional
    void getAllCountiesByCountyEnglishNameContainsSomething() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where countyEnglishName contains DEFAULT_COUNTY_ENGLISH_NAME
        defaultCountyShouldBeFound("countyEnglishName.contains=" + DEFAULT_COUNTY_ENGLISH_NAME);

        // Get all the countyList where countyEnglishName contains UPDATED_COUNTY_ENGLISH_NAME
        defaultCountyShouldNotBeFound("countyEnglishName.contains=" + UPDATED_COUNTY_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void getAllCountiesByCountyEnglishNameNotContainsSomething() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where countyEnglishName does not contain DEFAULT_COUNTY_ENGLISH_NAME
        defaultCountyShouldNotBeFound("countyEnglishName.doesNotContain=" + DEFAULT_COUNTY_ENGLISH_NAME);

        // Get all the countyList where countyEnglishName does not contain UPDATED_COUNTY_ENGLISH_NAME
        defaultCountyShouldBeFound("countyEnglishName.doesNotContain=" + UPDATED_COUNTY_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void getAllCountiesByProvinceIsEqualToSomething() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);
        Province province;
        if (TestUtil.findAll(em, Province.class).isEmpty()) {
            province = ProvinceResourceIT.createEntity(em);
            em.persist(province);
            em.flush();
        } else {
            province = TestUtil.findAll(em, Province.class).get(0);
        }
        em.persist(province);
        em.flush();
        county.setProvince(province);
        countyRepository.saveAndFlush(county);
        Long provinceId = province.getId();

        // Get all the countyList where province equals to provinceId
        defaultCountyShouldBeFound("provinceId.equals=" + provinceId);

        // Get all the countyList where province equals to (provinceId + 1)
        defaultCountyShouldNotBeFound("provinceId.equals=" + (provinceId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCountyShouldBeFound(String filter) throws Exception {
        restCountyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(county.getId().intValue())))
            .andExpect(jsonPath("$.[*].countyId").value(hasItem(DEFAULT_COUNTY_ID)))
            .andExpect(jsonPath("$.[*].countyCode").value(hasItem(DEFAULT_COUNTY_CODE)))
            .andExpect(jsonPath("$.[*].countyName").value(hasItem(DEFAULT_COUNTY_NAME)))
            .andExpect(jsonPath("$.[*].countyEnglishName").value(hasItem(DEFAULT_COUNTY_ENGLISH_NAME)));

        // Check, that the count call also returns 1
        restCountyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCountyShouldNotBeFound(String filter) throws Exception {
        restCountyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCountyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCounty() throws Exception {
        // Get the county
        restCountyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCounty() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        int databaseSizeBeforeUpdate = countyRepository.findAll().size();

        // Update the county
        County updatedCounty = countyRepository.findById(county.getId()).get();
        // Disconnect from session so that the updates on updatedCounty are not directly saved in db
        em.detach(updatedCounty);
        updatedCounty
            .countyId(UPDATED_COUNTY_ID)
            .countyCode(UPDATED_COUNTY_CODE)
            .countyName(UPDATED_COUNTY_NAME)
            .countyEnglishName(UPDATED_COUNTY_ENGLISH_NAME);
        CountyDTO countyDTO = countyMapper.toDto(updatedCounty);

        restCountyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, countyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countyDTO))
            )
            .andExpect(status().isOk());

        // Validate the County in the database
        List<County> countyList = countyRepository.findAll();
        assertThat(countyList).hasSize(databaseSizeBeforeUpdate);
        County testCounty = countyList.get(countyList.size() - 1);
        assertThat(testCounty.getCountyId()).isEqualTo(UPDATED_COUNTY_ID);
        assertThat(testCounty.getCountyCode()).isEqualTo(UPDATED_COUNTY_CODE);
        assertThat(testCounty.getCountyName()).isEqualTo(UPDATED_COUNTY_NAME);
        assertThat(testCounty.getCountyEnglishName()).isEqualTo(UPDATED_COUNTY_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void putNonExistingCounty() throws Exception {
        int databaseSizeBeforeUpdate = countyRepository.findAll().size();
        county.setId(count.incrementAndGet());

        // Create the County
        CountyDTO countyDTO = countyMapper.toDto(county);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCountyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, countyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the County in the database
        List<County> countyList = countyRepository.findAll();
        assertThat(countyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCounty() throws Exception {
        int databaseSizeBeforeUpdate = countyRepository.findAll().size();
        county.setId(count.incrementAndGet());

        // Create the County
        CountyDTO countyDTO = countyMapper.toDto(county);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the County in the database
        List<County> countyList = countyRepository.findAll();
        assertThat(countyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCounty() throws Exception {
        int databaseSizeBeforeUpdate = countyRepository.findAll().size();
        county.setId(count.incrementAndGet());

        // Create the County
        CountyDTO countyDTO = countyMapper.toDto(county);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the County in the database
        List<County> countyList = countyRepository.findAll();
        assertThat(countyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCountyWithPatch() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        int databaseSizeBeforeUpdate = countyRepository.findAll().size();

        // Update the county using partial update
        County partialUpdatedCounty = new County();
        partialUpdatedCounty.setId(county.getId());

        partialUpdatedCounty.countyId(UPDATED_COUNTY_ID).countyCode(UPDATED_COUNTY_CODE);

        restCountyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCounty.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCounty))
            )
            .andExpect(status().isOk());

        // Validate the County in the database
        List<County> countyList = countyRepository.findAll();
        assertThat(countyList).hasSize(databaseSizeBeforeUpdate);
        County testCounty = countyList.get(countyList.size() - 1);
        assertThat(testCounty.getCountyId()).isEqualTo(UPDATED_COUNTY_ID);
        assertThat(testCounty.getCountyCode()).isEqualTo(UPDATED_COUNTY_CODE);
        assertThat(testCounty.getCountyName()).isEqualTo(DEFAULT_COUNTY_NAME);
        assertThat(testCounty.getCountyEnglishName()).isEqualTo(DEFAULT_COUNTY_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void fullUpdateCountyWithPatch() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        int databaseSizeBeforeUpdate = countyRepository.findAll().size();

        // Update the county using partial update
        County partialUpdatedCounty = new County();
        partialUpdatedCounty.setId(county.getId());

        partialUpdatedCounty
            .countyId(UPDATED_COUNTY_ID)
            .countyCode(UPDATED_COUNTY_CODE)
            .countyName(UPDATED_COUNTY_NAME)
            .countyEnglishName(UPDATED_COUNTY_ENGLISH_NAME);

        restCountyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCounty.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCounty))
            )
            .andExpect(status().isOk());

        // Validate the County in the database
        List<County> countyList = countyRepository.findAll();
        assertThat(countyList).hasSize(databaseSizeBeforeUpdate);
        County testCounty = countyList.get(countyList.size() - 1);
        assertThat(testCounty.getCountyId()).isEqualTo(UPDATED_COUNTY_ID);
        assertThat(testCounty.getCountyCode()).isEqualTo(UPDATED_COUNTY_CODE);
        assertThat(testCounty.getCountyName()).isEqualTo(UPDATED_COUNTY_NAME);
        assertThat(testCounty.getCountyEnglishName()).isEqualTo(UPDATED_COUNTY_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingCounty() throws Exception {
        int databaseSizeBeforeUpdate = countyRepository.findAll().size();
        county.setId(count.incrementAndGet());

        // Create the County
        CountyDTO countyDTO = countyMapper.toDto(county);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCountyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, countyDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(countyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the County in the database
        List<County> countyList = countyRepository.findAll();
        assertThat(countyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCounty() throws Exception {
        int databaseSizeBeforeUpdate = countyRepository.findAll().size();
        county.setId(count.incrementAndGet());

        // Create the County
        CountyDTO countyDTO = countyMapper.toDto(county);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(countyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the County in the database
        List<County> countyList = countyRepository.findAll();
        assertThat(countyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCounty() throws Exception {
        int databaseSizeBeforeUpdate = countyRepository.findAll().size();
        county.setId(count.incrementAndGet());

        // Create the County
        CountyDTO countyDTO = countyMapper.toDto(county);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountyMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(countyDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the County in the database
        List<County> countyList = countyRepository.findAll();
        assertThat(countyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCounty() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        int databaseSizeBeforeDelete = countyRepository.findAll().size();

        // Delete the county
        restCountyMockMvc
            .perform(delete(ENTITY_API_URL_ID, county.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<County> countyList = countyRepository.findAll();
        assertThat(countyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
