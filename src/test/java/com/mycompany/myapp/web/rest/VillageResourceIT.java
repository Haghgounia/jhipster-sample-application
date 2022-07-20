package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.RuralDistrict;
import com.mycompany.myapp.domain.Village;
import com.mycompany.myapp.repository.VillageRepository;
import com.mycompany.myapp.service.criteria.VillageCriteria;
import com.mycompany.myapp.service.dto.VillageDTO;
import com.mycompany.myapp.service.mapper.VillageMapper;
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
 * Integration tests for the {@link VillageResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class VillageResourceIT {

    private static final Integer DEFAULT_VILLAGE_ID = 1;
    private static final Integer UPDATED_VILLAGE_ID = 2;
    private static final Integer SMALLER_VILLAGE_ID = 1 - 1;

    private static final String DEFAULT_VILLAGE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_VILLAGE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_VILLAGE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_VILLAGE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_VILLAGE_ENGLISH_NAME = "AAAAAAAAAA";
    private static final String UPDATED_VILLAGE_ENGLISH_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/villages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VillageRepository villageRepository;

    @Autowired
    private VillageMapper villageMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVillageMockMvc;

    private Village village;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Village createEntity(EntityManager em) {
        Village village = new Village()
            .villageId(DEFAULT_VILLAGE_ID)
            .villageCode(DEFAULT_VILLAGE_CODE)
            .villageName(DEFAULT_VILLAGE_NAME)
            .villageEnglishName(DEFAULT_VILLAGE_ENGLISH_NAME);
        return village;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Village createUpdatedEntity(EntityManager em) {
        Village village = new Village()
            .villageId(UPDATED_VILLAGE_ID)
            .villageCode(UPDATED_VILLAGE_CODE)
            .villageName(UPDATED_VILLAGE_NAME)
            .villageEnglishName(UPDATED_VILLAGE_ENGLISH_NAME);
        return village;
    }

    @BeforeEach
    public void initTest() {
        village = createEntity(em);
    }

    @Test
    @Transactional
    void createVillage() throws Exception {
        int databaseSizeBeforeCreate = villageRepository.findAll().size();
        // Create the Village
        VillageDTO villageDTO = villageMapper.toDto(village);
        restVillageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(villageDTO)))
            .andExpect(status().isCreated());

        // Validate the Village in the database
        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeCreate + 1);
        Village testVillage = villageList.get(villageList.size() - 1);
        assertThat(testVillage.getVillageId()).isEqualTo(DEFAULT_VILLAGE_ID);
        assertThat(testVillage.getVillageCode()).isEqualTo(DEFAULT_VILLAGE_CODE);
        assertThat(testVillage.getVillageName()).isEqualTo(DEFAULT_VILLAGE_NAME);
        assertThat(testVillage.getVillageEnglishName()).isEqualTo(DEFAULT_VILLAGE_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void createVillageWithExistingId() throws Exception {
        // Create the Village with an existing ID
        village.setId(1L);
        VillageDTO villageDTO = villageMapper.toDto(village);

        int databaseSizeBeforeCreate = villageRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVillageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(villageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Village in the database
        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkVillageIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = villageRepository.findAll().size();
        // set the field null
        village.setVillageId(null);

        // Create the Village, which fails.
        VillageDTO villageDTO = villageMapper.toDto(village);

        restVillageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(villageDTO)))
            .andExpect(status().isBadRequest());

        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVillageCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = villageRepository.findAll().size();
        // set the field null
        village.setVillageCode(null);

        // Create the Village, which fails.
        VillageDTO villageDTO = villageMapper.toDto(village);

        restVillageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(villageDTO)))
            .andExpect(status().isBadRequest());

        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVillages() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList
        restVillageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(village.getId().intValue())))
            .andExpect(jsonPath("$.[*].villageId").value(hasItem(DEFAULT_VILLAGE_ID)))
            .andExpect(jsonPath("$.[*].villageCode").value(hasItem(DEFAULT_VILLAGE_CODE)))
            .andExpect(jsonPath("$.[*].villageName").value(hasItem(DEFAULT_VILLAGE_NAME)))
            .andExpect(jsonPath("$.[*].villageEnglishName").value(hasItem(DEFAULT_VILLAGE_ENGLISH_NAME)));
    }

    @Test
    @Transactional
    void getVillage() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get the village
        restVillageMockMvc
            .perform(get(ENTITY_API_URL_ID, village.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(village.getId().intValue()))
            .andExpect(jsonPath("$.villageId").value(DEFAULT_VILLAGE_ID))
            .andExpect(jsonPath("$.villageCode").value(DEFAULT_VILLAGE_CODE))
            .andExpect(jsonPath("$.villageName").value(DEFAULT_VILLAGE_NAME))
            .andExpect(jsonPath("$.villageEnglishName").value(DEFAULT_VILLAGE_ENGLISH_NAME));
    }

    @Test
    @Transactional
    void getVillagesByIdFiltering() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        Long id = village.getId();

        defaultVillageShouldBeFound("id.equals=" + id);
        defaultVillageShouldNotBeFound("id.notEquals=" + id);

        defaultVillageShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultVillageShouldNotBeFound("id.greaterThan=" + id);

        defaultVillageShouldBeFound("id.lessThanOrEqual=" + id);
        defaultVillageShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllVillagesByVillageIdIsEqualToSomething() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where villageId equals to DEFAULT_VILLAGE_ID
        defaultVillageShouldBeFound("villageId.equals=" + DEFAULT_VILLAGE_ID);

        // Get all the villageList where villageId equals to UPDATED_VILLAGE_ID
        defaultVillageShouldNotBeFound("villageId.equals=" + UPDATED_VILLAGE_ID);
    }

    @Test
    @Transactional
    void getAllVillagesByVillageIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where villageId not equals to DEFAULT_VILLAGE_ID
        defaultVillageShouldNotBeFound("villageId.notEquals=" + DEFAULT_VILLAGE_ID);

        // Get all the villageList where villageId not equals to UPDATED_VILLAGE_ID
        defaultVillageShouldBeFound("villageId.notEquals=" + UPDATED_VILLAGE_ID);
    }

    @Test
    @Transactional
    void getAllVillagesByVillageIdIsInShouldWork() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where villageId in DEFAULT_VILLAGE_ID or UPDATED_VILLAGE_ID
        defaultVillageShouldBeFound("villageId.in=" + DEFAULT_VILLAGE_ID + "," + UPDATED_VILLAGE_ID);

        // Get all the villageList where villageId equals to UPDATED_VILLAGE_ID
        defaultVillageShouldNotBeFound("villageId.in=" + UPDATED_VILLAGE_ID);
    }

    @Test
    @Transactional
    void getAllVillagesByVillageIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where villageId is not null
        defaultVillageShouldBeFound("villageId.specified=true");

        // Get all the villageList where villageId is null
        defaultVillageShouldNotBeFound("villageId.specified=false");
    }

    @Test
    @Transactional
    void getAllVillagesByVillageIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where villageId is greater than or equal to DEFAULT_VILLAGE_ID
        defaultVillageShouldBeFound("villageId.greaterThanOrEqual=" + DEFAULT_VILLAGE_ID);

        // Get all the villageList where villageId is greater than or equal to UPDATED_VILLAGE_ID
        defaultVillageShouldNotBeFound("villageId.greaterThanOrEqual=" + UPDATED_VILLAGE_ID);
    }

    @Test
    @Transactional
    void getAllVillagesByVillageIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where villageId is less than or equal to DEFAULT_VILLAGE_ID
        defaultVillageShouldBeFound("villageId.lessThanOrEqual=" + DEFAULT_VILLAGE_ID);

        // Get all the villageList where villageId is less than or equal to SMALLER_VILLAGE_ID
        defaultVillageShouldNotBeFound("villageId.lessThanOrEqual=" + SMALLER_VILLAGE_ID);
    }

    @Test
    @Transactional
    void getAllVillagesByVillageIdIsLessThanSomething() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where villageId is less than DEFAULT_VILLAGE_ID
        defaultVillageShouldNotBeFound("villageId.lessThan=" + DEFAULT_VILLAGE_ID);

        // Get all the villageList where villageId is less than UPDATED_VILLAGE_ID
        defaultVillageShouldBeFound("villageId.lessThan=" + UPDATED_VILLAGE_ID);
    }

    @Test
    @Transactional
    void getAllVillagesByVillageIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where villageId is greater than DEFAULT_VILLAGE_ID
        defaultVillageShouldNotBeFound("villageId.greaterThan=" + DEFAULT_VILLAGE_ID);

        // Get all the villageList where villageId is greater than SMALLER_VILLAGE_ID
        defaultVillageShouldBeFound("villageId.greaterThan=" + SMALLER_VILLAGE_ID);
    }

    @Test
    @Transactional
    void getAllVillagesByVillageCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where villageCode equals to DEFAULT_VILLAGE_CODE
        defaultVillageShouldBeFound("villageCode.equals=" + DEFAULT_VILLAGE_CODE);

        // Get all the villageList where villageCode equals to UPDATED_VILLAGE_CODE
        defaultVillageShouldNotBeFound("villageCode.equals=" + UPDATED_VILLAGE_CODE);
    }

    @Test
    @Transactional
    void getAllVillagesByVillageCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where villageCode not equals to DEFAULT_VILLAGE_CODE
        defaultVillageShouldNotBeFound("villageCode.notEquals=" + DEFAULT_VILLAGE_CODE);

        // Get all the villageList where villageCode not equals to UPDATED_VILLAGE_CODE
        defaultVillageShouldBeFound("villageCode.notEquals=" + UPDATED_VILLAGE_CODE);
    }

    @Test
    @Transactional
    void getAllVillagesByVillageCodeIsInShouldWork() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where villageCode in DEFAULT_VILLAGE_CODE or UPDATED_VILLAGE_CODE
        defaultVillageShouldBeFound("villageCode.in=" + DEFAULT_VILLAGE_CODE + "," + UPDATED_VILLAGE_CODE);

        // Get all the villageList where villageCode equals to UPDATED_VILLAGE_CODE
        defaultVillageShouldNotBeFound("villageCode.in=" + UPDATED_VILLAGE_CODE);
    }

    @Test
    @Transactional
    void getAllVillagesByVillageCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where villageCode is not null
        defaultVillageShouldBeFound("villageCode.specified=true");

        // Get all the villageList where villageCode is null
        defaultVillageShouldNotBeFound("villageCode.specified=false");
    }

    @Test
    @Transactional
    void getAllVillagesByVillageCodeContainsSomething() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where villageCode contains DEFAULT_VILLAGE_CODE
        defaultVillageShouldBeFound("villageCode.contains=" + DEFAULT_VILLAGE_CODE);

        // Get all the villageList where villageCode contains UPDATED_VILLAGE_CODE
        defaultVillageShouldNotBeFound("villageCode.contains=" + UPDATED_VILLAGE_CODE);
    }

    @Test
    @Transactional
    void getAllVillagesByVillageCodeNotContainsSomething() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where villageCode does not contain DEFAULT_VILLAGE_CODE
        defaultVillageShouldNotBeFound("villageCode.doesNotContain=" + DEFAULT_VILLAGE_CODE);

        // Get all the villageList where villageCode does not contain UPDATED_VILLAGE_CODE
        defaultVillageShouldBeFound("villageCode.doesNotContain=" + UPDATED_VILLAGE_CODE);
    }

    @Test
    @Transactional
    void getAllVillagesByVillageNameIsEqualToSomething() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where villageName equals to DEFAULT_VILLAGE_NAME
        defaultVillageShouldBeFound("villageName.equals=" + DEFAULT_VILLAGE_NAME);

        // Get all the villageList where villageName equals to UPDATED_VILLAGE_NAME
        defaultVillageShouldNotBeFound("villageName.equals=" + UPDATED_VILLAGE_NAME);
    }

    @Test
    @Transactional
    void getAllVillagesByVillageNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where villageName not equals to DEFAULT_VILLAGE_NAME
        defaultVillageShouldNotBeFound("villageName.notEquals=" + DEFAULT_VILLAGE_NAME);

        // Get all the villageList where villageName not equals to UPDATED_VILLAGE_NAME
        defaultVillageShouldBeFound("villageName.notEquals=" + UPDATED_VILLAGE_NAME);
    }

    @Test
    @Transactional
    void getAllVillagesByVillageNameIsInShouldWork() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where villageName in DEFAULT_VILLAGE_NAME or UPDATED_VILLAGE_NAME
        defaultVillageShouldBeFound("villageName.in=" + DEFAULT_VILLAGE_NAME + "," + UPDATED_VILLAGE_NAME);

        // Get all the villageList where villageName equals to UPDATED_VILLAGE_NAME
        defaultVillageShouldNotBeFound("villageName.in=" + UPDATED_VILLAGE_NAME);
    }

    @Test
    @Transactional
    void getAllVillagesByVillageNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where villageName is not null
        defaultVillageShouldBeFound("villageName.specified=true");

        // Get all the villageList where villageName is null
        defaultVillageShouldNotBeFound("villageName.specified=false");
    }

    @Test
    @Transactional
    void getAllVillagesByVillageNameContainsSomething() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where villageName contains DEFAULT_VILLAGE_NAME
        defaultVillageShouldBeFound("villageName.contains=" + DEFAULT_VILLAGE_NAME);

        // Get all the villageList where villageName contains UPDATED_VILLAGE_NAME
        defaultVillageShouldNotBeFound("villageName.contains=" + UPDATED_VILLAGE_NAME);
    }

    @Test
    @Transactional
    void getAllVillagesByVillageNameNotContainsSomething() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where villageName does not contain DEFAULT_VILLAGE_NAME
        defaultVillageShouldNotBeFound("villageName.doesNotContain=" + DEFAULT_VILLAGE_NAME);

        // Get all the villageList where villageName does not contain UPDATED_VILLAGE_NAME
        defaultVillageShouldBeFound("villageName.doesNotContain=" + UPDATED_VILLAGE_NAME);
    }

    @Test
    @Transactional
    void getAllVillagesByVillageEnglishNameIsEqualToSomething() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where villageEnglishName equals to DEFAULT_VILLAGE_ENGLISH_NAME
        defaultVillageShouldBeFound("villageEnglishName.equals=" + DEFAULT_VILLAGE_ENGLISH_NAME);

        // Get all the villageList where villageEnglishName equals to UPDATED_VILLAGE_ENGLISH_NAME
        defaultVillageShouldNotBeFound("villageEnglishName.equals=" + UPDATED_VILLAGE_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void getAllVillagesByVillageEnglishNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where villageEnglishName not equals to DEFAULT_VILLAGE_ENGLISH_NAME
        defaultVillageShouldNotBeFound("villageEnglishName.notEquals=" + DEFAULT_VILLAGE_ENGLISH_NAME);

        // Get all the villageList where villageEnglishName not equals to UPDATED_VILLAGE_ENGLISH_NAME
        defaultVillageShouldBeFound("villageEnglishName.notEquals=" + UPDATED_VILLAGE_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void getAllVillagesByVillageEnglishNameIsInShouldWork() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where villageEnglishName in DEFAULT_VILLAGE_ENGLISH_NAME or UPDATED_VILLAGE_ENGLISH_NAME
        defaultVillageShouldBeFound("villageEnglishName.in=" + DEFAULT_VILLAGE_ENGLISH_NAME + "," + UPDATED_VILLAGE_ENGLISH_NAME);

        // Get all the villageList where villageEnglishName equals to UPDATED_VILLAGE_ENGLISH_NAME
        defaultVillageShouldNotBeFound("villageEnglishName.in=" + UPDATED_VILLAGE_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void getAllVillagesByVillageEnglishNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where villageEnglishName is not null
        defaultVillageShouldBeFound("villageEnglishName.specified=true");

        // Get all the villageList where villageEnglishName is null
        defaultVillageShouldNotBeFound("villageEnglishName.specified=false");
    }

    @Test
    @Transactional
    void getAllVillagesByVillageEnglishNameContainsSomething() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where villageEnglishName contains DEFAULT_VILLAGE_ENGLISH_NAME
        defaultVillageShouldBeFound("villageEnglishName.contains=" + DEFAULT_VILLAGE_ENGLISH_NAME);

        // Get all the villageList where villageEnglishName contains UPDATED_VILLAGE_ENGLISH_NAME
        defaultVillageShouldNotBeFound("villageEnglishName.contains=" + UPDATED_VILLAGE_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void getAllVillagesByVillageEnglishNameNotContainsSomething() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where villageEnglishName does not contain DEFAULT_VILLAGE_ENGLISH_NAME
        defaultVillageShouldNotBeFound("villageEnglishName.doesNotContain=" + DEFAULT_VILLAGE_ENGLISH_NAME);

        // Get all the villageList where villageEnglishName does not contain UPDATED_VILLAGE_ENGLISH_NAME
        defaultVillageShouldBeFound("villageEnglishName.doesNotContain=" + UPDATED_VILLAGE_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void getAllVillagesByRuralDistrictIsEqualToSomething() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);
        RuralDistrict ruralDistrict;
        if (TestUtil.findAll(em, RuralDistrict.class).isEmpty()) {
            ruralDistrict = RuralDistrictResourceIT.createEntity(em);
            em.persist(ruralDistrict);
            em.flush();
        } else {
            ruralDistrict = TestUtil.findAll(em, RuralDistrict.class).get(0);
        }
        em.persist(ruralDistrict);
        em.flush();
        village.setRuralDistrict(ruralDistrict);
        villageRepository.saveAndFlush(village);
        Long ruralDistrictId = ruralDistrict.getId();

        // Get all the villageList where ruralDistrict equals to ruralDistrictId
        defaultVillageShouldBeFound("ruralDistrictId.equals=" + ruralDistrictId);

        // Get all the villageList where ruralDistrict equals to (ruralDistrictId + 1)
        defaultVillageShouldNotBeFound("ruralDistrictId.equals=" + (ruralDistrictId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVillageShouldBeFound(String filter) throws Exception {
        restVillageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(village.getId().intValue())))
            .andExpect(jsonPath("$.[*].villageId").value(hasItem(DEFAULT_VILLAGE_ID)))
            .andExpect(jsonPath("$.[*].villageCode").value(hasItem(DEFAULT_VILLAGE_CODE)))
            .andExpect(jsonPath("$.[*].villageName").value(hasItem(DEFAULT_VILLAGE_NAME)))
            .andExpect(jsonPath("$.[*].villageEnglishName").value(hasItem(DEFAULT_VILLAGE_ENGLISH_NAME)));

        // Check, that the count call also returns 1
        restVillageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVillageShouldNotBeFound(String filter) throws Exception {
        restVillageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVillageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingVillage() throws Exception {
        // Get the village
        restVillageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVillage() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        int databaseSizeBeforeUpdate = villageRepository.findAll().size();

        // Update the village
        Village updatedVillage = villageRepository.findById(village.getId()).get();
        // Disconnect from session so that the updates on updatedVillage are not directly saved in db
        em.detach(updatedVillage);
        updatedVillage
            .villageId(UPDATED_VILLAGE_ID)
            .villageCode(UPDATED_VILLAGE_CODE)
            .villageName(UPDATED_VILLAGE_NAME)
            .villageEnglishName(UPDATED_VILLAGE_ENGLISH_NAME);
        VillageDTO villageDTO = villageMapper.toDto(updatedVillage);

        restVillageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, villageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(villageDTO))
            )
            .andExpect(status().isOk());

        // Validate the Village in the database
        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeUpdate);
        Village testVillage = villageList.get(villageList.size() - 1);
        assertThat(testVillage.getVillageId()).isEqualTo(UPDATED_VILLAGE_ID);
        assertThat(testVillage.getVillageCode()).isEqualTo(UPDATED_VILLAGE_CODE);
        assertThat(testVillage.getVillageName()).isEqualTo(UPDATED_VILLAGE_NAME);
        assertThat(testVillage.getVillageEnglishName()).isEqualTo(UPDATED_VILLAGE_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void putNonExistingVillage() throws Exception {
        int databaseSizeBeforeUpdate = villageRepository.findAll().size();
        village.setId(count.incrementAndGet());

        // Create the Village
        VillageDTO villageDTO = villageMapper.toDto(village);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVillageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, villageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(villageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Village in the database
        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVillage() throws Exception {
        int databaseSizeBeforeUpdate = villageRepository.findAll().size();
        village.setId(count.incrementAndGet());

        // Create the Village
        VillageDTO villageDTO = villageMapper.toDto(village);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVillageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(villageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Village in the database
        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVillage() throws Exception {
        int databaseSizeBeforeUpdate = villageRepository.findAll().size();
        village.setId(count.incrementAndGet());

        // Create the Village
        VillageDTO villageDTO = villageMapper.toDto(village);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVillageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(villageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Village in the database
        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVillageWithPatch() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        int databaseSizeBeforeUpdate = villageRepository.findAll().size();

        // Update the village using partial update
        Village partialUpdatedVillage = new Village();
        partialUpdatedVillage.setId(village.getId());

        partialUpdatedVillage
            .villageId(UPDATED_VILLAGE_ID)
            .villageName(UPDATED_VILLAGE_NAME)
            .villageEnglishName(UPDATED_VILLAGE_ENGLISH_NAME);

        restVillageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVillage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVillage))
            )
            .andExpect(status().isOk());

        // Validate the Village in the database
        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeUpdate);
        Village testVillage = villageList.get(villageList.size() - 1);
        assertThat(testVillage.getVillageId()).isEqualTo(UPDATED_VILLAGE_ID);
        assertThat(testVillage.getVillageCode()).isEqualTo(DEFAULT_VILLAGE_CODE);
        assertThat(testVillage.getVillageName()).isEqualTo(UPDATED_VILLAGE_NAME);
        assertThat(testVillage.getVillageEnglishName()).isEqualTo(UPDATED_VILLAGE_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void fullUpdateVillageWithPatch() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        int databaseSizeBeforeUpdate = villageRepository.findAll().size();

        // Update the village using partial update
        Village partialUpdatedVillage = new Village();
        partialUpdatedVillage.setId(village.getId());

        partialUpdatedVillage
            .villageId(UPDATED_VILLAGE_ID)
            .villageCode(UPDATED_VILLAGE_CODE)
            .villageName(UPDATED_VILLAGE_NAME)
            .villageEnglishName(UPDATED_VILLAGE_ENGLISH_NAME);

        restVillageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVillage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVillage))
            )
            .andExpect(status().isOk());

        // Validate the Village in the database
        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeUpdate);
        Village testVillage = villageList.get(villageList.size() - 1);
        assertThat(testVillage.getVillageId()).isEqualTo(UPDATED_VILLAGE_ID);
        assertThat(testVillage.getVillageCode()).isEqualTo(UPDATED_VILLAGE_CODE);
        assertThat(testVillage.getVillageName()).isEqualTo(UPDATED_VILLAGE_NAME);
        assertThat(testVillage.getVillageEnglishName()).isEqualTo(UPDATED_VILLAGE_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingVillage() throws Exception {
        int databaseSizeBeforeUpdate = villageRepository.findAll().size();
        village.setId(count.incrementAndGet());

        // Create the Village
        VillageDTO villageDTO = villageMapper.toDto(village);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVillageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, villageDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(villageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Village in the database
        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVillage() throws Exception {
        int databaseSizeBeforeUpdate = villageRepository.findAll().size();
        village.setId(count.incrementAndGet());

        // Create the Village
        VillageDTO villageDTO = villageMapper.toDto(village);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVillageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(villageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Village in the database
        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVillage() throws Exception {
        int databaseSizeBeforeUpdate = villageRepository.findAll().size();
        village.setId(count.incrementAndGet());

        // Create the Village
        VillageDTO villageDTO = villageMapper.toDto(village);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVillageMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(villageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Village in the database
        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVillage() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        int databaseSizeBeforeDelete = villageRepository.findAll().size();

        // Delete the village
        restVillageMockMvc
            .perform(delete(ENTITY_API_URL_ID, village.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
