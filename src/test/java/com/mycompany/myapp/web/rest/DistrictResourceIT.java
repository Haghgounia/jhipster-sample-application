package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.County;
import com.mycompany.myapp.domain.District;
import com.mycompany.myapp.repository.DistrictRepository;
import com.mycompany.myapp.service.criteria.DistrictCriteria;
import com.mycompany.myapp.service.dto.DistrictDTO;
import com.mycompany.myapp.service.mapper.DistrictMapper;
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
 * Integration tests for the {@link DistrictResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DistrictResourceIT {

    private static final Integer DEFAULT_DISTRICT_ID = 1;
    private static final Integer UPDATED_DISTRICT_ID = 2;
    private static final Integer SMALLER_DISTRICT_ID = 1 - 1;

    private static final String DEFAULT_DISTRICT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_DISTRICT_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DISTRICT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DISTRICT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DISTRICT_ENGLISH_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DISTRICT_ENGLISH_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/districts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private DistrictMapper districtMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDistrictMockMvc;

    private District district;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static District createEntity(EntityManager em) {
        District district = new District()
            .districtId(DEFAULT_DISTRICT_ID)
            .districtCode(DEFAULT_DISTRICT_CODE)
            .districtName(DEFAULT_DISTRICT_NAME)
            .districtEnglishName(DEFAULT_DISTRICT_ENGLISH_NAME);
        return district;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static District createUpdatedEntity(EntityManager em) {
        District district = new District()
            .districtId(UPDATED_DISTRICT_ID)
            .districtCode(UPDATED_DISTRICT_CODE)
            .districtName(UPDATED_DISTRICT_NAME)
            .districtEnglishName(UPDATED_DISTRICT_ENGLISH_NAME);
        return district;
    }

    @BeforeEach
    public void initTest() {
        district = createEntity(em);
    }

    @Test
    @Transactional
    void createDistrict() throws Exception {
        int databaseSizeBeforeCreate = districtRepository.findAll().size();
        // Create the District
        DistrictDTO districtDTO = districtMapper.toDto(district);
        restDistrictMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(districtDTO)))
            .andExpect(status().isCreated());

        // Validate the District in the database
        List<District> districtList = districtRepository.findAll();
        assertThat(districtList).hasSize(databaseSizeBeforeCreate + 1);
        District testDistrict = districtList.get(districtList.size() - 1);
        assertThat(testDistrict.getDistrictId()).isEqualTo(DEFAULT_DISTRICT_ID);
        assertThat(testDistrict.getDistrictCode()).isEqualTo(DEFAULT_DISTRICT_CODE);
        assertThat(testDistrict.getDistrictName()).isEqualTo(DEFAULT_DISTRICT_NAME);
        assertThat(testDistrict.getDistrictEnglishName()).isEqualTo(DEFAULT_DISTRICT_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void createDistrictWithExistingId() throws Exception {
        // Create the District with an existing ID
        district.setId(1L);
        DistrictDTO districtDTO = districtMapper.toDto(district);

        int databaseSizeBeforeCreate = districtRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDistrictMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(districtDTO)))
            .andExpect(status().isBadRequest());

        // Validate the District in the database
        List<District> districtList = districtRepository.findAll();
        assertThat(districtList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDistrictIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = districtRepository.findAll().size();
        // set the field null
        district.setDistrictId(null);

        // Create the District, which fails.
        DistrictDTO districtDTO = districtMapper.toDto(district);

        restDistrictMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(districtDTO)))
            .andExpect(status().isBadRequest());

        List<District> districtList = districtRepository.findAll();
        assertThat(districtList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDistrictCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = districtRepository.findAll().size();
        // set the field null
        district.setDistrictCode(null);

        // Create the District, which fails.
        DistrictDTO districtDTO = districtMapper.toDto(district);

        restDistrictMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(districtDTO)))
            .andExpect(status().isBadRequest());

        List<District> districtList = districtRepository.findAll();
        assertThat(districtList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDistricts() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        // Get all the districtList
        restDistrictMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(district.getId().intValue())))
            .andExpect(jsonPath("$.[*].districtId").value(hasItem(DEFAULT_DISTRICT_ID)))
            .andExpect(jsonPath("$.[*].districtCode").value(hasItem(DEFAULT_DISTRICT_CODE)))
            .andExpect(jsonPath("$.[*].districtName").value(hasItem(DEFAULT_DISTRICT_NAME)))
            .andExpect(jsonPath("$.[*].districtEnglishName").value(hasItem(DEFAULT_DISTRICT_ENGLISH_NAME)));
    }

    @Test
    @Transactional
    void getDistrict() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        // Get the district
        restDistrictMockMvc
            .perform(get(ENTITY_API_URL_ID, district.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(district.getId().intValue()))
            .andExpect(jsonPath("$.districtId").value(DEFAULT_DISTRICT_ID))
            .andExpect(jsonPath("$.districtCode").value(DEFAULT_DISTRICT_CODE))
            .andExpect(jsonPath("$.districtName").value(DEFAULT_DISTRICT_NAME))
            .andExpect(jsonPath("$.districtEnglishName").value(DEFAULT_DISTRICT_ENGLISH_NAME));
    }

    @Test
    @Transactional
    void getDistrictsByIdFiltering() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        Long id = district.getId();

        defaultDistrictShouldBeFound("id.equals=" + id);
        defaultDistrictShouldNotBeFound("id.notEquals=" + id);

        defaultDistrictShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDistrictShouldNotBeFound("id.greaterThan=" + id);

        defaultDistrictShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDistrictShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDistrictsByDistrictIdIsEqualToSomething() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        // Get all the districtList where districtId equals to DEFAULT_DISTRICT_ID
        defaultDistrictShouldBeFound("districtId.equals=" + DEFAULT_DISTRICT_ID);

        // Get all the districtList where districtId equals to UPDATED_DISTRICT_ID
        defaultDistrictShouldNotBeFound("districtId.equals=" + UPDATED_DISTRICT_ID);
    }

    @Test
    @Transactional
    void getAllDistrictsByDistrictIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        // Get all the districtList where districtId not equals to DEFAULT_DISTRICT_ID
        defaultDistrictShouldNotBeFound("districtId.notEquals=" + DEFAULT_DISTRICT_ID);

        // Get all the districtList where districtId not equals to UPDATED_DISTRICT_ID
        defaultDistrictShouldBeFound("districtId.notEquals=" + UPDATED_DISTRICT_ID);
    }

    @Test
    @Transactional
    void getAllDistrictsByDistrictIdIsInShouldWork() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        // Get all the districtList where districtId in DEFAULT_DISTRICT_ID or UPDATED_DISTRICT_ID
        defaultDistrictShouldBeFound("districtId.in=" + DEFAULT_DISTRICT_ID + "," + UPDATED_DISTRICT_ID);

        // Get all the districtList where districtId equals to UPDATED_DISTRICT_ID
        defaultDistrictShouldNotBeFound("districtId.in=" + UPDATED_DISTRICT_ID);
    }

    @Test
    @Transactional
    void getAllDistrictsByDistrictIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        // Get all the districtList where districtId is not null
        defaultDistrictShouldBeFound("districtId.specified=true");

        // Get all the districtList where districtId is null
        defaultDistrictShouldNotBeFound("districtId.specified=false");
    }

    @Test
    @Transactional
    void getAllDistrictsByDistrictIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        // Get all the districtList where districtId is greater than or equal to DEFAULT_DISTRICT_ID
        defaultDistrictShouldBeFound("districtId.greaterThanOrEqual=" + DEFAULT_DISTRICT_ID);

        // Get all the districtList where districtId is greater than or equal to UPDATED_DISTRICT_ID
        defaultDistrictShouldNotBeFound("districtId.greaterThanOrEqual=" + UPDATED_DISTRICT_ID);
    }

    @Test
    @Transactional
    void getAllDistrictsByDistrictIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        // Get all the districtList where districtId is less than or equal to DEFAULT_DISTRICT_ID
        defaultDistrictShouldBeFound("districtId.lessThanOrEqual=" + DEFAULT_DISTRICT_ID);

        // Get all the districtList where districtId is less than or equal to SMALLER_DISTRICT_ID
        defaultDistrictShouldNotBeFound("districtId.lessThanOrEqual=" + SMALLER_DISTRICT_ID);
    }

    @Test
    @Transactional
    void getAllDistrictsByDistrictIdIsLessThanSomething() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        // Get all the districtList where districtId is less than DEFAULT_DISTRICT_ID
        defaultDistrictShouldNotBeFound("districtId.lessThan=" + DEFAULT_DISTRICT_ID);

        // Get all the districtList where districtId is less than UPDATED_DISTRICT_ID
        defaultDistrictShouldBeFound("districtId.lessThan=" + UPDATED_DISTRICT_ID);
    }

    @Test
    @Transactional
    void getAllDistrictsByDistrictIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        // Get all the districtList where districtId is greater than DEFAULT_DISTRICT_ID
        defaultDistrictShouldNotBeFound("districtId.greaterThan=" + DEFAULT_DISTRICT_ID);

        // Get all the districtList where districtId is greater than SMALLER_DISTRICT_ID
        defaultDistrictShouldBeFound("districtId.greaterThan=" + SMALLER_DISTRICT_ID);
    }

    @Test
    @Transactional
    void getAllDistrictsByDistrictCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        // Get all the districtList where districtCode equals to DEFAULT_DISTRICT_CODE
        defaultDistrictShouldBeFound("districtCode.equals=" + DEFAULT_DISTRICT_CODE);

        // Get all the districtList where districtCode equals to UPDATED_DISTRICT_CODE
        defaultDistrictShouldNotBeFound("districtCode.equals=" + UPDATED_DISTRICT_CODE);
    }

    @Test
    @Transactional
    void getAllDistrictsByDistrictCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        // Get all the districtList where districtCode not equals to DEFAULT_DISTRICT_CODE
        defaultDistrictShouldNotBeFound("districtCode.notEquals=" + DEFAULT_DISTRICT_CODE);

        // Get all the districtList where districtCode not equals to UPDATED_DISTRICT_CODE
        defaultDistrictShouldBeFound("districtCode.notEquals=" + UPDATED_DISTRICT_CODE);
    }

    @Test
    @Transactional
    void getAllDistrictsByDistrictCodeIsInShouldWork() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        // Get all the districtList where districtCode in DEFAULT_DISTRICT_CODE or UPDATED_DISTRICT_CODE
        defaultDistrictShouldBeFound("districtCode.in=" + DEFAULT_DISTRICT_CODE + "," + UPDATED_DISTRICT_CODE);

        // Get all the districtList where districtCode equals to UPDATED_DISTRICT_CODE
        defaultDistrictShouldNotBeFound("districtCode.in=" + UPDATED_DISTRICT_CODE);
    }

    @Test
    @Transactional
    void getAllDistrictsByDistrictCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        // Get all the districtList where districtCode is not null
        defaultDistrictShouldBeFound("districtCode.specified=true");

        // Get all the districtList where districtCode is null
        defaultDistrictShouldNotBeFound("districtCode.specified=false");
    }

    @Test
    @Transactional
    void getAllDistrictsByDistrictCodeContainsSomething() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        // Get all the districtList where districtCode contains DEFAULT_DISTRICT_CODE
        defaultDistrictShouldBeFound("districtCode.contains=" + DEFAULT_DISTRICT_CODE);

        // Get all the districtList where districtCode contains UPDATED_DISTRICT_CODE
        defaultDistrictShouldNotBeFound("districtCode.contains=" + UPDATED_DISTRICT_CODE);
    }

    @Test
    @Transactional
    void getAllDistrictsByDistrictCodeNotContainsSomething() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        // Get all the districtList where districtCode does not contain DEFAULT_DISTRICT_CODE
        defaultDistrictShouldNotBeFound("districtCode.doesNotContain=" + DEFAULT_DISTRICT_CODE);

        // Get all the districtList where districtCode does not contain UPDATED_DISTRICT_CODE
        defaultDistrictShouldBeFound("districtCode.doesNotContain=" + UPDATED_DISTRICT_CODE);
    }

    @Test
    @Transactional
    void getAllDistrictsByDistrictNameIsEqualToSomething() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        // Get all the districtList where districtName equals to DEFAULT_DISTRICT_NAME
        defaultDistrictShouldBeFound("districtName.equals=" + DEFAULT_DISTRICT_NAME);

        // Get all the districtList where districtName equals to UPDATED_DISTRICT_NAME
        defaultDistrictShouldNotBeFound("districtName.equals=" + UPDATED_DISTRICT_NAME);
    }

    @Test
    @Transactional
    void getAllDistrictsByDistrictNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        // Get all the districtList where districtName not equals to DEFAULT_DISTRICT_NAME
        defaultDistrictShouldNotBeFound("districtName.notEquals=" + DEFAULT_DISTRICT_NAME);

        // Get all the districtList where districtName not equals to UPDATED_DISTRICT_NAME
        defaultDistrictShouldBeFound("districtName.notEquals=" + UPDATED_DISTRICT_NAME);
    }

    @Test
    @Transactional
    void getAllDistrictsByDistrictNameIsInShouldWork() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        // Get all the districtList where districtName in DEFAULT_DISTRICT_NAME or UPDATED_DISTRICT_NAME
        defaultDistrictShouldBeFound("districtName.in=" + DEFAULT_DISTRICT_NAME + "," + UPDATED_DISTRICT_NAME);

        // Get all the districtList where districtName equals to UPDATED_DISTRICT_NAME
        defaultDistrictShouldNotBeFound("districtName.in=" + UPDATED_DISTRICT_NAME);
    }

    @Test
    @Transactional
    void getAllDistrictsByDistrictNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        // Get all the districtList where districtName is not null
        defaultDistrictShouldBeFound("districtName.specified=true");

        // Get all the districtList where districtName is null
        defaultDistrictShouldNotBeFound("districtName.specified=false");
    }

    @Test
    @Transactional
    void getAllDistrictsByDistrictNameContainsSomething() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        // Get all the districtList where districtName contains DEFAULT_DISTRICT_NAME
        defaultDistrictShouldBeFound("districtName.contains=" + DEFAULT_DISTRICT_NAME);

        // Get all the districtList where districtName contains UPDATED_DISTRICT_NAME
        defaultDistrictShouldNotBeFound("districtName.contains=" + UPDATED_DISTRICT_NAME);
    }

    @Test
    @Transactional
    void getAllDistrictsByDistrictNameNotContainsSomething() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        // Get all the districtList where districtName does not contain DEFAULT_DISTRICT_NAME
        defaultDistrictShouldNotBeFound("districtName.doesNotContain=" + DEFAULT_DISTRICT_NAME);

        // Get all the districtList where districtName does not contain UPDATED_DISTRICT_NAME
        defaultDistrictShouldBeFound("districtName.doesNotContain=" + UPDATED_DISTRICT_NAME);
    }

    @Test
    @Transactional
    void getAllDistrictsByDistrictEnglishNameIsEqualToSomething() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        // Get all the districtList where districtEnglishName equals to DEFAULT_DISTRICT_ENGLISH_NAME
        defaultDistrictShouldBeFound("districtEnglishName.equals=" + DEFAULT_DISTRICT_ENGLISH_NAME);

        // Get all the districtList where districtEnglishName equals to UPDATED_DISTRICT_ENGLISH_NAME
        defaultDistrictShouldNotBeFound("districtEnglishName.equals=" + UPDATED_DISTRICT_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void getAllDistrictsByDistrictEnglishNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        // Get all the districtList where districtEnglishName not equals to DEFAULT_DISTRICT_ENGLISH_NAME
        defaultDistrictShouldNotBeFound("districtEnglishName.notEquals=" + DEFAULT_DISTRICT_ENGLISH_NAME);

        // Get all the districtList where districtEnglishName not equals to UPDATED_DISTRICT_ENGLISH_NAME
        defaultDistrictShouldBeFound("districtEnglishName.notEquals=" + UPDATED_DISTRICT_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void getAllDistrictsByDistrictEnglishNameIsInShouldWork() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        // Get all the districtList where districtEnglishName in DEFAULT_DISTRICT_ENGLISH_NAME or UPDATED_DISTRICT_ENGLISH_NAME
        defaultDistrictShouldBeFound("districtEnglishName.in=" + DEFAULT_DISTRICT_ENGLISH_NAME + "," + UPDATED_DISTRICT_ENGLISH_NAME);

        // Get all the districtList where districtEnglishName equals to UPDATED_DISTRICT_ENGLISH_NAME
        defaultDistrictShouldNotBeFound("districtEnglishName.in=" + UPDATED_DISTRICT_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void getAllDistrictsByDistrictEnglishNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        // Get all the districtList where districtEnglishName is not null
        defaultDistrictShouldBeFound("districtEnglishName.specified=true");

        // Get all the districtList where districtEnglishName is null
        defaultDistrictShouldNotBeFound("districtEnglishName.specified=false");
    }

    @Test
    @Transactional
    void getAllDistrictsByDistrictEnglishNameContainsSomething() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        // Get all the districtList where districtEnglishName contains DEFAULT_DISTRICT_ENGLISH_NAME
        defaultDistrictShouldBeFound("districtEnglishName.contains=" + DEFAULT_DISTRICT_ENGLISH_NAME);

        // Get all the districtList where districtEnglishName contains UPDATED_DISTRICT_ENGLISH_NAME
        defaultDistrictShouldNotBeFound("districtEnglishName.contains=" + UPDATED_DISTRICT_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void getAllDistrictsByDistrictEnglishNameNotContainsSomething() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        // Get all the districtList where districtEnglishName does not contain DEFAULT_DISTRICT_ENGLISH_NAME
        defaultDistrictShouldNotBeFound("districtEnglishName.doesNotContain=" + DEFAULT_DISTRICT_ENGLISH_NAME);

        // Get all the districtList where districtEnglishName does not contain UPDATED_DISTRICT_ENGLISH_NAME
        defaultDistrictShouldBeFound("districtEnglishName.doesNotContain=" + UPDATED_DISTRICT_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void getAllDistrictsByCountyIsEqualToSomething() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);
        County county;
        if (TestUtil.findAll(em, County.class).isEmpty()) {
            county = CountyResourceIT.createEntity(em);
            em.persist(county);
            em.flush();
        } else {
            county = TestUtil.findAll(em, County.class).get(0);
        }
        em.persist(county);
        em.flush();
        district.setCounty(county);
        districtRepository.saveAndFlush(district);
        Long countyId = county.getId();

        // Get all the districtList where county equals to countyId
        defaultDistrictShouldBeFound("countyId.equals=" + countyId);

        // Get all the districtList where county equals to (countyId + 1)
        defaultDistrictShouldNotBeFound("countyId.equals=" + (countyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDistrictShouldBeFound(String filter) throws Exception {
        restDistrictMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(district.getId().intValue())))
            .andExpect(jsonPath("$.[*].districtId").value(hasItem(DEFAULT_DISTRICT_ID)))
            .andExpect(jsonPath("$.[*].districtCode").value(hasItem(DEFAULT_DISTRICT_CODE)))
            .andExpect(jsonPath("$.[*].districtName").value(hasItem(DEFAULT_DISTRICT_NAME)))
            .andExpect(jsonPath("$.[*].districtEnglishName").value(hasItem(DEFAULT_DISTRICT_ENGLISH_NAME)));

        // Check, that the count call also returns 1
        restDistrictMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDistrictShouldNotBeFound(String filter) throws Exception {
        restDistrictMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDistrictMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDistrict() throws Exception {
        // Get the district
        restDistrictMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDistrict() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        int databaseSizeBeforeUpdate = districtRepository.findAll().size();

        // Update the district
        District updatedDistrict = districtRepository.findById(district.getId()).get();
        // Disconnect from session so that the updates on updatedDistrict are not directly saved in db
        em.detach(updatedDistrict);
        updatedDistrict
            .districtId(UPDATED_DISTRICT_ID)
            .districtCode(UPDATED_DISTRICT_CODE)
            .districtName(UPDATED_DISTRICT_NAME)
            .districtEnglishName(UPDATED_DISTRICT_ENGLISH_NAME);
        DistrictDTO districtDTO = districtMapper.toDto(updatedDistrict);

        restDistrictMockMvc
            .perform(
                put(ENTITY_API_URL_ID, districtDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(districtDTO))
            )
            .andExpect(status().isOk());

        // Validate the District in the database
        List<District> districtList = districtRepository.findAll();
        assertThat(districtList).hasSize(databaseSizeBeforeUpdate);
        District testDistrict = districtList.get(districtList.size() - 1);
        assertThat(testDistrict.getDistrictId()).isEqualTo(UPDATED_DISTRICT_ID);
        assertThat(testDistrict.getDistrictCode()).isEqualTo(UPDATED_DISTRICT_CODE);
        assertThat(testDistrict.getDistrictName()).isEqualTo(UPDATED_DISTRICT_NAME);
        assertThat(testDistrict.getDistrictEnglishName()).isEqualTo(UPDATED_DISTRICT_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void putNonExistingDistrict() throws Exception {
        int databaseSizeBeforeUpdate = districtRepository.findAll().size();
        district.setId(count.incrementAndGet());

        // Create the District
        DistrictDTO districtDTO = districtMapper.toDto(district);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDistrictMockMvc
            .perform(
                put(ENTITY_API_URL_ID, districtDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(districtDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the District in the database
        List<District> districtList = districtRepository.findAll();
        assertThat(districtList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDistrict() throws Exception {
        int databaseSizeBeforeUpdate = districtRepository.findAll().size();
        district.setId(count.incrementAndGet());

        // Create the District
        DistrictDTO districtDTO = districtMapper.toDto(district);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDistrictMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(districtDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the District in the database
        List<District> districtList = districtRepository.findAll();
        assertThat(districtList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDistrict() throws Exception {
        int databaseSizeBeforeUpdate = districtRepository.findAll().size();
        district.setId(count.incrementAndGet());

        // Create the District
        DistrictDTO districtDTO = districtMapper.toDto(district);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDistrictMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(districtDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the District in the database
        List<District> districtList = districtRepository.findAll();
        assertThat(districtList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDistrictWithPatch() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        int databaseSizeBeforeUpdate = districtRepository.findAll().size();

        // Update the district using partial update
        District partialUpdatedDistrict = new District();
        partialUpdatedDistrict.setId(district.getId());

        partialUpdatedDistrict.districtId(UPDATED_DISTRICT_ID).districtCode(UPDATED_DISTRICT_CODE).districtName(UPDATED_DISTRICT_NAME);

        restDistrictMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDistrict.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDistrict))
            )
            .andExpect(status().isOk());

        // Validate the District in the database
        List<District> districtList = districtRepository.findAll();
        assertThat(districtList).hasSize(databaseSizeBeforeUpdate);
        District testDistrict = districtList.get(districtList.size() - 1);
        assertThat(testDistrict.getDistrictId()).isEqualTo(UPDATED_DISTRICT_ID);
        assertThat(testDistrict.getDistrictCode()).isEqualTo(UPDATED_DISTRICT_CODE);
        assertThat(testDistrict.getDistrictName()).isEqualTo(UPDATED_DISTRICT_NAME);
        assertThat(testDistrict.getDistrictEnglishName()).isEqualTo(DEFAULT_DISTRICT_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void fullUpdateDistrictWithPatch() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        int databaseSizeBeforeUpdate = districtRepository.findAll().size();

        // Update the district using partial update
        District partialUpdatedDistrict = new District();
        partialUpdatedDistrict.setId(district.getId());

        partialUpdatedDistrict
            .districtId(UPDATED_DISTRICT_ID)
            .districtCode(UPDATED_DISTRICT_CODE)
            .districtName(UPDATED_DISTRICT_NAME)
            .districtEnglishName(UPDATED_DISTRICT_ENGLISH_NAME);

        restDistrictMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDistrict.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDistrict))
            )
            .andExpect(status().isOk());

        // Validate the District in the database
        List<District> districtList = districtRepository.findAll();
        assertThat(districtList).hasSize(databaseSizeBeforeUpdate);
        District testDistrict = districtList.get(districtList.size() - 1);
        assertThat(testDistrict.getDistrictId()).isEqualTo(UPDATED_DISTRICT_ID);
        assertThat(testDistrict.getDistrictCode()).isEqualTo(UPDATED_DISTRICT_CODE);
        assertThat(testDistrict.getDistrictName()).isEqualTo(UPDATED_DISTRICT_NAME);
        assertThat(testDistrict.getDistrictEnglishName()).isEqualTo(UPDATED_DISTRICT_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingDistrict() throws Exception {
        int databaseSizeBeforeUpdate = districtRepository.findAll().size();
        district.setId(count.incrementAndGet());

        // Create the District
        DistrictDTO districtDTO = districtMapper.toDto(district);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDistrictMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, districtDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(districtDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the District in the database
        List<District> districtList = districtRepository.findAll();
        assertThat(districtList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDistrict() throws Exception {
        int databaseSizeBeforeUpdate = districtRepository.findAll().size();
        district.setId(count.incrementAndGet());

        // Create the District
        DistrictDTO districtDTO = districtMapper.toDto(district);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDistrictMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(districtDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the District in the database
        List<District> districtList = districtRepository.findAll();
        assertThat(districtList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDistrict() throws Exception {
        int databaseSizeBeforeUpdate = districtRepository.findAll().size();
        district.setId(count.incrementAndGet());

        // Create the District
        DistrictDTO districtDTO = districtMapper.toDto(district);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDistrictMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(districtDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the District in the database
        List<District> districtList = districtRepository.findAll();
        assertThat(districtList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDistrict() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        int databaseSizeBeforeDelete = districtRepository.findAll().size();

        // Delete the district
        restDistrictMockMvc
            .perform(delete(ENTITY_API_URL_ID, district.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<District> districtList = districtRepository.findAll();
        assertThat(districtList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
