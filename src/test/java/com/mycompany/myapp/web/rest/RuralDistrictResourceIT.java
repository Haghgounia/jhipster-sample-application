package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.District;
import com.mycompany.myapp.domain.RuralDistrict;
import com.mycompany.myapp.repository.RuralDistrictRepository;
import com.mycompany.myapp.service.criteria.RuralDistrictCriteria;
import com.mycompany.myapp.service.dto.RuralDistrictDTO;
import com.mycompany.myapp.service.mapper.RuralDistrictMapper;
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
 * Integration tests for the {@link RuralDistrictResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RuralDistrictResourceIT {

    private static final Integer DEFAULT_RURAL_DISTRICT_ID = 1;
    private static final Integer UPDATED_RURAL_DISTRICT_ID = 2;
    private static final Integer SMALLER_RURAL_DISTRICT_ID = 1 - 1;

    private static final String DEFAULT_RURAL_DISTRICT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_RURAL_DISTRICT_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_RURAL_DISTRICT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_RURAL_DISTRICT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_RURAL_DISTRICT_ENGLISH_NAME = "AAAAAAAAAA";
    private static final String UPDATED_RURAL_DISTRICT_ENGLISH_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/rural-districts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RuralDistrictRepository ruralDistrictRepository;

    @Autowired
    private RuralDistrictMapper ruralDistrictMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRuralDistrictMockMvc;

    private RuralDistrict ruralDistrict;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RuralDistrict createEntity(EntityManager em) {
        RuralDistrict ruralDistrict = new RuralDistrict()
            .ruralDistrictId(DEFAULT_RURAL_DISTRICT_ID)
            .ruralDistrictCode(DEFAULT_RURAL_DISTRICT_CODE)
            .ruralDistrictName(DEFAULT_RURAL_DISTRICT_NAME)
            .ruralDistrictEnglishName(DEFAULT_RURAL_DISTRICT_ENGLISH_NAME);
        return ruralDistrict;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RuralDistrict createUpdatedEntity(EntityManager em) {
        RuralDistrict ruralDistrict = new RuralDistrict()
            .ruralDistrictId(UPDATED_RURAL_DISTRICT_ID)
            .ruralDistrictCode(UPDATED_RURAL_DISTRICT_CODE)
            .ruralDistrictName(UPDATED_RURAL_DISTRICT_NAME)
            .ruralDistrictEnglishName(UPDATED_RURAL_DISTRICT_ENGLISH_NAME);
        return ruralDistrict;
    }

    @BeforeEach
    public void initTest() {
        ruralDistrict = createEntity(em);
    }

    @Test
    @Transactional
    void createRuralDistrict() throws Exception {
        int databaseSizeBeforeCreate = ruralDistrictRepository.findAll().size();
        // Create the RuralDistrict
        RuralDistrictDTO ruralDistrictDTO = ruralDistrictMapper.toDto(ruralDistrict);
        restRuralDistrictMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ruralDistrictDTO))
            )
            .andExpect(status().isCreated());

        // Validate the RuralDistrict in the database
        List<RuralDistrict> ruralDistrictList = ruralDistrictRepository.findAll();
        assertThat(ruralDistrictList).hasSize(databaseSizeBeforeCreate + 1);
        RuralDistrict testRuralDistrict = ruralDistrictList.get(ruralDistrictList.size() - 1);
        assertThat(testRuralDistrict.getRuralDistrictId()).isEqualTo(DEFAULT_RURAL_DISTRICT_ID);
        assertThat(testRuralDistrict.getRuralDistrictCode()).isEqualTo(DEFAULT_RURAL_DISTRICT_CODE);
        assertThat(testRuralDistrict.getRuralDistrictName()).isEqualTo(DEFAULT_RURAL_DISTRICT_NAME);
        assertThat(testRuralDistrict.getRuralDistrictEnglishName()).isEqualTo(DEFAULT_RURAL_DISTRICT_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void createRuralDistrictWithExistingId() throws Exception {
        // Create the RuralDistrict with an existing ID
        ruralDistrict.setId(1L);
        RuralDistrictDTO ruralDistrictDTO = ruralDistrictMapper.toDto(ruralDistrict);

        int databaseSizeBeforeCreate = ruralDistrictRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRuralDistrictMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ruralDistrictDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RuralDistrict in the database
        List<RuralDistrict> ruralDistrictList = ruralDistrictRepository.findAll();
        assertThat(ruralDistrictList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRuralDistrictIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = ruralDistrictRepository.findAll().size();
        // set the field null
        ruralDistrict.setRuralDistrictId(null);

        // Create the RuralDistrict, which fails.
        RuralDistrictDTO ruralDistrictDTO = ruralDistrictMapper.toDto(ruralDistrict);

        restRuralDistrictMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ruralDistrictDTO))
            )
            .andExpect(status().isBadRequest());

        List<RuralDistrict> ruralDistrictList = ruralDistrictRepository.findAll();
        assertThat(ruralDistrictList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRuralDistrictCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = ruralDistrictRepository.findAll().size();
        // set the field null
        ruralDistrict.setRuralDistrictCode(null);

        // Create the RuralDistrict, which fails.
        RuralDistrictDTO ruralDistrictDTO = ruralDistrictMapper.toDto(ruralDistrict);

        restRuralDistrictMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ruralDistrictDTO))
            )
            .andExpect(status().isBadRequest());

        List<RuralDistrict> ruralDistrictList = ruralDistrictRepository.findAll();
        assertThat(ruralDistrictList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRuralDistricts() throws Exception {
        // Initialize the database
        ruralDistrictRepository.saveAndFlush(ruralDistrict);

        // Get all the ruralDistrictList
        restRuralDistrictMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ruralDistrict.getId().intValue())))
            .andExpect(jsonPath("$.[*].ruralDistrictId").value(hasItem(DEFAULT_RURAL_DISTRICT_ID)))
            .andExpect(jsonPath("$.[*].ruralDistrictCode").value(hasItem(DEFAULT_RURAL_DISTRICT_CODE)))
            .andExpect(jsonPath("$.[*].ruralDistrictName").value(hasItem(DEFAULT_RURAL_DISTRICT_NAME)))
            .andExpect(jsonPath("$.[*].ruralDistrictEnglishName").value(hasItem(DEFAULT_RURAL_DISTRICT_ENGLISH_NAME)));
    }

    @Test
    @Transactional
    void getRuralDistrict() throws Exception {
        // Initialize the database
        ruralDistrictRepository.saveAndFlush(ruralDistrict);

        // Get the ruralDistrict
        restRuralDistrictMockMvc
            .perform(get(ENTITY_API_URL_ID, ruralDistrict.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ruralDistrict.getId().intValue()))
            .andExpect(jsonPath("$.ruralDistrictId").value(DEFAULT_RURAL_DISTRICT_ID))
            .andExpect(jsonPath("$.ruralDistrictCode").value(DEFAULT_RURAL_DISTRICT_CODE))
            .andExpect(jsonPath("$.ruralDistrictName").value(DEFAULT_RURAL_DISTRICT_NAME))
            .andExpect(jsonPath("$.ruralDistrictEnglishName").value(DEFAULT_RURAL_DISTRICT_ENGLISH_NAME));
    }

    @Test
    @Transactional
    void getRuralDistrictsByIdFiltering() throws Exception {
        // Initialize the database
        ruralDistrictRepository.saveAndFlush(ruralDistrict);

        Long id = ruralDistrict.getId();

        defaultRuralDistrictShouldBeFound("id.equals=" + id);
        defaultRuralDistrictShouldNotBeFound("id.notEquals=" + id);

        defaultRuralDistrictShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRuralDistrictShouldNotBeFound("id.greaterThan=" + id);

        defaultRuralDistrictShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRuralDistrictShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRuralDistrictsByRuralDistrictIdIsEqualToSomething() throws Exception {
        // Initialize the database
        ruralDistrictRepository.saveAndFlush(ruralDistrict);

        // Get all the ruralDistrictList where ruralDistrictId equals to DEFAULT_RURAL_DISTRICT_ID
        defaultRuralDistrictShouldBeFound("ruralDistrictId.equals=" + DEFAULT_RURAL_DISTRICT_ID);

        // Get all the ruralDistrictList where ruralDistrictId equals to UPDATED_RURAL_DISTRICT_ID
        defaultRuralDistrictShouldNotBeFound("ruralDistrictId.equals=" + UPDATED_RURAL_DISTRICT_ID);
    }

    @Test
    @Transactional
    void getAllRuralDistrictsByRuralDistrictIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ruralDistrictRepository.saveAndFlush(ruralDistrict);

        // Get all the ruralDistrictList where ruralDistrictId not equals to DEFAULT_RURAL_DISTRICT_ID
        defaultRuralDistrictShouldNotBeFound("ruralDistrictId.notEquals=" + DEFAULT_RURAL_DISTRICT_ID);

        // Get all the ruralDistrictList where ruralDistrictId not equals to UPDATED_RURAL_DISTRICT_ID
        defaultRuralDistrictShouldBeFound("ruralDistrictId.notEquals=" + UPDATED_RURAL_DISTRICT_ID);
    }

    @Test
    @Transactional
    void getAllRuralDistrictsByRuralDistrictIdIsInShouldWork() throws Exception {
        // Initialize the database
        ruralDistrictRepository.saveAndFlush(ruralDistrict);

        // Get all the ruralDistrictList where ruralDistrictId in DEFAULT_RURAL_DISTRICT_ID or UPDATED_RURAL_DISTRICT_ID
        defaultRuralDistrictShouldBeFound("ruralDistrictId.in=" + DEFAULT_RURAL_DISTRICT_ID + "," + UPDATED_RURAL_DISTRICT_ID);

        // Get all the ruralDistrictList where ruralDistrictId equals to UPDATED_RURAL_DISTRICT_ID
        defaultRuralDistrictShouldNotBeFound("ruralDistrictId.in=" + UPDATED_RURAL_DISTRICT_ID);
    }

    @Test
    @Transactional
    void getAllRuralDistrictsByRuralDistrictIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        ruralDistrictRepository.saveAndFlush(ruralDistrict);

        // Get all the ruralDistrictList where ruralDistrictId is not null
        defaultRuralDistrictShouldBeFound("ruralDistrictId.specified=true");

        // Get all the ruralDistrictList where ruralDistrictId is null
        defaultRuralDistrictShouldNotBeFound("ruralDistrictId.specified=false");
    }

    @Test
    @Transactional
    void getAllRuralDistrictsByRuralDistrictIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ruralDistrictRepository.saveAndFlush(ruralDistrict);

        // Get all the ruralDistrictList where ruralDistrictId is greater than or equal to DEFAULT_RURAL_DISTRICT_ID
        defaultRuralDistrictShouldBeFound("ruralDistrictId.greaterThanOrEqual=" + DEFAULT_RURAL_DISTRICT_ID);

        // Get all the ruralDistrictList where ruralDistrictId is greater than or equal to UPDATED_RURAL_DISTRICT_ID
        defaultRuralDistrictShouldNotBeFound("ruralDistrictId.greaterThanOrEqual=" + UPDATED_RURAL_DISTRICT_ID);
    }

    @Test
    @Transactional
    void getAllRuralDistrictsByRuralDistrictIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ruralDistrictRepository.saveAndFlush(ruralDistrict);

        // Get all the ruralDistrictList where ruralDistrictId is less than or equal to DEFAULT_RURAL_DISTRICT_ID
        defaultRuralDistrictShouldBeFound("ruralDistrictId.lessThanOrEqual=" + DEFAULT_RURAL_DISTRICT_ID);

        // Get all the ruralDistrictList where ruralDistrictId is less than or equal to SMALLER_RURAL_DISTRICT_ID
        defaultRuralDistrictShouldNotBeFound("ruralDistrictId.lessThanOrEqual=" + SMALLER_RURAL_DISTRICT_ID);
    }

    @Test
    @Transactional
    void getAllRuralDistrictsByRuralDistrictIdIsLessThanSomething() throws Exception {
        // Initialize the database
        ruralDistrictRepository.saveAndFlush(ruralDistrict);

        // Get all the ruralDistrictList where ruralDistrictId is less than DEFAULT_RURAL_DISTRICT_ID
        defaultRuralDistrictShouldNotBeFound("ruralDistrictId.lessThan=" + DEFAULT_RURAL_DISTRICT_ID);

        // Get all the ruralDistrictList where ruralDistrictId is less than UPDATED_RURAL_DISTRICT_ID
        defaultRuralDistrictShouldBeFound("ruralDistrictId.lessThan=" + UPDATED_RURAL_DISTRICT_ID);
    }

    @Test
    @Transactional
    void getAllRuralDistrictsByRuralDistrictIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ruralDistrictRepository.saveAndFlush(ruralDistrict);

        // Get all the ruralDistrictList where ruralDistrictId is greater than DEFAULT_RURAL_DISTRICT_ID
        defaultRuralDistrictShouldNotBeFound("ruralDistrictId.greaterThan=" + DEFAULT_RURAL_DISTRICT_ID);

        // Get all the ruralDistrictList where ruralDistrictId is greater than SMALLER_RURAL_DISTRICT_ID
        defaultRuralDistrictShouldBeFound("ruralDistrictId.greaterThan=" + SMALLER_RURAL_DISTRICT_ID);
    }

    @Test
    @Transactional
    void getAllRuralDistrictsByRuralDistrictCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        ruralDistrictRepository.saveAndFlush(ruralDistrict);

        // Get all the ruralDistrictList where ruralDistrictCode equals to DEFAULT_RURAL_DISTRICT_CODE
        defaultRuralDistrictShouldBeFound("ruralDistrictCode.equals=" + DEFAULT_RURAL_DISTRICT_CODE);

        // Get all the ruralDistrictList where ruralDistrictCode equals to UPDATED_RURAL_DISTRICT_CODE
        defaultRuralDistrictShouldNotBeFound("ruralDistrictCode.equals=" + UPDATED_RURAL_DISTRICT_CODE);
    }

    @Test
    @Transactional
    void getAllRuralDistrictsByRuralDistrictCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ruralDistrictRepository.saveAndFlush(ruralDistrict);

        // Get all the ruralDistrictList where ruralDistrictCode not equals to DEFAULT_RURAL_DISTRICT_CODE
        defaultRuralDistrictShouldNotBeFound("ruralDistrictCode.notEquals=" + DEFAULT_RURAL_DISTRICT_CODE);

        // Get all the ruralDistrictList where ruralDistrictCode not equals to UPDATED_RURAL_DISTRICT_CODE
        defaultRuralDistrictShouldBeFound("ruralDistrictCode.notEquals=" + UPDATED_RURAL_DISTRICT_CODE);
    }

    @Test
    @Transactional
    void getAllRuralDistrictsByRuralDistrictCodeIsInShouldWork() throws Exception {
        // Initialize the database
        ruralDistrictRepository.saveAndFlush(ruralDistrict);

        // Get all the ruralDistrictList where ruralDistrictCode in DEFAULT_RURAL_DISTRICT_CODE or UPDATED_RURAL_DISTRICT_CODE
        defaultRuralDistrictShouldBeFound("ruralDistrictCode.in=" + DEFAULT_RURAL_DISTRICT_CODE + "," + UPDATED_RURAL_DISTRICT_CODE);

        // Get all the ruralDistrictList where ruralDistrictCode equals to UPDATED_RURAL_DISTRICT_CODE
        defaultRuralDistrictShouldNotBeFound("ruralDistrictCode.in=" + UPDATED_RURAL_DISTRICT_CODE);
    }

    @Test
    @Transactional
    void getAllRuralDistrictsByRuralDistrictCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        ruralDistrictRepository.saveAndFlush(ruralDistrict);

        // Get all the ruralDistrictList where ruralDistrictCode is not null
        defaultRuralDistrictShouldBeFound("ruralDistrictCode.specified=true");

        // Get all the ruralDistrictList where ruralDistrictCode is null
        defaultRuralDistrictShouldNotBeFound("ruralDistrictCode.specified=false");
    }

    @Test
    @Transactional
    void getAllRuralDistrictsByRuralDistrictCodeContainsSomething() throws Exception {
        // Initialize the database
        ruralDistrictRepository.saveAndFlush(ruralDistrict);

        // Get all the ruralDistrictList where ruralDistrictCode contains DEFAULT_RURAL_DISTRICT_CODE
        defaultRuralDistrictShouldBeFound("ruralDistrictCode.contains=" + DEFAULT_RURAL_DISTRICT_CODE);

        // Get all the ruralDistrictList where ruralDistrictCode contains UPDATED_RURAL_DISTRICT_CODE
        defaultRuralDistrictShouldNotBeFound("ruralDistrictCode.contains=" + UPDATED_RURAL_DISTRICT_CODE);
    }

    @Test
    @Transactional
    void getAllRuralDistrictsByRuralDistrictCodeNotContainsSomething() throws Exception {
        // Initialize the database
        ruralDistrictRepository.saveAndFlush(ruralDistrict);

        // Get all the ruralDistrictList where ruralDistrictCode does not contain DEFAULT_RURAL_DISTRICT_CODE
        defaultRuralDistrictShouldNotBeFound("ruralDistrictCode.doesNotContain=" + DEFAULT_RURAL_DISTRICT_CODE);

        // Get all the ruralDistrictList where ruralDistrictCode does not contain UPDATED_RURAL_DISTRICT_CODE
        defaultRuralDistrictShouldBeFound("ruralDistrictCode.doesNotContain=" + UPDATED_RURAL_DISTRICT_CODE);
    }

    @Test
    @Transactional
    void getAllRuralDistrictsByRuralDistrictNameIsEqualToSomething() throws Exception {
        // Initialize the database
        ruralDistrictRepository.saveAndFlush(ruralDistrict);

        // Get all the ruralDistrictList where ruralDistrictName equals to DEFAULT_RURAL_DISTRICT_NAME
        defaultRuralDistrictShouldBeFound("ruralDistrictName.equals=" + DEFAULT_RURAL_DISTRICT_NAME);

        // Get all the ruralDistrictList where ruralDistrictName equals to UPDATED_RURAL_DISTRICT_NAME
        defaultRuralDistrictShouldNotBeFound("ruralDistrictName.equals=" + UPDATED_RURAL_DISTRICT_NAME);
    }

    @Test
    @Transactional
    void getAllRuralDistrictsByRuralDistrictNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ruralDistrictRepository.saveAndFlush(ruralDistrict);

        // Get all the ruralDistrictList where ruralDistrictName not equals to DEFAULT_RURAL_DISTRICT_NAME
        defaultRuralDistrictShouldNotBeFound("ruralDistrictName.notEquals=" + DEFAULT_RURAL_DISTRICT_NAME);

        // Get all the ruralDistrictList where ruralDistrictName not equals to UPDATED_RURAL_DISTRICT_NAME
        defaultRuralDistrictShouldBeFound("ruralDistrictName.notEquals=" + UPDATED_RURAL_DISTRICT_NAME);
    }

    @Test
    @Transactional
    void getAllRuralDistrictsByRuralDistrictNameIsInShouldWork() throws Exception {
        // Initialize the database
        ruralDistrictRepository.saveAndFlush(ruralDistrict);

        // Get all the ruralDistrictList where ruralDistrictName in DEFAULT_RURAL_DISTRICT_NAME or UPDATED_RURAL_DISTRICT_NAME
        defaultRuralDistrictShouldBeFound("ruralDistrictName.in=" + DEFAULT_RURAL_DISTRICT_NAME + "," + UPDATED_RURAL_DISTRICT_NAME);

        // Get all the ruralDistrictList where ruralDistrictName equals to UPDATED_RURAL_DISTRICT_NAME
        defaultRuralDistrictShouldNotBeFound("ruralDistrictName.in=" + UPDATED_RURAL_DISTRICT_NAME);
    }

    @Test
    @Transactional
    void getAllRuralDistrictsByRuralDistrictNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        ruralDistrictRepository.saveAndFlush(ruralDistrict);

        // Get all the ruralDistrictList where ruralDistrictName is not null
        defaultRuralDistrictShouldBeFound("ruralDistrictName.specified=true");

        // Get all the ruralDistrictList where ruralDistrictName is null
        defaultRuralDistrictShouldNotBeFound("ruralDistrictName.specified=false");
    }

    @Test
    @Transactional
    void getAllRuralDistrictsByRuralDistrictNameContainsSomething() throws Exception {
        // Initialize the database
        ruralDistrictRepository.saveAndFlush(ruralDistrict);

        // Get all the ruralDistrictList where ruralDistrictName contains DEFAULT_RURAL_DISTRICT_NAME
        defaultRuralDistrictShouldBeFound("ruralDistrictName.contains=" + DEFAULT_RURAL_DISTRICT_NAME);

        // Get all the ruralDistrictList where ruralDistrictName contains UPDATED_RURAL_DISTRICT_NAME
        defaultRuralDistrictShouldNotBeFound("ruralDistrictName.contains=" + UPDATED_RURAL_DISTRICT_NAME);
    }

    @Test
    @Transactional
    void getAllRuralDistrictsByRuralDistrictNameNotContainsSomething() throws Exception {
        // Initialize the database
        ruralDistrictRepository.saveAndFlush(ruralDistrict);

        // Get all the ruralDistrictList where ruralDistrictName does not contain DEFAULT_RURAL_DISTRICT_NAME
        defaultRuralDistrictShouldNotBeFound("ruralDistrictName.doesNotContain=" + DEFAULT_RURAL_DISTRICT_NAME);

        // Get all the ruralDistrictList where ruralDistrictName does not contain UPDATED_RURAL_DISTRICT_NAME
        defaultRuralDistrictShouldBeFound("ruralDistrictName.doesNotContain=" + UPDATED_RURAL_DISTRICT_NAME);
    }

    @Test
    @Transactional
    void getAllRuralDistrictsByRuralDistrictEnglishNameIsEqualToSomething() throws Exception {
        // Initialize the database
        ruralDistrictRepository.saveAndFlush(ruralDistrict);

        // Get all the ruralDistrictList where ruralDistrictEnglishName equals to DEFAULT_RURAL_DISTRICT_ENGLISH_NAME
        defaultRuralDistrictShouldBeFound("ruralDistrictEnglishName.equals=" + DEFAULT_RURAL_DISTRICT_ENGLISH_NAME);

        // Get all the ruralDistrictList where ruralDistrictEnglishName equals to UPDATED_RURAL_DISTRICT_ENGLISH_NAME
        defaultRuralDistrictShouldNotBeFound("ruralDistrictEnglishName.equals=" + UPDATED_RURAL_DISTRICT_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void getAllRuralDistrictsByRuralDistrictEnglishNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ruralDistrictRepository.saveAndFlush(ruralDistrict);

        // Get all the ruralDistrictList where ruralDistrictEnglishName not equals to DEFAULT_RURAL_DISTRICT_ENGLISH_NAME
        defaultRuralDistrictShouldNotBeFound("ruralDistrictEnglishName.notEquals=" + DEFAULT_RURAL_DISTRICT_ENGLISH_NAME);

        // Get all the ruralDistrictList where ruralDistrictEnglishName not equals to UPDATED_RURAL_DISTRICT_ENGLISH_NAME
        defaultRuralDistrictShouldBeFound("ruralDistrictEnglishName.notEquals=" + UPDATED_RURAL_DISTRICT_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void getAllRuralDistrictsByRuralDistrictEnglishNameIsInShouldWork() throws Exception {
        // Initialize the database
        ruralDistrictRepository.saveAndFlush(ruralDistrict);

        // Get all the ruralDistrictList where ruralDistrictEnglishName in DEFAULT_RURAL_DISTRICT_ENGLISH_NAME or UPDATED_RURAL_DISTRICT_ENGLISH_NAME
        defaultRuralDistrictShouldBeFound(
            "ruralDistrictEnglishName.in=" + DEFAULT_RURAL_DISTRICT_ENGLISH_NAME + "," + UPDATED_RURAL_DISTRICT_ENGLISH_NAME
        );

        // Get all the ruralDistrictList where ruralDistrictEnglishName equals to UPDATED_RURAL_DISTRICT_ENGLISH_NAME
        defaultRuralDistrictShouldNotBeFound("ruralDistrictEnglishName.in=" + UPDATED_RURAL_DISTRICT_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void getAllRuralDistrictsByRuralDistrictEnglishNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        ruralDistrictRepository.saveAndFlush(ruralDistrict);

        // Get all the ruralDistrictList where ruralDistrictEnglishName is not null
        defaultRuralDistrictShouldBeFound("ruralDistrictEnglishName.specified=true");

        // Get all the ruralDistrictList where ruralDistrictEnglishName is null
        defaultRuralDistrictShouldNotBeFound("ruralDistrictEnglishName.specified=false");
    }

    @Test
    @Transactional
    void getAllRuralDistrictsByRuralDistrictEnglishNameContainsSomething() throws Exception {
        // Initialize the database
        ruralDistrictRepository.saveAndFlush(ruralDistrict);

        // Get all the ruralDistrictList where ruralDistrictEnglishName contains DEFAULT_RURAL_DISTRICT_ENGLISH_NAME
        defaultRuralDistrictShouldBeFound("ruralDistrictEnglishName.contains=" + DEFAULT_RURAL_DISTRICT_ENGLISH_NAME);

        // Get all the ruralDistrictList where ruralDistrictEnglishName contains UPDATED_RURAL_DISTRICT_ENGLISH_NAME
        defaultRuralDistrictShouldNotBeFound("ruralDistrictEnglishName.contains=" + UPDATED_RURAL_DISTRICT_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void getAllRuralDistrictsByRuralDistrictEnglishNameNotContainsSomething() throws Exception {
        // Initialize the database
        ruralDistrictRepository.saveAndFlush(ruralDistrict);

        // Get all the ruralDistrictList where ruralDistrictEnglishName does not contain DEFAULT_RURAL_DISTRICT_ENGLISH_NAME
        defaultRuralDistrictShouldNotBeFound("ruralDistrictEnglishName.doesNotContain=" + DEFAULT_RURAL_DISTRICT_ENGLISH_NAME);

        // Get all the ruralDistrictList where ruralDistrictEnglishName does not contain UPDATED_RURAL_DISTRICT_ENGLISH_NAME
        defaultRuralDistrictShouldBeFound("ruralDistrictEnglishName.doesNotContain=" + UPDATED_RURAL_DISTRICT_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void getAllRuralDistrictsByDistrictIsEqualToSomething() throws Exception {
        // Initialize the database
        ruralDistrictRepository.saveAndFlush(ruralDistrict);
        District district;
        if (TestUtil.findAll(em, District.class).isEmpty()) {
            district = DistrictResourceIT.createEntity(em);
            em.persist(district);
            em.flush();
        } else {
            district = TestUtil.findAll(em, District.class).get(0);
        }
        em.persist(district);
        em.flush();
        ruralDistrict.setDistrict(district);
        ruralDistrictRepository.saveAndFlush(ruralDistrict);
        Long districtId = district.getId();

        // Get all the ruralDistrictList where district equals to districtId
        defaultRuralDistrictShouldBeFound("districtId.equals=" + districtId);

        // Get all the ruralDistrictList where district equals to (districtId + 1)
        defaultRuralDistrictShouldNotBeFound("districtId.equals=" + (districtId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRuralDistrictShouldBeFound(String filter) throws Exception {
        restRuralDistrictMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ruralDistrict.getId().intValue())))
            .andExpect(jsonPath("$.[*].ruralDistrictId").value(hasItem(DEFAULT_RURAL_DISTRICT_ID)))
            .andExpect(jsonPath("$.[*].ruralDistrictCode").value(hasItem(DEFAULT_RURAL_DISTRICT_CODE)))
            .andExpect(jsonPath("$.[*].ruralDistrictName").value(hasItem(DEFAULT_RURAL_DISTRICT_NAME)))
            .andExpect(jsonPath("$.[*].ruralDistrictEnglishName").value(hasItem(DEFAULT_RURAL_DISTRICT_ENGLISH_NAME)));

        // Check, that the count call also returns 1
        restRuralDistrictMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRuralDistrictShouldNotBeFound(String filter) throws Exception {
        restRuralDistrictMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRuralDistrictMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRuralDistrict() throws Exception {
        // Get the ruralDistrict
        restRuralDistrictMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRuralDistrict() throws Exception {
        // Initialize the database
        ruralDistrictRepository.saveAndFlush(ruralDistrict);

        int databaseSizeBeforeUpdate = ruralDistrictRepository.findAll().size();

        // Update the ruralDistrict
        RuralDistrict updatedRuralDistrict = ruralDistrictRepository.findById(ruralDistrict.getId()).get();
        // Disconnect from session so that the updates on updatedRuralDistrict are not directly saved in db
        em.detach(updatedRuralDistrict);
        updatedRuralDistrict
            .ruralDistrictId(UPDATED_RURAL_DISTRICT_ID)
            .ruralDistrictCode(UPDATED_RURAL_DISTRICT_CODE)
            .ruralDistrictName(UPDATED_RURAL_DISTRICT_NAME)
            .ruralDistrictEnglishName(UPDATED_RURAL_DISTRICT_ENGLISH_NAME);
        RuralDistrictDTO ruralDistrictDTO = ruralDistrictMapper.toDto(updatedRuralDistrict);

        restRuralDistrictMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ruralDistrictDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ruralDistrictDTO))
            )
            .andExpect(status().isOk());

        // Validate the RuralDistrict in the database
        List<RuralDistrict> ruralDistrictList = ruralDistrictRepository.findAll();
        assertThat(ruralDistrictList).hasSize(databaseSizeBeforeUpdate);
        RuralDistrict testRuralDistrict = ruralDistrictList.get(ruralDistrictList.size() - 1);
        assertThat(testRuralDistrict.getRuralDistrictId()).isEqualTo(UPDATED_RURAL_DISTRICT_ID);
        assertThat(testRuralDistrict.getRuralDistrictCode()).isEqualTo(UPDATED_RURAL_DISTRICT_CODE);
        assertThat(testRuralDistrict.getRuralDistrictName()).isEqualTo(UPDATED_RURAL_DISTRICT_NAME);
        assertThat(testRuralDistrict.getRuralDistrictEnglishName()).isEqualTo(UPDATED_RURAL_DISTRICT_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void putNonExistingRuralDistrict() throws Exception {
        int databaseSizeBeforeUpdate = ruralDistrictRepository.findAll().size();
        ruralDistrict.setId(count.incrementAndGet());

        // Create the RuralDistrict
        RuralDistrictDTO ruralDistrictDTO = ruralDistrictMapper.toDto(ruralDistrict);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRuralDistrictMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ruralDistrictDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ruralDistrictDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RuralDistrict in the database
        List<RuralDistrict> ruralDistrictList = ruralDistrictRepository.findAll();
        assertThat(ruralDistrictList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRuralDistrict() throws Exception {
        int databaseSizeBeforeUpdate = ruralDistrictRepository.findAll().size();
        ruralDistrict.setId(count.incrementAndGet());

        // Create the RuralDistrict
        RuralDistrictDTO ruralDistrictDTO = ruralDistrictMapper.toDto(ruralDistrict);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRuralDistrictMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ruralDistrictDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RuralDistrict in the database
        List<RuralDistrict> ruralDistrictList = ruralDistrictRepository.findAll();
        assertThat(ruralDistrictList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRuralDistrict() throws Exception {
        int databaseSizeBeforeUpdate = ruralDistrictRepository.findAll().size();
        ruralDistrict.setId(count.incrementAndGet());

        // Create the RuralDistrict
        RuralDistrictDTO ruralDistrictDTO = ruralDistrictMapper.toDto(ruralDistrict);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRuralDistrictMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ruralDistrictDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RuralDistrict in the database
        List<RuralDistrict> ruralDistrictList = ruralDistrictRepository.findAll();
        assertThat(ruralDistrictList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRuralDistrictWithPatch() throws Exception {
        // Initialize the database
        ruralDistrictRepository.saveAndFlush(ruralDistrict);

        int databaseSizeBeforeUpdate = ruralDistrictRepository.findAll().size();

        // Update the ruralDistrict using partial update
        RuralDistrict partialUpdatedRuralDistrict = new RuralDistrict();
        partialUpdatedRuralDistrict.setId(ruralDistrict.getId());

        partialUpdatedRuralDistrict.ruralDistrictId(UPDATED_RURAL_DISTRICT_ID).ruralDistrictCode(UPDATED_RURAL_DISTRICT_CODE);

        restRuralDistrictMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRuralDistrict.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRuralDistrict))
            )
            .andExpect(status().isOk());

        // Validate the RuralDistrict in the database
        List<RuralDistrict> ruralDistrictList = ruralDistrictRepository.findAll();
        assertThat(ruralDistrictList).hasSize(databaseSizeBeforeUpdate);
        RuralDistrict testRuralDistrict = ruralDistrictList.get(ruralDistrictList.size() - 1);
        assertThat(testRuralDistrict.getRuralDistrictId()).isEqualTo(UPDATED_RURAL_DISTRICT_ID);
        assertThat(testRuralDistrict.getRuralDistrictCode()).isEqualTo(UPDATED_RURAL_DISTRICT_CODE);
        assertThat(testRuralDistrict.getRuralDistrictName()).isEqualTo(DEFAULT_RURAL_DISTRICT_NAME);
        assertThat(testRuralDistrict.getRuralDistrictEnglishName()).isEqualTo(DEFAULT_RURAL_DISTRICT_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void fullUpdateRuralDistrictWithPatch() throws Exception {
        // Initialize the database
        ruralDistrictRepository.saveAndFlush(ruralDistrict);

        int databaseSizeBeforeUpdate = ruralDistrictRepository.findAll().size();

        // Update the ruralDistrict using partial update
        RuralDistrict partialUpdatedRuralDistrict = new RuralDistrict();
        partialUpdatedRuralDistrict.setId(ruralDistrict.getId());

        partialUpdatedRuralDistrict
            .ruralDistrictId(UPDATED_RURAL_DISTRICT_ID)
            .ruralDistrictCode(UPDATED_RURAL_DISTRICT_CODE)
            .ruralDistrictName(UPDATED_RURAL_DISTRICT_NAME)
            .ruralDistrictEnglishName(UPDATED_RURAL_DISTRICT_ENGLISH_NAME);

        restRuralDistrictMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRuralDistrict.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRuralDistrict))
            )
            .andExpect(status().isOk());

        // Validate the RuralDistrict in the database
        List<RuralDistrict> ruralDistrictList = ruralDistrictRepository.findAll();
        assertThat(ruralDistrictList).hasSize(databaseSizeBeforeUpdate);
        RuralDistrict testRuralDistrict = ruralDistrictList.get(ruralDistrictList.size() - 1);
        assertThat(testRuralDistrict.getRuralDistrictId()).isEqualTo(UPDATED_RURAL_DISTRICT_ID);
        assertThat(testRuralDistrict.getRuralDistrictCode()).isEqualTo(UPDATED_RURAL_DISTRICT_CODE);
        assertThat(testRuralDistrict.getRuralDistrictName()).isEqualTo(UPDATED_RURAL_DISTRICT_NAME);
        assertThat(testRuralDistrict.getRuralDistrictEnglishName()).isEqualTo(UPDATED_RURAL_DISTRICT_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingRuralDistrict() throws Exception {
        int databaseSizeBeforeUpdate = ruralDistrictRepository.findAll().size();
        ruralDistrict.setId(count.incrementAndGet());

        // Create the RuralDistrict
        RuralDistrictDTO ruralDistrictDTO = ruralDistrictMapper.toDto(ruralDistrict);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRuralDistrictMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ruralDistrictDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ruralDistrictDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RuralDistrict in the database
        List<RuralDistrict> ruralDistrictList = ruralDistrictRepository.findAll();
        assertThat(ruralDistrictList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRuralDistrict() throws Exception {
        int databaseSizeBeforeUpdate = ruralDistrictRepository.findAll().size();
        ruralDistrict.setId(count.incrementAndGet());

        // Create the RuralDistrict
        RuralDistrictDTO ruralDistrictDTO = ruralDistrictMapper.toDto(ruralDistrict);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRuralDistrictMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ruralDistrictDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RuralDistrict in the database
        List<RuralDistrict> ruralDistrictList = ruralDistrictRepository.findAll();
        assertThat(ruralDistrictList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRuralDistrict() throws Exception {
        int databaseSizeBeforeUpdate = ruralDistrictRepository.findAll().size();
        ruralDistrict.setId(count.incrementAndGet());

        // Create the RuralDistrict
        RuralDistrictDTO ruralDistrictDTO = ruralDistrictMapper.toDto(ruralDistrict);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRuralDistrictMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ruralDistrictDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RuralDistrict in the database
        List<RuralDistrict> ruralDistrictList = ruralDistrictRepository.findAll();
        assertThat(ruralDistrictList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRuralDistrict() throws Exception {
        // Initialize the database
        ruralDistrictRepository.saveAndFlush(ruralDistrict);

        int databaseSizeBeforeDelete = ruralDistrictRepository.findAll().size();

        // Delete the ruralDistrict
        restRuralDistrictMockMvc
            .perform(delete(ENTITY_API_URL_ID, ruralDistrict.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RuralDistrict> ruralDistrictList = ruralDistrictRepository.findAll();
        assertThat(ruralDistrictList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
