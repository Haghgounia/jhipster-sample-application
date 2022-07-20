package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Language;
import com.mycompany.myapp.repository.LanguageRepository;
import com.mycompany.myapp.service.criteria.LanguageCriteria;
import com.mycompany.myapp.service.dto.LanguageDTO;
import com.mycompany.myapp.service.mapper.LanguageMapper;
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
 * Integration tests for the {@link LanguageResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LanguageResourceIT {

    private static final Integer DEFAULT_LANGUAGE_ID = 1;
    private static final Integer UPDATED_LANGUAGE_ID = 2;
    private static final Integer SMALLER_LANGUAGE_ID = 1 - 1;

    private static final String DEFAULT_LANGUAGE_ISO_CODE = "AAAAAAAAAA";
    private static final String UPDATED_LANGUAGE_ISO_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LANGUAGE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LANGUAGE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LANGUAGE_ENGLISH_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LANGUAGE_ENGLISH_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_IS_ACTIVE = 1;
    private static final Integer UPDATED_IS_ACTIVE = 2;
    private static final Integer SMALLER_IS_ACTIVE = 1 - 1;

    private static final Integer DEFAULT_SORT_ORDER = 1;
    private static final Integer UPDATED_SORT_ORDER = 2;
    private static final Integer SMALLER_SORT_ORDER = 1 - 1;

    private static final String ENTITY_API_URL = "/api/languages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private LanguageMapper languageMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLanguageMockMvc;

    private Language language;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Language createEntity(EntityManager em) {
        Language language = new Language()
            .languageId(DEFAULT_LANGUAGE_ID)
            .languageIsoCode(DEFAULT_LANGUAGE_ISO_CODE)
            .languageName(DEFAULT_LANGUAGE_NAME)
            .languageEnglishName(DEFAULT_LANGUAGE_ENGLISH_NAME)
            .isActive(DEFAULT_IS_ACTIVE)
            .sortOrder(DEFAULT_SORT_ORDER);
        return language;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Language createUpdatedEntity(EntityManager em) {
        Language language = new Language()
            .languageId(UPDATED_LANGUAGE_ID)
            .languageIsoCode(UPDATED_LANGUAGE_ISO_CODE)
            .languageName(UPDATED_LANGUAGE_NAME)
            .languageEnglishName(UPDATED_LANGUAGE_ENGLISH_NAME)
            .isActive(UPDATED_IS_ACTIVE)
            .sortOrder(UPDATED_SORT_ORDER);
        return language;
    }

    @BeforeEach
    public void initTest() {
        language = createEntity(em);
    }

    @Test
    @Transactional
    void createLanguage() throws Exception {
        int databaseSizeBeforeCreate = languageRepository.findAll().size();
        // Create the Language
        LanguageDTO languageDTO = languageMapper.toDto(language);
        restLanguageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(languageDTO)))
            .andExpect(status().isCreated());

        // Validate the Language in the database
        List<Language> languageList = languageRepository.findAll();
        assertThat(languageList).hasSize(databaseSizeBeforeCreate + 1);
        Language testLanguage = languageList.get(languageList.size() - 1);
        assertThat(testLanguage.getLanguageId()).isEqualTo(DEFAULT_LANGUAGE_ID);
        assertThat(testLanguage.getLanguageIsoCode()).isEqualTo(DEFAULT_LANGUAGE_ISO_CODE);
        assertThat(testLanguage.getLanguageName()).isEqualTo(DEFAULT_LANGUAGE_NAME);
        assertThat(testLanguage.getLanguageEnglishName()).isEqualTo(DEFAULT_LANGUAGE_ENGLISH_NAME);
        assertThat(testLanguage.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testLanguage.getSortOrder()).isEqualTo(DEFAULT_SORT_ORDER);
    }

    @Test
    @Transactional
    void createLanguageWithExistingId() throws Exception {
        // Create the Language with an existing ID
        language.setId(1L);
        LanguageDTO languageDTO = languageMapper.toDto(language);

        int databaseSizeBeforeCreate = languageRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLanguageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(languageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Language in the database
        List<Language> languageList = languageRepository.findAll();
        assertThat(languageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLanguageIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = languageRepository.findAll().size();
        // set the field null
        language.setLanguageId(null);

        // Create the Language, which fails.
        LanguageDTO languageDTO = languageMapper.toDto(language);

        restLanguageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(languageDTO)))
            .andExpect(status().isBadRequest());

        List<Language> languageList = languageRepository.findAll();
        assertThat(languageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLanguageIsoCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = languageRepository.findAll().size();
        // set the field null
        language.setLanguageIsoCode(null);

        // Create the Language, which fails.
        LanguageDTO languageDTO = languageMapper.toDto(language);

        restLanguageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(languageDTO)))
            .andExpect(status().isBadRequest());

        List<Language> languageList = languageRepository.findAll();
        assertThat(languageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLanguages() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList
        restLanguageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(language.getId().intValue())))
            .andExpect(jsonPath("$.[*].languageId").value(hasItem(DEFAULT_LANGUAGE_ID)))
            .andExpect(jsonPath("$.[*].languageIsoCode").value(hasItem(DEFAULT_LANGUAGE_ISO_CODE)))
            .andExpect(jsonPath("$.[*].languageName").value(hasItem(DEFAULT_LANGUAGE_NAME)))
            .andExpect(jsonPath("$.[*].languageEnglishName").value(hasItem(DEFAULT_LANGUAGE_ENGLISH_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)));
    }

    @Test
    @Transactional
    void getLanguage() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get the language
        restLanguageMockMvc
            .perform(get(ENTITY_API_URL_ID, language.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(language.getId().intValue()))
            .andExpect(jsonPath("$.languageId").value(DEFAULT_LANGUAGE_ID))
            .andExpect(jsonPath("$.languageIsoCode").value(DEFAULT_LANGUAGE_ISO_CODE))
            .andExpect(jsonPath("$.languageName").value(DEFAULT_LANGUAGE_NAME))
            .andExpect(jsonPath("$.languageEnglishName").value(DEFAULT_LANGUAGE_ENGLISH_NAME))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE))
            .andExpect(jsonPath("$.sortOrder").value(DEFAULT_SORT_ORDER));
    }

    @Test
    @Transactional
    void getLanguagesByIdFiltering() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        Long id = language.getId();

        defaultLanguageShouldBeFound("id.equals=" + id);
        defaultLanguageShouldNotBeFound("id.notEquals=" + id);

        defaultLanguageShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLanguageShouldNotBeFound("id.greaterThan=" + id);

        defaultLanguageShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLanguageShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLanguagesByLanguageIdIsEqualToSomething() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where languageId equals to DEFAULT_LANGUAGE_ID
        defaultLanguageShouldBeFound("languageId.equals=" + DEFAULT_LANGUAGE_ID);

        // Get all the languageList where languageId equals to UPDATED_LANGUAGE_ID
        defaultLanguageShouldNotBeFound("languageId.equals=" + UPDATED_LANGUAGE_ID);
    }

    @Test
    @Transactional
    void getAllLanguagesByLanguageIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where languageId not equals to DEFAULT_LANGUAGE_ID
        defaultLanguageShouldNotBeFound("languageId.notEquals=" + DEFAULT_LANGUAGE_ID);

        // Get all the languageList where languageId not equals to UPDATED_LANGUAGE_ID
        defaultLanguageShouldBeFound("languageId.notEquals=" + UPDATED_LANGUAGE_ID);
    }

    @Test
    @Transactional
    void getAllLanguagesByLanguageIdIsInShouldWork() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where languageId in DEFAULT_LANGUAGE_ID or UPDATED_LANGUAGE_ID
        defaultLanguageShouldBeFound("languageId.in=" + DEFAULT_LANGUAGE_ID + "," + UPDATED_LANGUAGE_ID);

        // Get all the languageList where languageId equals to UPDATED_LANGUAGE_ID
        defaultLanguageShouldNotBeFound("languageId.in=" + UPDATED_LANGUAGE_ID);
    }

    @Test
    @Transactional
    void getAllLanguagesByLanguageIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where languageId is not null
        defaultLanguageShouldBeFound("languageId.specified=true");

        // Get all the languageList where languageId is null
        defaultLanguageShouldNotBeFound("languageId.specified=false");
    }

    @Test
    @Transactional
    void getAllLanguagesByLanguageIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where languageId is greater than or equal to DEFAULT_LANGUAGE_ID
        defaultLanguageShouldBeFound("languageId.greaterThanOrEqual=" + DEFAULT_LANGUAGE_ID);

        // Get all the languageList where languageId is greater than or equal to UPDATED_LANGUAGE_ID
        defaultLanguageShouldNotBeFound("languageId.greaterThanOrEqual=" + UPDATED_LANGUAGE_ID);
    }

    @Test
    @Transactional
    void getAllLanguagesByLanguageIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where languageId is less than or equal to DEFAULT_LANGUAGE_ID
        defaultLanguageShouldBeFound("languageId.lessThanOrEqual=" + DEFAULT_LANGUAGE_ID);

        // Get all the languageList where languageId is less than or equal to SMALLER_LANGUAGE_ID
        defaultLanguageShouldNotBeFound("languageId.lessThanOrEqual=" + SMALLER_LANGUAGE_ID);
    }

    @Test
    @Transactional
    void getAllLanguagesByLanguageIdIsLessThanSomething() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where languageId is less than DEFAULT_LANGUAGE_ID
        defaultLanguageShouldNotBeFound("languageId.lessThan=" + DEFAULT_LANGUAGE_ID);

        // Get all the languageList where languageId is less than UPDATED_LANGUAGE_ID
        defaultLanguageShouldBeFound("languageId.lessThan=" + UPDATED_LANGUAGE_ID);
    }

    @Test
    @Transactional
    void getAllLanguagesByLanguageIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where languageId is greater than DEFAULT_LANGUAGE_ID
        defaultLanguageShouldNotBeFound("languageId.greaterThan=" + DEFAULT_LANGUAGE_ID);

        // Get all the languageList where languageId is greater than SMALLER_LANGUAGE_ID
        defaultLanguageShouldBeFound("languageId.greaterThan=" + SMALLER_LANGUAGE_ID);
    }

    @Test
    @Transactional
    void getAllLanguagesByLanguageIsoCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where languageIsoCode equals to DEFAULT_LANGUAGE_ISO_CODE
        defaultLanguageShouldBeFound("languageIsoCode.equals=" + DEFAULT_LANGUAGE_ISO_CODE);

        // Get all the languageList where languageIsoCode equals to UPDATED_LANGUAGE_ISO_CODE
        defaultLanguageShouldNotBeFound("languageIsoCode.equals=" + UPDATED_LANGUAGE_ISO_CODE);
    }

    @Test
    @Transactional
    void getAllLanguagesByLanguageIsoCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where languageIsoCode not equals to DEFAULT_LANGUAGE_ISO_CODE
        defaultLanguageShouldNotBeFound("languageIsoCode.notEquals=" + DEFAULT_LANGUAGE_ISO_CODE);

        // Get all the languageList where languageIsoCode not equals to UPDATED_LANGUAGE_ISO_CODE
        defaultLanguageShouldBeFound("languageIsoCode.notEquals=" + UPDATED_LANGUAGE_ISO_CODE);
    }

    @Test
    @Transactional
    void getAllLanguagesByLanguageIsoCodeIsInShouldWork() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where languageIsoCode in DEFAULT_LANGUAGE_ISO_CODE or UPDATED_LANGUAGE_ISO_CODE
        defaultLanguageShouldBeFound("languageIsoCode.in=" + DEFAULT_LANGUAGE_ISO_CODE + "," + UPDATED_LANGUAGE_ISO_CODE);

        // Get all the languageList where languageIsoCode equals to UPDATED_LANGUAGE_ISO_CODE
        defaultLanguageShouldNotBeFound("languageIsoCode.in=" + UPDATED_LANGUAGE_ISO_CODE);
    }

    @Test
    @Transactional
    void getAllLanguagesByLanguageIsoCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where languageIsoCode is not null
        defaultLanguageShouldBeFound("languageIsoCode.specified=true");

        // Get all the languageList where languageIsoCode is null
        defaultLanguageShouldNotBeFound("languageIsoCode.specified=false");
    }

    @Test
    @Transactional
    void getAllLanguagesByLanguageIsoCodeContainsSomething() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where languageIsoCode contains DEFAULT_LANGUAGE_ISO_CODE
        defaultLanguageShouldBeFound("languageIsoCode.contains=" + DEFAULT_LANGUAGE_ISO_CODE);

        // Get all the languageList where languageIsoCode contains UPDATED_LANGUAGE_ISO_CODE
        defaultLanguageShouldNotBeFound("languageIsoCode.contains=" + UPDATED_LANGUAGE_ISO_CODE);
    }

    @Test
    @Transactional
    void getAllLanguagesByLanguageIsoCodeNotContainsSomething() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where languageIsoCode does not contain DEFAULT_LANGUAGE_ISO_CODE
        defaultLanguageShouldNotBeFound("languageIsoCode.doesNotContain=" + DEFAULT_LANGUAGE_ISO_CODE);

        // Get all the languageList where languageIsoCode does not contain UPDATED_LANGUAGE_ISO_CODE
        defaultLanguageShouldBeFound("languageIsoCode.doesNotContain=" + UPDATED_LANGUAGE_ISO_CODE);
    }

    @Test
    @Transactional
    void getAllLanguagesByLanguageNameIsEqualToSomething() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where languageName equals to DEFAULT_LANGUAGE_NAME
        defaultLanguageShouldBeFound("languageName.equals=" + DEFAULT_LANGUAGE_NAME);

        // Get all the languageList where languageName equals to UPDATED_LANGUAGE_NAME
        defaultLanguageShouldNotBeFound("languageName.equals=" + UPDATED_LANGUAGE_NAME);
    }

    @Test
    @Transactional
    void getAllLanguagesByLanguageNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where languageName not equals to DEFAULT_LANGUAGE_NAME
        defaultLanguageShouldNotBeFound("languageName.notEquals=" + DEFAULT_LANGUAGE_NAME);

        // Get all the languageList where languageName not equals to UPDATED_LANGUAGE_NAME
        defaultLanguageShouldBeFound("languageName.notEquals=" + UPDATED_LANGUAGE_NAME);
    }

    @Test
    @Transactional
    void getAllLanguagesByLanguageNameIsInShouldWork() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where languageName in DEFAULT_LANGUAGE_NAME or UPDATED_LANGUAGE_NAME
        defaultLanguageShouldBeFound("languageName.in=" + DEFAULT_LANGUAGE_NAME + "," + UPDATED_LANGUAGE_NAME);

        // Get all the languageList where languageName equals to UPDATED_LANGUAGE_NAME
        defaultLanguageShouldNotBeFound("languageName.in=" + UPDATED_LANGUAGE_NAME);
    }

    @Test
    @Transactional
    void getAllLanguagesByLanguageNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where languageName is not null
        defaultLanguageShouldBeFound("languageName.specified=true");

        // Get all the languageList where languageName is null
        defaultLanguageShouldNotBeFound("languageName.specified=false");
    }

    @Test
    @Transactional
    void getAllLanguagesByLanguageNameContainsSomething() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where languageName contains DEFAULT_LANGUAGE_NAME
        defaultLanguageShouldBeFound("languageName.contains=" + DEFAULT_LANGUAGE_NAME);

        // Get all the languageList where languageName contains UPDATED_LANGUAGE_NAME
        defaultLanguageShouldNotBeFound("languageName.contains=" + UPDATED_LANGUAGE_NAME);
    }

    @Test
    @Transactional
    void getAllLanguagesByLanguageNameNotContainsSomething() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where languageName does not contain DEFAULT_LANGUAGE_NAME
        defaultLanguageShouldNotBeFound("languageName.doesNotContain=" + DEFAULT_LANGUAGE_NAME);

        // Get all the languageList where languageName does not contain UPDATED_LANGUAGE_NAME
        defaultLanguageShouldBeFound("languageName.doesNotContain=" + UPDATED_LANGUAGE_NAME);
    }

    @Test
    @Transactional
    void getAllLanguagesByLanguageEnglishNameIsEqualToSomething() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where languageEnglishName equals to DEFAULT_LANGUAGE_ENGLISH_NAME
        defaultLanguageShouldBeFound("languageEnglishName.equals=" + DEFAULT_LANGUAGE_ENGLISH_NAME);

        // Get all the languageList where languageEnglishName equals to UPDATED_LANGUAGE_ENGLISH_NAME
        defaultLanguageShouldNotBeFound("languageEnglishName.equals=" + UPDATED_LANGUAGE_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void getAllLanguagesByLanguageEnglishNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where languageEnglishName not equals to DEFAULT_LANGUAGE_ENGLISH_NAME
        defaultLanguageShouldNotBeFound("languageEnglishName.notEquals=" + DEFAULT_LANGUAGE_ENGLISH_NAME);

        // Get all the languageList where languageEnglishName not equals to UPDATED_LANGUAGE_ENGLISH_NAME
        defaultLanguageShouldBeFound("languageEnglishName.notEquals=" + UPDATED_LANGUAGE_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void getAllLanguagesByLanguageEnglishNameIsInShouldWork() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where languageEnglishName in DEFAULT_LANGUAGE_ENGLISH_NAME or UPDATED_LANGUAGE_ENGLISH_NAME
        defaultLanguageShouldBeFound("languageEnglishName.in=" + DEFAULT_LANGUAGE_ENGLISH_NAME + "," + UPDATED_LANGUAGE_ENGLISH_NAME);

        // Get all the languageList where languageEnglishName equals to UPDATED_LANGUAGE_ENGLISH_NAME
        defaultLanguageShouldNotBeFound("languageEnglishName.in=" + UPDATED_LANGUAGE_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void getAllLanguagesByLanguageEnglishNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where languageEnglishName is not null
        defaultLanguageShouldBeFound("languageEnglishName.specified=true");

        // Get all the languageList where languageEnglishName is null
        defaultLanguageShouldNotBeFound("languageEnglishName.specified=false");
    }

    @Test
    @Transactional
    void getAllLanguagesByLanguageEnglishNameContainsSomething() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where languageEnglishName contains DEFAULT_LANGUAGE_ENGLISH_NAME
        defaultLanguageShouldBeFound("languageEnglishName.contains=" + DEFAULT_LANGUAGE_ENGLISH_NAME);

        // Get all the languageList where languageEnglishName contains UPDATED_LANGUAGE_ENGLISH_NAME
        defaultLanguageShouldNotBeFound("languageEnglishName.contains=" + UPDATED_LANGUAGE_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void getAllLanguagesByLanguageEnglishNameNotContainsSomething() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where languageEnglishName does not contain DEFAULT_LANGUAGE_ENGLISH_NAME
        defaultLanguageShouldNotBeFound("languageEnglishName.doesNotContain=" + DEFAULT_LANGUAGE_ENGLISH_NAME);

        // Get all the languageList where languageEnglishName does not contain UPDATED_LANGUAGE_ENGLISH_NAME
        defaultLanguageShouldBeFound("languageEnglishName.doesNotContain=" + UPDATED_LANGUAGE_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void getAllLanguagesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where isActive equals to DEFAULT_IS_ACTIVE
        defaultLanguageShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the languageList where isActive equals to UPDATED_IS_ACTIVE
        defaultLanguageShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllLanguagesByIsActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where isActive not equals to DEFAULT_IS_ACTIVE
        defaultLanguageShouldNotBeFound("isActive.notEquals=" + DEFAULT_IS_ACTIVE);

        // Get all the languageList where isActive not equals to UPDATED_IS_ACTIVE
        defaultLanguageShouldBeFound("isActive.notEquals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllLanguagesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultLanguageShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the languageList where isActive equals to UPDATED_IS_ACTIVE
        defaultLanguageShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllLanguagesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where isActive is not null
        defaultLanguageShouldBeFound("isActive.specified=true");

        // Get all the languageList where isActive is null
        defaultLanguageShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllLanguagesByIsActiveIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where isActive is greater than or equal to DEFAULT_IS_ACTIVE
        defaultLanguageShouldBeFound("isActive.greaterThanOrEqual=" + DEFAULT_IS_ACTIVE);

        // Get all the languageList where isActive is greater than or equal to UPDATED_IS_ACTIVE
        defaultLanguageShouldNotBeFound("isActive.greaterThanOrEqual=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllLanguagesByIsActiveIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where isActive is less than or equal to DEFAULT_IS_ACTIVE
        defaultLanguageShouldBeFound("isActive.lessThanOrEqual=" + DEFAULT_IS_ACTIVE);

        // Get all the languageList where isActive is less than or equal to SMALLER_IS_ACTIVE
        defaultLanguageShouldNotBeFound("isActive.lessThanOrEqual=" + SMALLER_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllLanguagesByIsActiveIsLessThanSomething() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where isActive is less than DEFAULT_IS_ACTIVE
        defaultLanguageShouldNotBeFound("isActive.lessThan=" + DEFAULT_IS_ACTIVE);

        // Get all the languageList where isActive is less than UPDATED_IS_ACTIVE
        defaultLanguageShouldBeFound("isActive.lessThan=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllLanguagesByIsActiveIsGreaterThanSomething() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where isActive is greater than DEFAULT_IS_ACTIVE
        defaultLanguageShouldNotBeFound("isActive.greaterThan=" + DEFAULT_IS_ACTIVE);

        // Get all the languageList where isActive is greater than SMALLER_IS_ACTIVE
        defaultLanguageShouldBeFound("isActive.greaterThan=" + SMALLER_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllLanguagesBySortOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where sortOrder equals to DEFAULT_SORT_ORDER
        defaultLanguageShouldBeFound("sortOrder.equals=" + DEFAULT_SORT_ORDER);

        // Get all the languageList where sortOrder equals to UPDATED_SORT_ORDER
        defaultLanguageShouldNotBeFound("sortOrder.equals=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    void getAllLanguagesBySortOrderIsNotEqualToSomething() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where sortOrder not equals to DEFAULT_SORT_ORDER
        defaultLanguageShouldNotBeFound("sortOrder.notEquals=" + DEFAULT_SORT_ORDER);

        // Get all the languageList where sortOrder not equals to UPDATED_SORT_ORDER
        defaultLanguageShouldBeFound("sortOrder.notEquals=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    void getAllLanguagesBySortOrderIsInShouldWork() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where sortOrder in DEFAULT_SORT_ORDER or UPDATED_SORT_ORDER
        defaultLanguageShouldBeFound("sortOrder.in=" + DEFAULT_SORT_ORDER + "," + UPDATED_SORT_ORDER);

        // Get all the languageList where sortOrder equals to UPDATED_SORT_ORDER
        defaultLanguageShouldNotBeFound("sortOrder.in=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    void getAllLanguagesBySortOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where sortOrder is not null
        defaultLanguageShouldBeFound("sortOrder.specified=true");

        // Get all the languageList where sortOrder is null
        defaultLanguageShouldNotBeFound("sortOrder.specified=false");
    }

    @Test
    @Transactional
    void getAllLanguagesBySortOrderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where sortOrder is greater than or equal to DEFAULT_SORT_ORDER
        defaultLanguageShouldBeFound("sortOrder.greaterThanOrEqual=" + DEFAULT_SORT_ORDER);

        // Get all the languageList where sortOrder is greater than or equal to UPDATED_SORT_ORDER
        defaultLanguageShouldNotBeFound("sortOrder.greaterThanOrEqual=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    void getAllLanguagesBySortOrderIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where sortOrder is less than or equal to DEFAULT_SORT_ORDER
        defaultLanguageShouldBeFound("sortOrder.lessThanOrEqual=" + DEFAULT_SORT_ORDER);

        // Get all the languageList where sortOrder is less than or equal to SMALLER_SORT_ORDER
        defaultLanguageShouldNotBeFound("sortOrder.lessThanOrEqual=" + SMALLER_SORT_ORDER);
    }

    @Test
    @Transactional
    void getAllLanguagesBySortOrderIsLessThanSomething() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where sortOrder is less than DEFAULT_SORT_ORDER
        defaultLanguageShouldNotBeFound("sortOrder.lessThan=" + DEFAULT_SORT_ORDER);

        // Get all the languageList where sortOrder is less than UPDATED_SORT_ORDER
        defaultLanguageShouldBeFound("sortOrder.lessThan=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    void getAllLanguagesBySortOrderIsGreaterThanSomething() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where sortOrder is greater than DEFAULT_SORT_ORDER
        defaultLanguageShouldNotBeFound("sortOrder.greaterThan=" + DEFAULT_SORT_ORDER);

        // Get all the languageList where sortOrder is greater than SMALLER_SORT_ORDER
        defaultLanguageShouldBeFound("sortOrder.greaterThan=" + SMALLER_SORT_ORDER);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLanguageShouldBeFound(String filter) throws Exception {
        restLanguageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(language.getId().intValue())))
            .andExpect(jsonPath("$.[*].languageId").value(hasItem(DEFAULT_LANGUAGE_ID)))
            .andExpect(jsonPath("$.[*].languageIsoCode").value(hasItem(DEFAULT_LANGUAGE_ISO_CODE)))
            .andExpect(jsonPath("$.[*].languageName").value(hasItem(DEFAULT_LANGUAGE_NAME)))
            .andExpect(jsonPath("$.[*].languageEnglishName").value(hasItem(DEFAULT_LANGUAGE_ENGLISH_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)));

        // Check, that the count call also returns 1
        restLanguageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLanguageShouldNotBeFound(String filter) throws Exception {
        restLanguageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLanguageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLanguage() throws Exception {
        // Get the language
        restLanguageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLanguage() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        int databaseSizeBeforeUpdate = languageRepository.findAll().size();

        // Update the language
        Language updatedLanguage = languageRepository.findById(language.getId()).get();
        // Disconnect from session so that the updates on updatedLanguage are not directly saved in db
        em.detach(updatedLanguage);
        updatedLanguage
            .languageId(UPDATED_LANGUAGE_ID)
            .languageIsoCode(UPDATED_LANGUAGE_ISO_CODE)
            .languageName(UPDATED_LANGUAGE_NAME)
            .languageEnglishName(UPDATED_LANGUAGE_ENGLISH_NAME)
            .isActive(UPDATED_IS_ACTIVE)
            .sortOrder(UPDATED_SORT_ORDER);
        LanguageDTO languageDTO = languageMapper.toDto(updatedLanguage);

        restLanguageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, languageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(languageDTO))
            )
            .andExpect(status().isOk());

        // Validate the Language in the database
        List<Language> languageList = languageRepository.findAll();
        assertThat(languageList).hasSize(databaseSizeBeforeUpdate);
        Language testLanguage = languageList.get(languageList.size() - 1);
        assertThat(testLanguage.getLanguageId()).isEqualTo(UPDATED_LANGUAGE_ID);
        assertThat(testLanguage.getLanguageIsoCode()).isEqualTo(UPDATED_LANGUAGE_ISO_CODE);
        assertThat(testLanguage.getLanguageName()).isEqualTo(UPDATED_LANGUAGE_NAME);
        assertThat(testLanguage.getLanguageEnglishName()).isEqualTo(UPDATED_LANGUAGE_ENGLISH_NAME);
        assertThat(testLanguage.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testLanguage.getSortOrder()).isEqualTo(UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    void putNonExistingLanguage() throws Exception {
        int databaseSizeBeforeUpdate = languageRepository.findAll().size();
        language.setId(count.incrementAndGet());

        // Create the Language
        LanguageDTO languageDTO = languageMapper.toDto(language);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLanguageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, languageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(languageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Language in the database
        List<Language> languageList = languageRepository.findAll();
        assertThat(languageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLanguage() throws Exception {
        int databaseSizeBeforeUpdate = languageRepository.findAll().size();
        language.setId(count.incrementAndGet());

        // Create the Language
        LanguageDTO languageDTO = languageMapper.toDto(language);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLanguageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(languageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Language in the database
        List<Language> languageList = languageRepository.findAll();
        assertThat(languageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLanguage() throws Exception {
        int databaseSizeBeforeUpdate = languageRepository.findAll().size();
        language.setId(count.incrementAndGet());

        // Create the Language
        LanguageDTO languageDTO = languageMapper.toDto(language);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLanguageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(languageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Language in the database
        List<Language> languageList = languageRepository.findAll();
        assertThat(languageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLanguageWithPatch() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        int databaseSizeBeforeUpdate = languageRepository.findAll().size();

        // Update the language using partial update
        Language partialUpdatedLanguage = new Language();
        partialUpdatedLanguage.setId(language.getId());

        partialUpdatedLanguage
            .languageId(UPDATED_LANGUAGE_ID)
            .languageName(UPDATED_LANGUAGE_NAME)
            .languageEnglishName(UPDATED_LANGUAGE_ENGLISH_NAME)
            .isActive(UPDATED_IS_ACTIVE)
            .sortOrder(UPDATED_SORT_ORDER);

        restLanguageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLanguage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLanguage))
            )
            .andExpect(status().isOk());

        // Validate the Language in the database
        List<Language> languageList = languageRepository.findAll();
        assertThat(languageList).hasSize(databaseSizeBeforeUpdate);
        Language testLanguage = languageList.get(languageList.size() - 1);
        assertThat(testLanguage.getLanguageId()).isEqualTo(UPDATED_LANGUAGE_ID);
        assertThat(testLanguage.getLanguageIsoCode()).isEqualTo(DEFAULT_LANGUAGE_ISO_CODE);
        assertThat(testLanguage.getLanguageName()).isEqualTo(UPDATED_LANGUAGE_NAME);
        assertThat(testLanguage.getLanguageEnglishName()).isEqualTo(UPDATED_LANGUAGE_ENGLISH_NAME);
        assertThat(testLanguage.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testLanguage.getSortOrder()).isEqualTo(UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    void fullUpdateLanguageWithPatch() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        int databaseSizeBeforeUpdate = languageRepository.findAll().size();

        // Update the language using partial update
        Language partialUpdatedLanguage = new Language();
        partialUpdatedLanguage.setId(language.getId());

        partialUpdatedLanguage
            .languageId(UPDATED_LANGUAGE_ID)
            .languageIsoCode(UPDATED_LANGUAGE_ISO_CODE)
            .languageName(UPDATED_LANGUAGE_NAME)
            .languageEnglishName(UPDATED_LANGUAGE_ENGLISH_NAME)
            .isActive(UPDATED_IS_ACTIVE)
            .sortOrder(UPDATED_SORT_ORDER);

        restLanguageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLanguage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLanguage))
            )
            .andExpect(status().isOk());

        // Validate the Language in the database
        List<Language> languageList = languageRepository.findAll();
        assertThat(languageList).hasSize(databaseSizeBeforeUpdate);
        Language testLanguage = languageList.get(languageList.size() - 1);
        assertThat(testLanguage.getLanguageId()).isEqualTo(UPDATED_LANGUAGE_ID);
        assertThat(testLanguage.getLanguageIsoCode()).isEqualTo(UPDATED_LANGUAGE_ISO_CODE);
        assertThat(testLanguage.getLanguageName()).isEqualTo(UPDATED_LANGUAGE_NAME);
        assertThat(testLanguage.getLanguageEnglishName()).isEqualTo(UPDATED_LANGUAGE_ENGLISH_NAME);
        assertThat(testLanguage.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testLanguage.getSortOrder()).isEqualTo(UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    void patchNonExistingLanguage() throws Exception {
        int databaseSizeBeforeUpdate = languageRepository.findAll().size();
        language.setId(count.incrementAndGet());

        // Create the Language
        LanguageDTO languageDTO = languageMapper.toDto(language);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLanguageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, languageDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(languageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Language in the database
        List<Language> languageList = languageRepository.findAll();
        assertThat(languageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLanguage() throws Exception {
        int databaseSizeBeforeUpdate = languageRepository.findAll().size();
        language.setId(count.incrementAndGet());

        // Create the Language
        LanguageDTO languageDTO = languageMapper.toDto(language);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLanguageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(languageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Language in the database
        List<Language> languageList = languageRepository.findAll();
        assertThat(languageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLanguage() throws Exception {
        int databaseSizeBeforeUpdate = languageRepository.findAll().size();
        language.setId(count.incrementAndGet());

        // Create the Language
        LanguageDTO languageDTO = languageMapper.toDto(language);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLanguageMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(languageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Language in the database
        List<Language> languageList = languageRepository.findAll();
        assertThat(languageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLanguage() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        int databaseSizeBeforeDelete = languageRepository.findAll().size();

        // Delete the language
        restLanguageMockMvc
            .perform(delete(ENTITY_API_URL_ID, language.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Language> languageList = languageRepository.findAll();
        assertThat(languageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
