package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Continent;
import com.mycompany.myapp.repository.ContinentRepository;
import com.mycompany.myapp.service.criteria.ContinentCriteria;
import com.mycompany.myapp.service.dto.ContinentDTO;
import com.mycompany.myapp.service.mapper.ContinentMapper;
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
 * Integration tests for the {@link ContinentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ContinentResourceIT {

    private static final Integer DEFAULT_CONTINENT_ID = 1;
    private static final Integer UPDATED_CONTINENT_ID = 2;
    private static final Integer SMALLER_CONTINENT_ID = 1 - 1;

    private static final String DEFAULT_CONTINENT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CONTINENT_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTINENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CONTINENT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTINENT_ENGLISH_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CONTINENT_ENGLISH_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/continents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContinentRepository continentRepository;

    @Autowired
    private ContinentMapper continentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContinentMockMvc;

    private Continent continent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Continent createEntity(EntityManager em) {
        Continent continent = new Continent()
            .continentId(DEFAULT_CONTINENT_ID)
            .continentCode(DEFAULT_CONTINENT_CODE)
            .continentName(DEFAULT_CONTINENT_NAME)
            .continentEnglishName(DEFAULT_CONTINENT_ENGLISH_NAME);
        return continent;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Continent createUpdatedEntity(EntityManager em) {
        Continent continent = new Continent()
            .continentId(UPDATED_CONTINENT_ID)
            .continentCode(UPDATED_CONTINENT_CODE)
            .continentName(UPDATED_CONTINENT_NAME)
            .continentEnglishName(UPDATED_CONTINENT_ENGLISH_NAME);
        return continent;
    }

    @BeforeEach
    public void initTest() {
        continent = createEntity(em);
    }

    @Test
    @Transactional
    void createContinent() throws Exception {
        int databaseSizeBeforeCreate = continentRepository.findAll().size();
        // Create the Continent
        ContinentDTO continentDTO = continentMapper.toDto(continent);
        restContinentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(continentDTO)))
            .andExpect(status().isCreated());

        // Validate the Continent in the database
        List<Continent> continentList = continentRepository.findAll();
        assertThat(continentList).hasSize(databaseSizeBeforeCreate + 1);
        Continent testContinent = continentList.get(continentList.size() - 1);
        assertThat(testContinent.getContinentId()).isEqualTo(DEFAULT_CONTINENT_ID);
        assertThat(testContinent.getContinentCode()).isEqualTo(DEFAULT_CONTINENT_CODE);
        assertThat(testContinent.getContinentName()).isEqualTo(DEFAULT_CONTINENT_NAME);
        assertThat(testContinent.getContinentEnglishName()).isEqualTo(DEFAULT_CONTINENT_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void createContinentWithExistingId() throws Exception {
        // Create the Continent with an existing ID
        continent.setId(1L);
        ContinentDTO continentDTO = continentMapper.toDto(continent);

        int databaseSizeBeforeCreate = continentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContinentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(continentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Continent in the database
        List<Continent> continentList = continentRepository.findAll();
        assertThat(continentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkContinentIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = continentRepository.findAll().size();
        // set the field null
        continent.setContinentId(null);

        // Create the Continent, which fails.
        ContinentDTO continentDTO = continentMapper.toDto(continent);

        restContinentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(continentDTO)))
            .andExpect(status().isBadRequest());

        List<Continent> continentList = continentRepository.findAll();
        assertThat(continentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContinentCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = continentRepository.findAll().size();
        // set the field null
        continent.setContinentCode(null);

        // Create the Continent, which fails.
        ContinentDTO continentDTO = continentMapper.toDto(continent);

        restContinentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(continentDTO)))
            .andExpect(status().isBadRequest());

        List<Continent> continentList = continentRepository.findAll();
        assertThat(continentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContinentNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = continentRepository.findAll().size();
        // set the field null
        continent.setContinentName(null);

        // Create the Continent, which fails.
        ContinentDTO continentDTO = continentMapper.toDto(continent);

        restContinentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(continentDTO)))
            .andExpect(status().isBadRequest());

        List<Continent> continentList = continentRepository.findAll();
        assertThat(continentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContinents() throws Exception {
        // Initialize the database
        continentRepository.saveAndFlush(continent);

        // Get all the continentList
        restContinentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(continent.getId().intValue())))
            .andExpect(jsonPath("$.[*].continentId").value(hasItem(DEFAULT_CONTINENT_ID)))
            .andExpect(jsonPath("$.[*].continentCode").value(hasItem(DEFAULT_CONTINENT_CODE)))
            .andExpect(jsonPath("$.[*].continentName").value(hasItem(DEFAULT_CONTINENT_NAME)))
            .andExpect(jsonPath("$.[*].continentEnglishName").value(hasItem(DEFAULT_CONTINENT_ENGLISH_NAME)));
    }

    @Test
    @Transactional
    void getContinent() throws Exception {
        // Initialize the database
        continentRepository.saveAndFlush(continent);

        // Get the continent
        restContinentMockMvc
            .perform(get(ENTITY_API_URL_ID, continent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(continent.getId().intValue()))
            .andExpect(jsonPath("$.continentId").value(DEFAULT_CONTINENT_ID))
            .andExpect(jsonPath("$.continentCode").value(DEFAULT_CONTINENT_CODE))
            .andExpect(jsonPath("$.continentName").value(DEFAULT_CONTINENT_NAME))
            .andExpect(jsonPath("$.continentEnglishName").value(DEFAULT_CONTINENT_ENGLISH_NAME));
    }

    @Test
    @Transactional
    void getContinentsByIdFiltering() throws Exception {
        // Initialize the database
        continentRepository.saveAndFlush(continent);

        Long id = continent.getId();

        defaultContinentShouldBeFound("id.equals=" + id);
        defaultContinentShouldNotBeFound("id.notEquals=" + id);

        defaultContinentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultContinentShouldNotBeFound("id.greaterThan=" + id);

        defaultContinentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultContinentShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllContinentsByContinentIdIsEqualToSomething() throws Exception {
        // Initialize the database
        continentRepository.saveAndFlush(continent);

        // Get all the continentList where continentId equals to DEFAULT_CONTINENT_ID
        defaultContinentShouldBeFound("continentId.equals=" + DEFAULT_CONTINENT_ID);

        // Get all the continentList where continentId equals to UPDATED_CONTINENT_ID
        defaultContinentShouldNotBeFound("continentId.equals=" + UPDATED_CONTINENT_ID);
    }

    @Test
    @Transactional
    void getAllContinentsByContinentIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        continentRepository.saveAndFlush(continent);

        // Get all the continentList where continentId not equals to DEFAULT_CONTINENT_ID
        defaultContinentShouldNotBeFound("continentId.notEquals=" + DEFAULT_CONTINENT_ID);

        // Get all the continentList where continentId not equals to UPDATED_CONTINENT_ID
        defaultContinentShouldBeFound("continentId.notEquals=" + UPDATED_CONTINENT_ID);
    }

    @Test
    @Transactional
    void getAllContinentsByContinentIdIsInShouldWork() throws Exception {
        // Initialize the database
        continentRepository.saveAndFlush(continent);

        // Get all the continentList where continentId in DEFAULT_CONTINENT_ID or UPDATED_CONTINENT_ID
        defaultContinentShouldBeFound("continentId.in=" + DEFAULT_CONTINENT_ID + "," + UPDATED_CONTINENT_ID);

        // Get all the continentList where continentId equals to UPDATED_CONTINENT_ID
        defaultContinentShouldNotBeFound("continentId.in=" + UPDATED_CONTINENT_ID);
    }

    @Test
    @Transactional
    void getAllContinentsByContinentIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        continentRepository.saveAndFlush(continent);

        // Get all the continentList where continentId is not null
        defaultContinentShouldBeFound("continentId.specified=true");

        // Get all the continentList where continentId is null
        defaultContinentShouldNotBeFound("continentId.specified=false");
    }

    @Test
    @Transactional
    void getAllContinentsByContinentIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        continentRepository.saveAndFlush(continent);

        // Get all the continentList where continentId is greater than or equal to DEFAULT_CONTINENT_ID
        defaultContinentShouldBeFound("continentId.greaterThanOrEqual=" + DEFAULT_CONTINENT_ID);

        // Get all the continentList where continentId is greater than or equal to UPDATED_CONTINENT_ID
        defaultContinentShouldNotBeFound("continentId.greaterThanOrEqual=" + UPDATED_CONTINENT_ID);
    }

    @Test
    @Transactional
    void getAllContinentsByContinentIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        continentRepository.saveAndFlush(continent);

        // Get all the continentList where continentId is less than or equal to DEFAULT_CONTINENT_ID
        defaultContinentShouldBeFound("continentId.lessThanOrEqual=" + DEFAULT_CONTINENT_ID);

        // Get all the continentList where continentId is less than or equal to SMALLER_CONTINENT_ID
        defaultContinentShouldNotBeFound("continentId.lessThanOrEqual=" + SMALLER_CONTINENT_ID);
    }

    @Test
    @Transactional
    void getAllContinentsByContinentIdIsLessThanSomething() throws Exception {
        // Initialize the database
        continentRepository.saveAndFlush(continent);

        // Get all the continentList where continentId is less than DEFAULT_CONTINENT_ID
        defaultContinentShouldNotBeFound("continentId.lessThan=" + DEFAULT_CONTINENT_ID);

        // Get all the continentList where continentId is less than UPDATED_CONTINENT_ID
        defaultContinentShouldBeFound("continentId.lessThan=" + UPDATED_CONTINENT_ID);
    }

    @Test
    @Transactional
    void getAllContinentsByContinentIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        continentRepository.saveAndFlush(continent);

        // Get all the continentList where continentId is greater than DEFAULT_CONTINENT_ID
        defaultContinentShouldNotBeFound("continentId.greaterThan=" + DEFAULT_CONTINENT_ID);

        // Get all the continentList where continentId is greater than SMALLER_CONTINENT_ID
        defaultContinentShouldBeFound("continentId.greaterThan=" + SMALLER_CONTINENT_ID);
    }

    @Test
    @Transactional
    void getAllContinentsByContinentCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        continentRepository.saveAndFlush(continent);

        // Get all the continentList where continentCode equals to DEFAULT_CONTINENT_CODE
        defaultContinentShouldBeFound("continentCode.equals=" + DEFAULT_CONTINENT_CODE);

        // Get all the continentList where continentCode equals to UPDATED_CONTINENT_CODE
        defaultContinentShouldNotBeFound("continentCode.equals=" + UPDATED_CONTINENT_CODE);
    }

    @Test
    @Transactional
    void getAllContinentsByContinentCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        continentRepository.saveAndFlush(continent);

        // Get all the continentList where continentCode not equals to DEFAULT_CONTINENT_CODE
        defaultContinentShouldNotBeFound("continentCode.notEquals=" + DEFAULT_CONTINENT_CODE);

        // Get all the continentList where continentCode not equals to UPDATED_CONTINENT_CODE
        defaultContinentShouldBeFound("continentCode.notEquals=" + UPDATED_CONTINENT_CODE);
    }

    @Test
    @Transactional
    void getAllContinentsByContinentCodeIsInShouldWork() throws Exception {
        // Initialize the database
        continentRepository.saveAndFlush(continent);

        // Get all the continentList where continentCode in DEFAULT_CONTINENT_CODE or UPDATED_CONTINENT_CODE
        defaultContinentShouldBeFound("continentCode.in=" + DEFAULT_CONTINENT_CODE + "," + UPDATED_CONTINENT_CODE);

        // Get all the continentList where continentCode equals to UPDATED_CONTINENT_CODE
        defaultContinentShouldNotBeFound("continentCode.in=" + UPDATED_CONTINENT_CODE);
    }

    @Test
    @Transactional
    void getAllContinentsByContinentCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        continentRepository.saveAndFlush(continent);

        // Get all the continentList where continentCode is not null
        defaultContinentShouldBeFound("continentCode.specified=true");

        // Get all the continentList where continentCode is null
        defaultContinentShouldNotBeFound("continentCode.specified=false");
    }

    @Test
    @Transactional
    void getAllContinentsByContinentCodeContainsSomething() throws Exception {
        // Initialize the database
        continentRepository.saveAndFlush(continent);

        // Get all the continentList where continentCode contains DEFAULT_CONTINENT_CODE
        defaultContinentShouldBeFound("continentCode.contains=" + DEFAULT_CONTINENT_CODE);

        // Get all the continentList where continentCode contains UPDATED_CONTINENT_CODE
        defaultContinentShouldNotBeFound("continentCode.contains=" + UPDATED_CONTINENT_CODE);
    }

    @Test
    @Transactional
    void getAllContinentsByContinentCodeNotContainsSomething() throws Exception {
        // Initialize the database
        continentRepository.saveAndFlush(continent);

        // Get all the continentList where continentCode does not contain DEFAULT_CONTINENT_CODE
        defaultContinentShouldNotBeFound("continentCode.doesNotContain=" + DEFAULT_CONTINENT_CODE);

        // Get all the continentList where continentCode does not contain UPDATED_CONTINENT_CODE
        defaultContinentShouldBeFound("continentCode.doesNotContain=" + UPDATED_CONTINENT_CODE);
    }

    @Test
    @Transactional
    void getAllContinentsByContinentNameIsEqualToSomething() throws Exception {
        // Initialize the database
        continentRepository.saveAndFlush(continent);

        // Get all the continentList where continentName equals to DEFAULT_CONTINENT_NAME
        defaultContinentShouldBeFound("continentName.equals=" + DEFAULT_CONTINENT_NAME);

        // Get all the continentList where continentName equals to UPDATED_CONTINENT_NAME
        defaultContinentShouldNotBeFound("continentName.equals=" + UPDATED_CONTINENT_NAME);
    }

    @Test
    @Transactional
    void getAllContinentsByContinentNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        continentRepository.saveAndFlush(continent);

        // Get all the continentList where continentName not equals to DEFAULT_CONTINENT_NAME
        defaultContinentShouldNotBeFound("continentName.notEquals=" + DEFAULT_CONTINENT_NAME);

        // Get all the continentList where continentName not equals to UPDATED_CONTINENT_NAME
        defaultContinentShouldBeFound("continentName.notEquals=" + UPDATED_CONTINENT_NAME);
    }

    @Test
    @Transactional
    void getAllContinentsByContinentNameIsInShouldWork() throws Exception {
        // Initialize the database
        continentRepository.saveAndFlush(continent);

        // Get all the continentList where continentName in DEFAULT_CONTINENT_NAME or UPDATED_CONTINENT_NAME
        defaultContinentShouldBeFound("continentName.in=" + DEFAULT_CONTINENT_NAME + "," + UPDATED_CONTINENT_NAME);

        // Get all the continentList where continentName equals to UPDATED_CONTINENT_NAME
        defaultContinentShouldNotBeFound("continentName.in=" + UPDATED_CONTINENT_NAME);
    }

    @Test
    @Transactional
    void getAllContinentsByContinentNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        continentRepository.saveAndFlush(continent);

        // Get all the continentList where continentName is not null
        defaultContinentShouldBeFound("continentName.specified=true");

        // Get all the continentList where continentName is null
        defaultContinentShouldNotBeFound("continentName.specified=false");
    }

    @Test
    @Transactional
    void getAllContinentsByContinentNameContainsSomething() throws Exception {
        // Initialize the database
        continentRepository.saveAndFlush(continent);

        // Get all the continentList where continentName contains DEFAULT_CONTINENT_NAME
        defaultContinentShouldBeFound("continentName.contains=" + DEFAULT_CONTINENT_NAME);

        // Get all the continentList where continentName contains UPDATED_CONTINENT_NAME
        defaultContinentShouldNotBeFound("continentName.contains=" + UPDATED_CONTINENT_NAME);
    }

    @Test
    @Transactional
    void getAllContinentsByContinentNameNotContainsSomething() throws Exception {
        // Initialize the database
        continentRepository.saveAndFlush(continent);

        // Get all the continentList where continentName does not contain DEFAULT_CONTINENT_NAME
        defaultContinentShouldNotBeFound("continentName.doesNotContain=" + DEFAULT_CONTINENT_NAME);

        // Get all the continentList where continentName does not contain UPDATED_CONTINENT_NAME
        defaultContinentShouldBeFound("continentName.doesNotContain=" + UPDATED_CONTINENT_NAME);
    }

    @Test
    @Transactional
    void getAllContinentsByContinentEnglishNameIsEqualToSomething() throws Exception {
        // Initialize the database
        continentRepository.saveAndFlush(continent);

        // Get all the continentList where continentEnglishName equals to DEFAULT_CONTINENT_ENGLISH_NAME
        defaultContinentShouldBeFound("continentEnglishName.equals=" + DEFAULT_CONTINENT_ENGLISH_NAME);

        // Get all the continentList where continentEnglishName equals to UPDATED_CONTINENT_ENGLISH_NAME
        defaultContinentShouldNotBeFound("continentEnglishName.equals=" + UPDATED_CONTINENT_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void getAllContinentsByContinentEnglishNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        continentRepository.saveAndFlush(continent);

        // Get all the continentList where continentEnglishName not equals to DEFAULT_CONTINENT_ENGLISH_NAME
        defaultContinentShouldNotBeFound("continentEnglishName.notEquals=" + DEFAULT_CONTINENT_ENGLISH_NAME);

        // Get all the continentList where continentEnglishName not equals to UPDATED_CONTINENT_ENGLISH_NAME
        defaultContinentShouldBeFound("continentEnglishName.notEquals=" + UPDATED_CONTINENT_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void getAllContinentsByContinentEnglishNameIsInShouldWork() throws Exception {
        // Initialize the database
        continentRepository.saveAndFlush(continent);

        // Get all the continentList where continentEnglishName in DEFAULT_CONTINENT_ENGLISH_NAME or UPDATED_CONTINENT_ENGLISH_NAME
        defaultContinentShouldBeFound("continentEnglishName.in=" + DEFAULT_CONTINENT_ENGLISH_NAME + "," + UPDATED_CONTINENT_ENGLISH_NAME);

        // Get all the continentList where continentEnglishName equals to UPDATED_CONTINENT_ENGLISH_NAME
        defaultContinentShouldNotBeFound("continentEnglishName.in=" + UPDATED_CONTINENT_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void getAllContinentsByContinentEnglishNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        continentRepository.saveAndFlush(continent);

        // Get all the continentList where continentEnglishName is not null
        defaultContinentShouldBeFound("continentEnglishName.specified=true");

        // Get all the continentList where continentEnglishName is null
        defaultContinentShouldNotBeFound("continentEnglishName.specified=false");
    }

    @Test
    @Transactional
    void getAllContinentsByContinentEnglishNameContainsSomething() throws Exception {
        // Initialize the database
        continentRepository.saveAndFlush(continent);

        // Get all the continentList where continentEnglishName contains DEFAULT_CONTINENT_ENGLISH_NAME
        defaultContinentShouldBeFound("continentEnglishName.contains=" + DEFAULT_CONTINENT_ENGLISH_NAME);

        // Get all the continentList where continentEnglishName contains UPDATED_CONTINENT_ENGLISH_NAME
        defaultContinentShouldNotBeFound("continentEnglishName.contains=" + UPDATED_CONTINENT_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void getAllContinentsByContinentEnglishNameNotContainsSomething() throws Exception {
        // Initialize the database
        continentRepository.saveAndFlush(continent);

        // Get all the continentList where continentEnglishName does not contain DEFAULT_CONTINENT_ENGLISH_NAME
        defaultContinentShouldNotBeFound("continentEnglishName.doesNotContain=" + DEFAULT_CONTINENT_ENGLISH_NAME);

        // Get all the continentList where continentEnglishName does not contain UPDATED_CONTINENT_ENGLISH_NAME
        defaultContinentShouldBeFound("continentEnglishName.doesNotContain=" + UPDATED_CONTINENT_ENGLISH_NAME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultContinentShouldBeFound(String filter) throws Exception {
        restContinentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(continent.getId().intValue())))
            .andExpect(jsonPath("$.[*].continentId").value(hasItem(DEFAULT_CONTINENT_ID)))
            .andExpect(jsonPath("$.[*].continentCode").value(hasItem(DEFAULT_CONTINENT_CODE)))
            .andExpect(jsonPath("$.[*].continentName").value(hasItem(DEFAULT_CONTINENT_NAME)))
            .andExpect(jsonPath("$.[*].continentEnglishName").value(hasItem(DEFAULT_CONTINENT_ENGLISH_NAME)));

        // Check, that the count call also returns 1
        restContinentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultContinentShouldNotBeFound(String filter) throws Exception {
        restContinentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restContinentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingContinent() throws Exception {
        // Get the continent
        restContinentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewContinent() throws Exception {
        // Initialize the database
        continentRepository.saveAndFlush(continent);

        int databaseSizeBeforeUpdate = continentRepository.findAll().size();

        // Update the continent
        Continent updatedContinent = continentRepository.findById(continent.getId()).get();
        // Disconnect from session so that the updates on updatedContinent are not directly saved in db
        em.detach(updatedContinent);
        updatedContinent
            .continentId(UPDATED_CONTINENT_ID)
            .continentCode(UPDATED_CONTINENT_CODE)
            .continentName(UPDATED_CONTINENT_NAME)
            .continentEnglishName(UPDATED_CONTINENT_ENGLISH_NAME);
        ContinentDTO continentDTO = continentMapper.toDto(updatedContinent);

        restContinentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, continentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(continentDTO))
            )
            .andExpect(status().isOk());

        // Validate the Continent in the database
        List<Continent> continentList = continentRepository.findAll();
        assertThat(continentList).hasSize(databaseSizeBeforeUpdate);
        Continent testContinent = continentList.get(continentList.size() - 1);
        assertThat(testContinent.getContinentId()).isEqualTo(UPDATED_CONTINENT_ID);
        assertThat(testContinent.getContinentCode()).isEqualTo(UPDATED_CONTINENT_CODE);
        assertThat(testContinent.getContinentName()).isEqualTo(UPDATED_CONTINENT_NAME);
        assertThat(testContinent.getContinentEnglishName()).isEqualTo(UPDATED_CONTINENT_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void putNonExistingContinent() throws Exception {
        int databaseSizeBeforeUpdate = continentRepository.findAll().size();
        continent.setId(count.incrementAndGet());

        // Create the Continent
        ContinentDTO continentDTO = continentMapper.toDto(continent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContinentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, continentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(continentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Continent in the database
        List<Continent> continentList = continentRepository.findAll();
        assertThat(continentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContinent() throws Exception {
        int databaseSizeBeforeUpdate = continentRepository.findAll().size();
        continent.setId(count.incrementAndGet());

        // Create the Continent
        ContinentDTO continentDTO = continentMapper.toDto(continent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContinentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(continentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Continent in the database
        List<Continent> continentList = continentRepository.findAll();
        assertThat(continentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContinent() throws Exception {
        int databaseSizeBeforeUpdate = continentRepository.findAll().size();
        continent.setId(count.incrementAndGet());

        // Create the Continent
        ContinentDTO continentDTO = continentMapper.toDto(continent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContinentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(continentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Continent in the database
        List<Continent> continentList = continentRepository.findAll();
        assertThat(continentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContinentWithPatch() throws Exception {
        // Initialize the database
        continentRepository.saveAndFlush(continent);

        int databaseSizeBeforeUpdate = continentRepository.findAll().size();

        // Update the continent using partial update
        Continent partialUpdatedContinent = new Continent();
        partialUpdatedContinent.setId(continent.getId());

        partialUpdatedContinent
            .continentId(UPDATED_CONTINENT_ID)
            .continentCode(UPDATED_CONTINENT_CODE)
            .continentName(UPDATED_CONTINENT_NAME)
            .continentEnglishName(UPDATED_CONTINENT_ENGLISH_NAME);

        restContinentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContinent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContinent))
            )
            .andExpect(status().isOk());

        // Validate the Continent in the database
        List<Continent> continentList = continentRepository.findAll();
        assertThat(continentList).hasSize(databaseSizeBeforeUpdate);
        Continent testContinent = continentList.get(continentList.size() - 1);
        assertThat(testContinent.getContinentId()).isEqualTo(UPDATED_CONTINENT_ID);
        assertThat(testContinent.getContinentCode()).isEqualTo(UPDATED_CONTINENT_CODE);
        assertThat(testContinent.getContinentName()).isEqualTo(UPDATED_CONTINENT_NAME);
        assertThat(testContinent.getContinentEnglishName()).isEqualTo(UPDATED_CONTINENT_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void fullUpdateContinentWithPatch() throws Exception {
        // Initialize the database
        continentRepository.saveAndFlush(continent);

        int databaseSizeBeforeUpdate = continentRepository.findAll().size();

        // Update the continent using partial update
        Continent partialUpdatedContinent = new Continent();
        partialUpdatedContinent.setId(continent.getId());

        partialUpdatedContinent
            .continentId(UPDATED_CONTINENT_ID)
            .continentCode(UPDATED_CONTINENT_CODE)
            .continentName(UPDATED_CONTINENT_NAME)
            .continentEnglishName(UPDATED_CONTINENT_ENGLISH_NAME);

        restContinentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContinent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContinent))
            )
            .andExpect(status().isOk());

        // Validate the Continent in the database
        List<Continent> continentList = continentRepository.findAll();
        assertThat(continentList).hasSize(databaseSizeBeforeUpdate);
        Continent testContinent = continentList.get(continentList.size() - 1);
        assertThat(testContinent.getContinentId()).isEqualTo(UPDATED_CONTINENT_ID);
        assertThat(testContinent.getContinentCode()).isEqualTo(UPDATED_CONTINENT_CODE);
        assertThat(testContinent.getContinentName()).isEqualTo(UPDATED_CONTINENT_NAME);
        assertThat(testContinent.getContinentEnglishName()).isEqualTo(UPDATED_CONTINENT_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingContinent() throws Exception {
        int databaseSizeBeforeUpdate = continentRepository.findAll().size();
        continent.setId(count.incrementAndGet());

        // Create the Continent
        ContinentDTO continentDTO = continentMapper.toDto(continent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContinentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, continentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(continentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Continent in the database
        List<Continent> continentList = continentRepository.findAll();
        assertThat(continentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContinent() throws Exception {
        int databaseSizeBeforeUpdate = continentRepository.findAll().size();
        continent.setId(count.incrementAndGet());

        // Create the Continent
        ContinentDTO continentDTO = continentMapper.toDto(continent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContinentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(continentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Continent in the database
        List<Continent> continentList = continentRepository.findAll();
        assertThat(continentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContinent() throws Exception {
        int databaseSizeBeforeUpdate = continentRepository.findAll().size();
        continent.setId(count.incrementAndGet());

        // Create the Continent
        ContinentDTO continentDTO = continentMapper.toDto(continent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContinentMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(continentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Continent in the database
        List<Continent> continentList = continentRepository.findAll();
        assertThat(continentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContinent() throws Exception {
        // Initialize the database
        continentRepository.saveAndFlush(continent);

        int databaseSizeBeforeDelete = continentRepository.findAll().size();

        // Delete the continent
        restContinentMockMvc
            .perform(delete(ENTITY_API_URL_ID, continent.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Continent> continentList = continentRepository.findAll();
        assertThat(continentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
