package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Continent;
import com.mycompany.myapp.domain.Country;
import com.mycompany.myapp.domain.Currency;
import com.mycompany.myapp.domain.Language;
import com.mycompany.myapp.repository.CountryRepository;
import com.mycompany.myapp.service.criteria.CountryCriteria;
import com.mycompany.myapp.service.dto.CountryDTO;
import com.mycompany.myapp.service.mapper.CountryMapper;
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
 * Integration tests for the {@link CountryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CountryResourceIT {

    private static final Integer DEFAULT_COUNTRY_ID = 1;
    private static final Integer UPDATED_COUNTRY_ID = 2;
    private static final Integer SMALLER_COUNTRY_ID = 1 - 1;

    private static final String DEFAULT_COUNTRY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY_ENGLISH_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY_ENGLISH_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY_ENGLISH_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY_ENGLISH_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY_ISO_CODE = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY_ISO_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NATIONALITY = "AAAAAAAAAA";
    private static final String UPDATED_NATIONALITY = "BBBBBBBBBB";

    private static final String DEFAULT_ENGLISH_NATIONALITY = "AAAAAAAAAA";
    private static final String UPDATED_ENGLISH_NATIONALITY = "BBBBBBBBBB";

    private static final Integer DEFAULT_IS_ACTIVE = 1;
    private static final Integer UPDATED_IS_ACTIVE = 2;
    private static final Integer SMALLER_IS_ACTIVE = 1 - 1;

    private static final Integer DEFAULT_SORT_ORDER = 1;
    private static final Integer UPDATED_SORT_ORDER = 2;
    private static final Integer SMALLER_SORT_ORDER = 1 - 1;

    private static final String ENTITY_API_URL = "/api/countries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CountryMapper countryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCountryMockMvc;

    private Country country;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Country createEntity(EntityManager em) {
        Country country = new Country()
            .countryId(DEFAULT_COUNTRY_ID)
            .countryName(DEFAULT_COUNTRY_NAME)
            .countryEnglishName(DEFAULT_COUNTRY_ENGLISH_NAME)
            .countryFullName(DEFAULT_COUNTRY_FULL_NAME)
            .countryEnglishFullName(DEFAULT_COUNTRY_ENGLISH_FULL_NAME)
            .countryIsoCode(DEFAULT_COUNTRY_ISO_CODE)
            .nationality(DEFAULT_NATIONALITY)
            .englishNationality(DEFAULT_ENGLISH_NATIONALITY)
            .isActive(DEFAULT_IS_ACTIVE)
            .sortOrder(DEFAULT_SORT_ORDER);
        return country;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Country createUpdatedEntity(EntityManager em) {
        Country country = new Country()
            .countryId(UPDATED_COUNTRY_ID)
            .countryName(UPDATED_COUNTRY_NAME)
            .countryEnglishName(UPDATED_COUNTRY_ENGLISH_NAME)
            .countryFullName(UPDATED_COUNTRY_FULL_NAME)
            .countryEnglishFullName(UPDATED_COUNTRY_ENGLISH_FULL_NAME)
            .countryIsoCode(UPDATED_COUNTRY_ISO_CODE)
            .nationality(UPDATED_NATIONALITY)
            .englishNationality(UPDATED_ENGLISH_NATIONALITY)
            .isActive(UPDATED_IS_ACTIVE)
            .sortOrder(UPDATED_SORT_ORDER);
        return country;
    }

    @BeforeEach
    public void initTest() {
        country = createEntity(em);
    }

    @Test
    @Transactional
    void createCountry() throws Exception {
        int databaseSizeBeforeCreate = countryRepository.findAll().size();
        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);
        restCountryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countryDTO)))
            .andExpect(status().isCreated());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeCreate + 1);
        Country testCountry = countryList.get(countryList.size() - 1);
        assertThat(testCountry.getCountryId()).isEqualTo(DEFAULT_COUNTRY_ID);
        assertThat(testCountry.getCountryName()).isEqualTo(DEFAULT_COUNTRY_NAME);
        assertThat(testCountry.getCountryEnglishName()).isEqualTo(DEFAULT_COUNTRY_ENGLISH_NAME);
        assertThat(testCountry.getCountryFullName()).isEqualTo(DEFAULT_COUNTRY_FULL_NAME);
        assertThat(testCountry.getCountryEnglishFullName()).isEqualTo(DEFAULT_COUNTRY_ENGLISH_FULL_NAME);
        assertThat(testCountry.getCountryIsoCode()).isEqualTo(DEFAULT_COUNTRY_ISO_CODE);
        assertThat(testCountry.getNationality()).isEqualTo(DEFAULT_NATIONALITY);
        assertThat(testCountry.getEnglishNationality()).isEqualTo(DEFAULT_ENGLISH_NATIONALITY);
        assertThat(testCountry.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testCountry.getSortOrder()).isEqualTo(DEFAULT_SORT_ORDER);
    }

    @Test
    @Transactional
    void createCountryWithExistingId() throws Exception {
        // Create the Country with an existing ID
        country.setId(1L);
        CountryDTO countryDTO = countryMapper.toDto(country);

        int databaseSizeBeforeCreate = countryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCountryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCountryIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = countryRepository.findAll().size();
        // set the field null
        country.setCountryId(null);

        // Create the Country, which fails.
        CountryDTO countryDTO = countryMapper.toDto(country);

        restCountryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countryDTO)))
            .andExpect(status().isBadRequest());

        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCountryNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = countryRepository.findAll().size();
        // set the field null
        country.setCountryName(null);

        // Create the Country, which fails.
        CountryDTO countryDTO = countryMapper.toDto(country);

        restCountryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countryDTO)))
            .andExpect(status().isBadRequest());

        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCountryIsoCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = countryRepository.findAll().size();
        // set the field null
        country.setCountryIsoCode(null);

        // Create the Country, which fails.
        CountryDTO countryDTO = countryMapper.toDto(country);

        restCountryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countryDTO)))
            .andExpect(status().isBadRequest());

        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCountries() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList
        restCountryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(country.getId().intValue())))
            .andExpect(jsonPath("$.[*].countryId").value(hasItem(DEFAULT_COUNTRY_ID)))
            .andExpect(jsonPath("$.[*].countryName").value(hasItem(DEFAULT_COUNTRY_NAME)))
            .andExpect(jsonPath("$.[*].countryEnglishName").value(hasItem(DEFAULT_COUNTRY_ENGLISH_NAME)))
            .andExpect(jsonPath("$.[*].countryFullName").value(hasItem(DEFAULT_COUNTRY_FULL_NAME)))
            .andExpect(jsonPath("$.[*].countryEnglishFullName").value(hasItem(DEFAULT_COUNTRY_ENGLISH_FULL_NAME)))
            .andExpect(jsonPath("$.[*].countryIsoCode").value(hasItem(DEFAULT_COUNTRY_ISO_CODE)))
            .andExpect(jsonPath("$.[*].nationality").value(hasItem(DEFAULT_NATIONALITY)))
            .andExpect(jsonPath("$.[*].englishNationality").value(hasItem(DEFAULT_ENGLISH_NATIONALITY)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)));
    }

    @Test
    @Transactional
    void getCountry() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get the country
        restCountryMockMvc
            .perform(get(ENTITY_API_URL_ID, country.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(country.getId().intValue()))
            .andExpect(jsonPath("$.countryId").value(DEFAULT_COUNTRY_ID))
            .andExpect(jsonPath("$.countryName").value(DEFAULT_COUNTRY_NAME))
            .andExpect(jsonPath("$.countryEnglishName").value(DEFAULT_COUNTRY_ENGLISH_NAME))
            .andExpect(jsonPath("$.countryFullName").value(DEFAULT_COUNTRY_FULL_NAME))
            .andExpect(jsonPath("$.countryEnglishFullName").value(DEFAULT_COUNTRY_ENGLISH_FULL_NAME))
            .andExpect(jsonPath("$.countryIsoCode").value(DEFAULT_COUNTRY_ISO_CODE))
            .andExpect(jsonPath("$.nationality").value(DEFAULT_NATIONALITY))
            .andExpect(jsonPath("$.englishNationality").value(DEFAULT_ENGLISH_NATIONALITY))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE))
            .andExpect(jsonPath("$.sortOrder").value(DEFAULT_SORT_ORDER));
    }

    @Test
    @Transactional
    void getCountriesByIdFiltering() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        Long id = country.getId();

        defaultCountryShouldBeFound("id.equals=" + id);
        defaultCountryShouldNotBeFound("id.notEquals=" + id);

        defaultCountryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCountryShouldNotBeFound("id.greaterThan=" + id);

        defaultCountryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCountryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCountriesByCountryIdIsEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryId equals to DEFAULT_COUNTRY_ID
        defaultCountryShouldBeFound("countryId.equals=" + DEFAULT_COUNTRY_ID);

        // Get all the countryList where countryId equals to UPDATED_COUNTRY_ID
        defaultCountryShouldNotBeFound("countryId.equals=" + UPDATED_COUNTRY_ID);
    }

    @Test
    @Transactional
    void getAllCountriesByCountryIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryId not equals to DEFAULT_COUNTRY_ID
        defaultCountryShouldNotBeFound("countryId.notEquals=" + DEFAULT_COUNTRY_ID);

        // Get all the countryList where countryId not equals to UPDATED_COUNTRY_ID
        defaultCountryShouldBeFound("countryId.notEquals=" + UPDATED_COUNTRY_ID);
    }

    @Test
    @Transactional
    void getAllCountriesByCountryIdIsInShouldWork() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryId in DEFAULT_COUNTRY_ID or UPDATED_COUNTRY_ID
        defaultCountryShouldBeFound("countryId.in=" + DEFAULT_COUNTRY_ID + "," + UPDATED_COUNTRY_ID);

        // Get all the countryList where countryId equals to UPDATED_COUNTRY_ID
        defaultCountryShouldNotBeFound("countryId.in=" + UPDATED_COUNTRY_ID);
    }

    @Test
    @Transactional
    void getAllCountriesByCountryIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryId is not null
        defaultCountryShouldBeFound("countryId.specified=true");

        // Get all the countryList where countryId is null
        defaultCountryShouldNotBeFound("countryId.specified=false");
    }

    @Test
    @Transactional
    void getAllCountriesByCountryIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryId is greater than or equal to DEFAULT_COUNTRY_ID
        defaultCountryShouldBeFound("countryId.greaterThanOrEqual=" + DEFAULT_COUNTRY_ID);

        // Get all the countryList where countryId is greater than or equal to UPDATED_COUNTRY_ID
        defaultCountryShouldNotBeFound("countryId.greaterThanOrEqual=" + UPDATED_COUNTRY_ID);
    }

    @Test
    @Transactional
    void getAllCountriesByCountryIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryId is less than or equal to DEFAULT_COUNTRY_ID
        defaultCountryShouldBeFound("countryId.lessThanOrEqual=" + DEFAULT_COUNTRY_ID);

        // Get all the countryList where countryId is less than or equal to SMALLER_COUNTRY_ID
        defaultCountryShouldNotBeFound("countryId.lessThanOrEqual=" + SMALLER_COUNTRY_ID);
    }

    @Test
    @Transactional
    void getAllCountriesByCountryIdIsLessThanSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryId is less than DEFAULT_COUNTRY_ID
        defaultCountryShouldNotBeFound("countryId.lessThan=" + DEFAULT_COUNTRY_ID);

        // Get all the countryList where countryId is less than UPDATED_COUNTRY_ID
        defaultCountryShouldBeFound("countryId.lessThan=" + UPDATED_COUNTRY_ID);
    }

    @Test
    @Transactional
    void getAllCountriesByCountryIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryId is greater than DEFAULT_COUNTRY_ID
        defaultCountryShouldNotBeFound("countryId.greaterThan=" + DEFAULT_COUNTRY_ID);

        // Get all the countryList where countryId is greater than SMALLER_COUNTRY_ID
        defaultCountryShouldBeFound("countryId.greaterThan=" + SMALLER_COUNTRY_ID);
    }

    @Test
    @Transactional
    void getAllCountriesByCountryNameIsEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryName equals to DEFAULT_COUNTRY_NAME
        defaultCountryShouldBeFound("countryName.equals=" + DEFAULT_COUNTRY_NAME);

        // Get all the countryList where countryName equals to UPDATED_COUNTRY_NAME
        defaultCountryShouldNotBeFound("countryName.equals=" + UPDATED_COUNTRY_NAME);
    }

    @Test
    @Transactional
    void getAllCountriesByCountryNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryName not equals to DEFAULT_COUNTRY_NAME
        defaultCountryShouldNotBeFound("countryName.notEquals=" + DEFAULT_COUNTRY_NAME);

        // Get all the countryList where countryName not equals to UPDATED_COUNTRY_NAME
        defaultCountryShouldBeFound("countryName.notEquals=" + UPDATED_COUNTRY_NAME);
    }

    @Test
    @Transactional
    void getAllCountriesByCountryNameIsInShouldWork() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryName in DEFAULT_COUNTRY_NAME or UPDATED_COUNTRY_NAME
        defaultCountryShouldBeFound("countryName.in=" + DEFAULT_COUNTRY_NAME + "," + UPDATED_COUNTRY_NAME);

        // Get all the countryList where countryName equals to UPDATED_COUNTRY_NAME
        defaultCountryShouldNotBeFound("countryName.in=" + UPDATED_COUNTRY_NAME);
    }

    @Test
    @Transactional
    void getAllCountriesByCountryNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryName is not null
        defaultCountryShouldBeFound("countryName.specified=true");

        // Get all the countryList where countryName is null
        defaultCountryShouldNotBeFound("countryName.specified=false");
    }

    @Test
    @Transactional
    void getAllCountriesByCountryNameContainsSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryName contains DEFAULT_COUNTRY_NAME
        defaultCountryShouldBeFound("countryName.contains=" + DEFAULT_COUNTRY_NAME);

        // Get all the countryList where countryName contains UPDATED_COUNTRY_NAME
        defaultCountryShouldNotBeFound("countryName.contains=" + UPDATED_COUNTRY_NAME);
    }

    @Test
    @Transactional
    void getAllCountriesByCountryNameNotContainsSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryName does not contain DEFAULT_COUNTRY_NAME
        defaultCountryShouldNotBeFound("countryName.doesNotContain=" + DEFAULT_COUNTRY_NAME);

        // Get all the countryList where countryName does not contain UPDATED_COUNTRY_NAME
        defaultCountryShouldBeFound("countryName.doesNotContain=" + UPDATED_COUNTRY_NAME);
    }

    @Test
    @Transactional
    void getAllCountriesByCountryEnglishNameIsEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryEnglishName equals to DEFAULT_COUNTRY_ENGLISH_NAME
        defaultCountryShouldBeFound("countryEnglishName.equals=" + DEFAULT_COUNTRY_ENGLISH_NAME);

        // Get all the countryList where countryEnglishName equals to UPDATED_COUNTRY_ENGLISH_NAME
        defaultCountryShouldNotBeFound("countryEnglishName.equals=" + UPDATED_COUNTRY_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void getAllCountriesByCountryEnglishNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryEnglishName not equals to DEFAULT_COUNTRY_ENGLISH_NAME
        defaultCountryShouldNotBeFound("countryEnglishName.notEquals=" + DEFAULT_COUNTRY_ENGLISH_NAME);

        // Get all the countryList where countryEnglishName not equals to UPDATED_COUNTRY_ENGLISH_NAME
        defaultCountryShouldBeFound("countryEnglishName.notEquals=" + UPDATED_COUNTRY_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void getAllCountriesByCountryEnglishNameIsInShouldWork() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryEnglishName in DEFAULT_COUNTRY_ENGLISH_NAME or UPDATED_COUNTRY_ENGLISH_NAME
        defaultCountryShouldBeFound("countryEnglishName.in=" + DEFAULT_COUNTRY_ENGLISH_NAME + "," + UPDATED_COUNTRY_ENGLISH_NAME);

        // Get all the countryList where countryEnglishName equals to UPDATED_COUNTRY_ENGLISH_NAME
        defaultCountryShouldNotBeFound("countryEnglishName.in=" + UPDATED_COUNTRY_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void getAllCountriesByCountryEnglishNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryEnglishName is not null
        defaultCountryShouldBeFound("countryEnglishName.specified=true");

        // Get all the countryList where countryEnglishName is null
        defaultCountryShouldNotBeFound("countryEnglishName.specified=false");
    }

    @Test
    @Transactional
    void getAllCountriesByCountryEnglishNameContainsSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryEnglishName contains DEFAULT_COUNTRY_ENGLISH_NAME
        defaultCountryShouldBeFound("countryEnglishName.contains=" + DEFAULT_COUNTRY_ENGLISH_NAME);

        // Get all the countryList where countryEnglishName contains UPDATED_COUNTRY_ENGLISH_NAME
        defaultCountryShouldNotBeFound("countryEnglishName.contains=" + UPDATED_COUNTRY_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void getAllCountriesByCountryEnglishNameNotContainsSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryEnglishName does not contain DEFAULT_COUNTRY_ENGLISH_NAME
        defaultCountryShouldNotBeFound("countryEnglishName.doesNotContain=" + DEFAULT_COUNTRY_ENGLISH_NAME);

        // Get all the countryList where countryEnglishName does not contain UPDATED_COUNTRY_ENGLISH_NAME
        defaultCountryShouldBeFound("countryEnglishName.doesNotContain=" + UPDATED_COUNTRY_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void getAllCountriesByCountryFullNameIsEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryFullName equals to DEFAULT_COUNTRY_FULL_NAME
        defaultCountryShouldBeFound("countryFullName.equals=" + DEFAULT_COUNTRY_FULL_NAME);

        // Get all the countryList where countryFullName equals to UPDATED_COUNTRY_FULL_NAME
        defaultCountryShouldNotBeFound("countryFullName.equals=" + UPDATED_COUNTRY_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllCountriesByCountryFullNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryFullName not equals to DEFAULT_COUNTRY_FULL_NAME
        defaultCountryShouldNotBeFound("countryFullName.notEquals=" + DEFAULT_COUNTRY_FULL_NAME);

        // Get all the countryList where countryFullName not equals to UPDATED_COUNTRY_FULL_NAME
        defaultCountryShouldBeFound("countryFullName.notEquals=" + UPDATED_COUNTRY_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllCountriesByCountryFullNameIsInShouldWork() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryFullName in DEFAULT_COUNTRY_FULL_NAME or UPDATED_COUNTRY_FULL_NAME
        defaultCountryShouldBeFound("countryFullName.in=" + DEFAULT_COUNTRY_FULL_NAME + "," + UPDATED_COUNTRY_FULL_NAME);

        // Get all the countryList where countryFullName equals to UPDATED_COUNTRY_FULL_NAME
        defaultCountryShouldNotBeFound("countryFullName.in=" + UPDATED_COUNTRY_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllCountriesByCountryFullNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryFullName is not null
        defaultCountryShouldBeFound("countryFullName.specified=true");

        // Get all the countryList where countryFullName is null
        defaultCountryShouldNotBeFound("countryFullName.specified=false");
    }

    @Test
    @Transactional
    void getAllCountriesByCountryFullNameContainsSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryFullName contains DEFAULT_COUNTRY_FULL_NAME
        defaultCountryShouldBeFound("countryFullName.contains=" + DEFAULT_COUNTRY_FULL_NAME);

        // Get all the countryList where countryFullName contains UPDATED_COUNTRY_FULL_NAME
        defaultCountryShouldNotBeFound("countryFullName.contains=" + UPDATED_COUNTRY_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllCountriesByCountryFullNameNotContainsSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryFullName does not contain DEFAULT_COUNTRY_FULL_NAME
        defaultCountryShouldNotBeFound("countryFullName.doesNotContain=" + DEFAULT_COUNTRY_FULL_NAME);

        // Get all the countryList where countryFullName does not contain UPDATED_COUNTRY_FULL_NAME
        defaultCountryShouldBeFound("countryFullName.doesNotContain=" + UPDATED_COUNTRY_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllCountriesByCountryEnglishFullNameIsEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryEnglishFullName equals to DEFAULT_COUNTRY_ENGLISH_FULL_NAME
        defaultCountryShouldBeFound("countryEnglishFullName.equals=" + DEFAULT_COUNTRY_ENGLISH_FULL_NAME);

        // Get all the countryList where countryEnglishFullName equals to UPDATED_COUNTRY_ENGLISH_FULL_NAME
        defaultCountryShouldNotBeFound("countryEnglishFullName.equals=" + UPDATED_COUNTRY_ENGLISH_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllCountriesByCountryEnglishFullNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryEnglishFullName not equals to DEFAULT_COUNTRY_ENGLISH_FULL_NAME
        defaultCountryShouldNotBeFound("countryEnglishFullName.notEquals=" + DEFAULT_COUNTRY_ENGLISH_FULL_NAME);

        // Get all the countryList where countryEnglishFullName not equals to UPDATED_COUNTRY_ENGLISH_FULL_NAME
        defaultCountryShouldBeFound("countryEnglishFullName.notEquals=" + UPDATED_COUNTRY_ENGLISH_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllCountriesByCountryEnglishFullNameIsInShouldWork() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryEnglishFullName in DEFAULT_COUNTRY_ENGLISH_FULL_NAME or UPDATED_COUNTRY_ENGLISH_FULL_NAME
        defaultCountryShouldBeFound(
            "countryEnglishFullName.in=" + DEFAULT_COUNTRY_ENGLISH_FULL_NAME + "," + UPDATED_COUNTRY_ENGLISH_FULL_NAME
        );

        // Get all the countryList where countryEnglishFullName equals to UPDATED_COUNTRY_ENGLISH_FULL_NAME
        defaultCountryShouldNotBeFound("countryEnglishFullName.in=" + UPDATED_COUNTRY_ENGLISH_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllCountriesByCountryEnglishFullNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryEnglishFullName is not null
        defaultCountryShouldBeFound("countryEnglishFullName.specified=true");

        // Get all the countryList where countryEnglishFullName is null
        defaultCountryShouldNotBeFound("countryEnglishFullName.specified=false");
    }

    @Test
    @Transactional
    void getAllCountriesByCountryEnglishFullNameContainsSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryEnglishFullName contains DEFAULT_COUNTRY_ENGLISH_FULL_NAME
        defaultCountryShouldBeFound("countryEnglishFullName.contains=" + DEFAULT_COUNTRY_ENGLISH_FULL_NAME);

        // Get all the countryList where countryEnglishFullName contains UPDATED_COUNTRY_ENGLISH_FULL_NAME
        defaultCountryShouldNotBeFound("countryEnglishFullName.contains=" + UPDATED_COUNTRY_ENGLISH_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllCountriesByCountryEnglishFullNameNotContainsSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryEnglishFullName does not contain DEFAULT_COUNTRY_ENGLISH_FULL_NAME
        defaultCountryShouldNotBeFound("countryEnglishFullName.doesNotContain=" + DEFAULT_COUNTRY_ENGLISH_FULL_NAME);

        // Get all the countryList where countryEnglishFullName does not contain UPDATED_COUNTRY_ENGLISH_FULL_NAME
        defaultCountryShouldBeFound("countryEnglishFullName.doesNotContain=" + UPDATED_COUNTRY_ENGLISH_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllCountriesByCountryIsoCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryIsoCode equals to DEFAULT_COUNTRY_ISO_CODE
        defaultCountryShouldBeFound("countryIsoCode.equals=" + DEFAULT_COUNTRY_ISO_CODE);

        // Get all the countryList where countryIsoCode equals to UPDATED_COUNTRY_ISO_CODE
        defaultCountryShouldNotBeFound("countryIsoCode.equals=" + UPDATED_COUNTRY_ISO_CODE);
    }

    @Test
    @Transactional
    void getAllCountriesByCountryIsoCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryIsoCode not equals to DEFAULT_COUNTRY_ISO_CODE
        defaultCountryShouldNotBeFound("countryIsoCode.notEquals=" + DEFAULT_COUNTRY_ISO_CODE);

        // Get all the countryList where countryIsoCode not equals to UPDATED_COUNTRY_ISO_CODE
        defaultCountryShouldBeFound("countryIsoCode.notEquals=" + UPDATED_COUNTRY_ISO_CODE);
    }

    @Test
    @Transactional
    void getAllCountriesByCountryIsoCodeIsInShouldWork() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryIsoCode in DEFAULT_COUNTRY_ISO_CODE or UPDATED_COUNTRY_ISO_CODE
        defaultCountryShouldBeFound("countryIsoCode.in=" + DEFAULT_COUNTRY_ISO_CODE + "," + UPDATED_COUNTRY_ISO_CODE);

        // Get all the countryList where countryIsoCode equals to UPDATED_COUNTRY_ISO_CODE
        defaultCountryShouldNotBeFound("countryIsoCode.in=" + UPDATED_COUNTRY_ISO_CODE);
    }

    @Test
    @Transactional
    void getAllCountriesByCountryIsoCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryIsoCode is not null
        defaultCountryShouldBeFound("countryIsoCode.specified=true");

        // Get all the countryList where countryIsoCode is null
        defaultCountryShouldNotBeFound("countryIsoCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCountriesByCountryIsoCodeContainsSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryIsoCode contains DEFAULT_COUNTRY_ISO_CODE
        defaultCountryShouldBeFound("countryIsoCode.contains=" + DEFAULT_COUNTRY_ISO_CODE);

        // Get all the countryList where countryIsoCode contains UPDATED_COUNTRY_ISO_CODE
        defaultCountryShouldNotBeFound("countryIsoCode.contains=" + UPDATED_COUNTRY_ISO_CODE);
    }

    @Test
    @Transactional
    void getAllCountriesByCountryIsoCodeNotContainsSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryIsoCode does not contain DEFAULT_COUNTRY_ISO_CODE
        defaultCountryShouldNotBeFound("countryIsoCode.doesNotContain=" + DEFAULT_COUNTRY_ISO_CODE);

        // Get all the countryList where countryIsoCode does not contain UPDATED_COUNTRY_ISO_CODE
        defaultCountryShouldBeFound("countryIsoCode.doesNotContain=" + UPDATED_COUNTRY_ISO_CODE);
    }

    @Test
    @Transactional
    void getAllCountriesByNationalityIsEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where nationality equals to DEFAULT_NATIONALITY
        defaultCountryShouldBeFound("nationality.equals=" + DEFAULT_NATIONALITY);

        // Get all the countryList where nationality equals to UPDATED_NATIONALITY
        defaultCountryShouldNotBeFound("nationality.equals=" + UPDATED_NATIONALITY);
    }

    @Test
    @Transactional
    void getAllCountriesByNationalityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where nationality not equals to DEFAULT_NATIONALITY
        defaultCountryShouldNotBeFound("nationality.notEquals=" + DEFAULT_NATIONALITY);

        // Get all the countryList where nationality not equals to UPDATED_NATIONALITY
        defaultCountryShouldBeFound("nationality.notEquals=" + UPDATED_NATIONALITY);
    }

    @Test
    @Transactional
    void getAllCountriesByNationalityIsInShouldWork() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where nationality in DEFAULT_NATIONALITY or UPDATED_NATIONALITY
        defaultCountryShouldBeFound("nationality.in=" + DEFAULT_NATIONALITY + "," + UPDATED_NATIONALITY);

        // Get all the countryList where nationality equals to UPDATED_NATIONALITY
        defaultCountryShouldNotBeFound("nationality.in=" + UPDATED_NATIONALITY);
    }

    @Test
    @Transactional
    void getAllCountriesByNationalityIsNullOrNotNull() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where nationality is not null
        defaultCountryShouldBeFound("nationality.specified=true");

        // Get all the countryList where nationality is null
        defaultCountryShouldNotBeFound("nationality.specified=false");
    }

    @Test
    @Transactional
    void getAllCountriesByNationalityContainsSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where nationality contains DEFAULT_NATIONALITY
        defaultCountryShouldBeFound("nationality.contains=" + DEFAULT_NATIONALITY);

        // Get all the countryList where nationality contains UPDATED_NATIONALITY
        defaultCountryShouldNotBeFound("nationality.contains=" + UPDATED_NATIONALITY);
    }

    @Test
    @Transactional
    void getAllCountriesByNationalityNotContainsSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where nationality does not contain DEFAULT_NATIONALITY
        defaultCountryShouldNotBeFound("nationality.doesNotContain=" + DEFAULT_NATIONALITY);

        // Get all the countryList where nationality does not contain UPDATED_NATIONALITY
        defaultCountryShouldBeFound("nationality.doesNotContain=" + UPDATED_NATIONALITY);
    }

    @Test
    @Transactional
    void getAllCountriesByEnglishNationalityIsEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where englishNationality equals to DEFAULT_ENGLISH_NATIONALITY
        defaultCountryShouldBeFound("englishNationality.equals=" + DEFAULT_ENGLISH_NATIONALITY);

        // Get all the countryList where englishNationality equals to UPDATED_ENGLISH_NATIONALITY
        defaultCountryShouldNotBeFound("englishNationality.equals=" + UPDATED_ENGLISH_NATIONALITY);
    }

    @Test
    @Transactional
    void getAllCountriesByEnglishNationalityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where englishNationality not equals to DEFAULT_ENGLISH_NATIONALITY
        defaultCountryShouldNotBeFound("englishNationality.notEquals=" + DEFAULT_ENGLISH_NATIONALITY);

        // Get all the countryList where englishNationality not equals to UPDATED_ENGLISH_NATIONALITY
        defaultCountryShouldBeFound("englishNationality.notEquals=" + UPDATED_ENGLISH_NATIONALITY);
    }

    @Test
    @Transactional
    void getAllCountriesByEnglishNationalityIsInShouldWork() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where englishNationality in DEFAULT_ENGLISH_NATIONALITY or UPDATED_ENGLISH_NATIONALITY
        defaultCountryShouldBeFound("englishNationality.in=" + DEFAULT_ENGLISH_NATIONALITY + "," + UPDATED_ENGLISH_NATIONALITY);

        // Get all the countryList where englishNationality equals to UPDATED_ENGLISH_NATIONALITY
        defaultCountryShouldNotBeFound("englishNationality.in=" + UPDATED_ENGLISH_NATIONALITY);
    }

    @Test
    @Transactional
    void getAllCountriesByEnglishNationalityIsNullOrNotNull() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where englishNationality is not null
        defaultCountryShouldBeFound("englishNationality.specified=true");

        // Get all the countryList where englishNationality is null
        defaultCountryShouldNotBeFound("englishNationality.specified=false");
    }

    @Test
    @Transactional
    void getAllCountriesByEnglishNationalityContainsSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where englishNationality contains DEFAULT_ENGLISH_NATIONALITY
        defaultCountryShouldBeFound("englishNationality.contains=" + DEFAULT_ENGLISH_NATIONALITY);

        // Get all the countryList where englishNationality contains UPDATED_ENGLISH_NATIONALITY
        defaultCountryShouldNotBeFound("englishNationality.contains=" + UPDATED_ENGLISH_NATIONALITY);
    }

    @Test
    @Transactional
    void getAllCountriesByEnglishNationalityNotContainsSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where englishNationality does not contain DEFAULT_ENGLISH_NATIONALITY
        defaultCountryShouldNotBeFound("englishNationality.doesNotContain=" + DEFAULT_ENGLISH_NATIONALITY);

        // Get all the countryList where englishNationality does not contain UPDATED_ENGLISH_NATIONALITY
        defaultCountryShouldBeFound("englishNationality.doesNotContain=" + UPDATED_ENGLISH_NATIONALITY);
    }

    @Test
    @Transactional
    void getAllCountriesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where isActive equals to DEFAULT_IS_ACTIVE
        defaultCountryShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the countryList where isActive equals to UPDATED_IS_ACTIVE
        defaultCountryShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllCountriesByIsActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where isActive not equals to DEFAULT_IS_ACTIVE
        defaultCountryShouldNotBeFound("isActive.notEquals=" + DEFAULT_IS_ACTIVE);

        // Get all the countryList where isActive not equals to UPDATED_IS_ACTIVE
        defaultCountryShouldBeFound("isActive.notEquals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllCountriesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultCountryShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the countryList where isActive equals to UPDATED_IS_ACTIVE
        defaultCountryShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllCountriesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where isActive is not null
        defaultCountryShouldBeFound("isActive.specified=true");

        // Get all the countryList where isActive is null
        defaultCountryShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllCountriesByIsActiveIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where isActive is greater than or equal to DEFAULT_IS_ACTIVE
        defaultCountryShouldBeFound("isActive.greaterThanOrEqual=" + DEFAULT_IS_ACTIVE);

        // Get all the countryList where isActive is greater than or equal to UPDATED_IS_ACTIVE
        defaultCountryShouldNotBeFound("isActive.greaterThanOrEqual=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllCountriesByIsActiveIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where isActive is less than or equal to DEFAULT_IS_ACTIVE
        defaultCountryShouldBeFound("isActive.lessThanOrEqual=" + DEFAULT_IS_ACTIVE);

        // Get all the countryList where isActive is less than or equal to SMALLER_IS_ACTIVE
        defaultCountryShouldNotBeFound("isActive.lessThanOrEqual=" + SMALLER_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllCountriesByIsActiveIsLessThanSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where isActive is less than DEFAULT_IS_ACTIVE
        defaultCountryShouldNotBeFound("isActive.lessThan=" + DEFAULT_IS_ACTIVE);

        // Get all the countryList where isActive is less than UPDATED_IS_ACTIVE
        defaultCountryShouldBeFound("isActive.lessThan=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllCountriesByIsActiveIsGreaterThanSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where isActive is greater than DEFAULT_IS_ACTIVE
        defaultCountryShouldNotBeFound("isActive.greaterThan=" + DEFAULT_IS_ACTIVE);

        // Get all the countryList where isActive is greater than SMALLER_IS_ACTIVE
        defaultCountryShouldBeFound("isActive.greaterThan=" + SMALLER_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllCountriesBySortOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where sortOrder equals to DEFAULT_SORT_ORDER
        defaultCountryShouldBeFound("sortOrder.equals=" + DEFAULT_SORT_ORDER);

        // Get all the countryList where sortOrder equals to UPDATED_SORT_ORDER
        defaultCountryShouldNotBeFound("sortOrder.equals=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    void getAllCountriesBySortOrderIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where sortOrder not equals to DEFAULT_SORT_ORDER
        defaultCountryShouldNotBeFound("sortOrder.notEquals=" + DEFAULT_SORT_ORDER);

        // Get all the countryList where sortOrder not equals to UPDATED_SORT_ORDER
        defaultCountryShouldBeFound("sortOrder.notEquals=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    void getAllCountriesBySortOrderIsInShouldWork() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where sortOrder in DEFAULT_SORT_ORDER or UPDATED_SORT_ORDER
        defaultCountryShouldBeFound("sortOrder.in=" + DEFAULT_SORT_ORDER + "," + UPDATED_SORT_ORDER);

        // Get all the countryList where sortOrder equals to UPDATED_SORT_ORDER
        defaultCountryShouldNotBeFound("sortOrder.in=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    void getAllCountriesBySortOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where sortOrder is not null
        defaultCountryShouldBeFound("sortOrder.specified=true");

        // Get all the countryList where sortOrder is null
        defaultCountryShouldNotBeFound("sortOrder.specified=false");
    }

    @Test
    @Transactional
    void getAllCountriesBySortOrderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where sortOrder is greater than or equal to DEFAULT_SORT_ORDER
        defaultCountryShouldBeFound("sortOrder.greaterThanOrEqual=" + DEFAULT_SORT_ORDER);

        // Get all the countryList where sortOrder is greater than or equal to UPDATED_SORT_ORDER
        defaultCountryShouldNotBeFound("sortOrder.greaterThanOrEqual=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    void getAllCountriesBySortOrderIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where sortOrder is less than or equal to DEFAULT_SORT_ORDER
        defaultCountryShouldBeFound("sortOrder.lessThanOrEqual=" + DEFAULT_SORT_ORDER);

        // Get all the countryList where sortOrder is less than or equal to SMALLER_SORT_ORDER
        defaultCountryShouldNotBeFound("sortOrder.lessThanOrEqual=" + SMALLER_SORT_ORDER);
    }

    @Test
    @Transactional
    void getAllCountriesBySortOrderIsLessThanSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where sortOrder is less than DEFAULT_SORT_ORDER
        defaultCountryShouldNotBeFound("sortOrder.lessThan=" + DEFAULT_SORT_ORDER);

        // Get all the countryList where sortOrder is less than UPDATED_SORT_ORDER
        defaultCountryShouldBeFound("sortOrder.lessThan=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    void getAllCountriesBySortOrderIsGreaterThanSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where sortOrder is greater than DEFAULT_SORT_ORDER
        defaultCountryShouldNotBeFound("sortOrder.greaterThan=" + DEFAULT_SORT_ORDER);

        // Get all the countryList where sortOrder is greater than SMALLER_SORT_ORDER
        defaultCountryShouldBeFound("sortOrder.greaterThan=" + SMALLER_SORT_ORDER);
    }

    @Test
    @Transactional
    void getAllCountriesByContinentIsEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);
        Continent continent;
        if (TestUtil.findAll(em, Continent.class).isEmpty()) {
            continent = ContinentResourceIT.createEntity(em);
            em.persist(continent);
            em.flush();
        } else {
            continent = TestUtil.findAll(em, Continent.class).get(0);
        }
        em.persist(continent);
        em.flush();
        country.setContinent(continent);
        countryRepository.saveAndFlush(country);
        Long continentId = continent.getId();

        // Get all the countryList where continent equals to continentId
        defaultCountryShouldBeFound("continentId.equals=" + continentId);

        // Get all the countryList where continent equals to (continentId + 1)
        defaultCountryShouldNotBeFound("continentId.equals=" + (continentId + 1));
    }

    @Test
    @Transactional
    void getAllCountriesByCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);
        Currency currency;
        if (TestUtil.findAll(em, Currency.class).isEmpty()) {
            currency = CurrencyResourceIT.createEntity(em);
            em.persist(currency);
            em.flush();
        } else {
            currency = TestUtil.findAll(em, Currency.class).get(0);
        }
        em.persist(currency);
        em.flush();
        country.setCurrency(currency);
        countryRepository.saveAndFlush(country);
        Long currencyId = currency.getId();

        // Get all the countryList where currency equals to currencyId
        defaultCountryShouldBeFound("currencyId.equals=" + currencyId);

        // Get all the countryList where currency equals to (currencyId + 1)
        defaultCountryShouldNotBeFound("currencyId.equals=" + (currencyId + 1));
    }

    @Test
    @Transactional
    void getAllCountriesByOfficialLanguageIsEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);
        Language officialLanguage;
        if (TestUtil.findAll(em, Language.class).isEmpty()) {
            officialLanguage = LanguageResourceIT.createEntity(em);
            em.persist(officialLanguage);
            em.flush();
        } else {
            officialLanguage = TestUtil.findAll(em, Language.class).get(0);
        }
        em.persist(officialLanguage);
        em.flush();
        country.setOfficialLanguage(officialLanguage);
        countryRepository.saveAndFlush(country);
        Long officialLanguageId = officialLanguage.getId();

        // Get all the countryList where officialLanguage equals to officialLanguageId
        defaultCountryShouldBeFound("officialLanguageId.equals=" + officialLanguageId);

        // Get all the countryList where officialLanguage equals to (officialLanguageId + 1)
        defaultCountryShouldNotBeFound("officialLanguageId.equals=" + (officialLanguageId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCountryShouldBeFound(String filter) throws Exception {
        restCountryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(country.getId().intValue())))
            .andExpect(jsonPath("$.[*].countryId").value(hasItem(DEFAULT_COUNTRY_ID)))
            .andExpect(jsonPath("$.[*].countryName").value(hasItem(DEFAULT_COUNTRY_NAME)))
            .andExpect(jsonPath("$.[*].countryEnglishName").value(hasItem(DEFAULT_COUNTRY_ENGLISH_NAME)))
            .andExpect(jsonPath("$.[*].countryFullName").value(hasItem(DEFAULT_COUNTRY_FULL_NAME)))
            .andExpect(jsonPath("$.[*].countryEnglishFullName").value(hasItem(DEFAULT_COUNTRY_ENGLISH_FULL_NAME)))
            .andExpect(jsonPath("$.[*].countryIsoCode").value(hasItem(DEFAULT_COUNTRY_ISO_CODE)))
            .andExpect(jsonPath("$.[*].nationality").value(hasItem(DEFAULT_NATIONALITY)))
            .andExpect(jsonPath("$.[*].englishNationality").value(hasItem(DEFAULT_ENGLISH_NATIONALITY)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)));

        // Check, that the count call also returns 1
        restCountryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCountryShouldNotBeFound(String filter) throws Exception {
        restCountryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCountryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCountry() throws Exception {
        // Get the country
        restCountryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCountry() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        int databaseSizeBeforeUpdate = countryRepository.findAll().size();

        // Update the country
        Country updatedCountry = countryRepository.findById(country.getId()).get();
        // Disconnect from session so that the updates on updatedCountry are not directly saved in db
        em.detach(updatedCountry);
        updatedCountry
            .countryId(UPDATED_COUNTRY_ID)
            .countryName(UPDATED_COUNTRY_NAME)
            .countryEnglishName(UPDATED_COUNTRY_ENGLISH_NAME)
            .countryFullName(UPDATED_COUNTRY_FULL_NAME)
            .countryEnglishFullName(UPDATED_COUNTRY_ENGLISH_FULL_NAME)
            .countryIsoCode(UPDATED_COUNTRY_ISO_CODE)
            .nationality(UPDATED_NATIONALITY)
            .englishNationality(UPDATED_ENGLISH_NATIONALITY)
            .isActive(UPDATED_IS_ACTIVE)
            .sortOrder(UPDATED_SORT_ORDER);
        CountryDTO countryDTO = countryMapper.toDto(updatedCountry);

        restCountryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, countryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countryDTO))
            )
            .andExpect(status().isOk());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
        Country testCountry = countryList.get(countryList.size() - 1);
        assertThat(testCountry.getCountryId()).isEqualTo(UPDATED_COUNTRY_ID);
        assertThat(testCountry.getCountryName()).isEqualTo(UPDATED_COUNTRY_NAME);
        assertThat(testCountry.getCountryEnglishName()).isEqualTo(UPDATED_COUNTRY_ENGLISH_NAME);
        assertThat(testCountry.getCountryFullName()).isEqualTo(UPDATED_COUNTRY_FULL_NAME);
        assertThat(testCountry.getCountryEnglishFullName()).isEqualTo(UPDATED_COUNTRY_ENGLISH_FULL_NAME);
        assertThat(testCountry.getCountryIsoCode()).isEqualTo(UPDATED_COUNTRY_ISO_CODE);
        assertThat(testCountry.getNationality()).isEqualTo(UPDATED_NATIONALITY);
        assertThat(testCountry.getEnglishNationality()).isEqualTo(UPDATED_ENGLISH_NATIONALITY);
        assertThat(testCountry.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testCountry.getSortOrder()).isEqualTo(UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    void putNonExistingCountry() throws Exception {
        int databaseSizeBeforeUpdate = countryRepository.findAll().size();
        country.setId(count.incrementAndGet());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCountryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, countryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCountry() throws Exception {
        int databaseSizeBeforeUpdate = countryRepository.findAll().size();
        country.setId(count.incrementAndGet());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCountry() throws Exception {
        int databaseSizeBeforeUpdate = countryRepository.findAll().size();
        country.setId(count.incrementAndGet());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCountryWithPatch() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        int databaseSizeBeforeUpdate = countryRepository.findAll().size();

        // Update the country using partial update
        Country partialUpdatedCountry = new Country();
        partialUpdatedCountry.setId(country.getId());

        partialUpdatedCountry
            .countryId(UPDATED_COUNTRY_ID)
            .countryName(UPDATED_COUNTRY_NAME)
            .countryEnglishName(UPDATED_COUNTRY_ENGLISH_NAME)
            .countryFullName(UPDATED_COUNTRY_FULL_NAME)
            .nationality(UPDATED_NATIONALITY)
            .englishNationality(UPDATED_ENGLISH_NATIONALITY)
            .isActive(UPDATED_IS_ACTIVE);

        restCountryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCountry.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCountry))
            )
            .andExpect(status().isOk());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
        Country testCountry = countryList.get(countryList.size() - 1);
        assertThat(testCountry.getCountryId()).isEqualTo(UPDATED_COUNTRY_ID);
        assertThat(testCountry.getCountryName()).isEqualTo(UPDATED_COUNTRY_NAME);
        assertThat(testCountry.getCountryEnglishName()).isEqualTo(UPDATED_COUNTRY_ENGLISH_NAME);
        assertThat(testCountry.getCountryFullName()).isEqualTo(UPDATED_COUNTRY_FULL_NAME);
        assertThat(testCountry.getCountryEnglishFullName()).isEqualTo(DEFAULT_COUNTRY_ENGLISH_FULL_NAME);
        assertThat(testCountry.getCountryIsoCode()).isEqualTo(DEFAULT_COUNTRY_ISO_CODE);
        assertThat(testCountry.getNationality()).isEqualTo(UPDATED_NATIONALITY);
        assertThat(testCountry.getEnglishNationality()).isEqualTo(UPDATED_ENGLISH_NATIONALITY);
        assertThat(testCountry.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testCountry.getSortOrder()).isEqualTo(DEFAULT_SORT_ORDER);
    }

    @Test
    @Transactional
    void fullUpdateCountryWithPatch() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        int databaseSizeBeforeUpdate = countryRepository.findAll().size();

        // Update the country using partial update
        Country partialUpdatedCountry = new Country();
        partialUpdatedCountry.setId(country.getId());

        partialUpdatedCountry
            .countryId(UPDATED_COUNTRY_ID)
            .countryName(UPDATED_COUNTRY_NAME)
            .countryEnglishName(UPDATED_COUNTRY_ENGLISH_NAME)
            .countryFullName(UPDATED_COUNTRY_FULL_NAME)
            .countryEnglishFullName(UPDATED_COUNTRY_ENGLISH_FULL_NAME)
            .countryIsoCode(UPDATED_COUNTRY_ISO_CODE)
            .nationality(UPDATED_NATIONALITY)
            .englishNationality(UPDATED_ENGLISH_NATIONALITY)
            .isActive(UPDATED_IS_ACTIVE)
            .sortOrder(UPDATED_SORT_ORDER);

        restCountryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCountry.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCountry))
            )
            .andExpect(status().isOk());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
        Country testCountry = countryList.get(countryList.size() - 1);
        assertThat(testCountry.getCountryId()).isEqualTo(UPDATED_COUNTRY_ID);
        assertThat(testCountry.getCountryName()).isEqualTo(UPDATED_COUNTRY_NAME);
        assertThat(testCountry.getCountryEnglishName()).isEqualTo(UPDATED_COUNTRY_ENGLISH_NAME);
        assertThat(testCountry.getCountryFullName()).isEqualTo(UPDATED_COUNTRY_FULL_NAME);
        assertThat(testCountry.getCountryEnglishFullName()).isEqualTo(UPDATED_COUNTRY_ENGLISH_FULL_NAME);
        assertThat(testCountry.getCountryIsoCode()).isEqualTo(UPDATED_COUNTRY_ISO_CODE);
        assertThat(testCountry.getNationality()).isEqualTo(UPDATED_NATIONALITY);
        assertThat(testCountry.getEnglishNationality()).isEqualTo(UPDATED_ENGLISH_NATIONALITY);
        assertThat(testCountry.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testCountry.getSortOrder()).isEqualTo(UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    void patchNonExistingCountry() throws Exception {
        int databaseSizeBeforeUpdate = countryRepository.findAll().size();
        country.setId(count.incrementAndGet());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCountryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, countryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(countryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCountry() throws Exception {
        int databaseSizeBeforeUpdate = countryRepository.findAll().size();
        country.setId(count.incrementAndGet());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(countryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCountry() throws Exception {
        int databaseSizeBeforeUpdate = countryRepository.findAll().size();
        country.setId(count.incrementAndGet());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(countryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCountry() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        int databaseSizeBeforeDelete = countryRepository.findAll().size();

        // Delete the country
        restCountryMockMvc
            .perform(delete(ENTITY_API_URL_ID, country.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
