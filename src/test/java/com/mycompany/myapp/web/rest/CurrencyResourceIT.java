package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Currency;
import com.mycompany.myapp.repository.CurrencyRepository;
import com.mycompany.myapp.service.criteria.CurrencyCriteria;
import com.mycompany.myapp.service.dto.CurrencyDTO;
import com.mycompany.myapp.service.mapper.CurrencyMapper;
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
 * Integration tests for the {@link CurrencyResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CurrencyResourceIT {

    private static final Integer DEFAULT_CURRENCY_ID = 1;
    private static final Integer UPDATED_CURRENCY_ID = 2;
    private static final Integer SMALLER_CURRENCY_ID = 1 - 1;

    private static final String DEFAULT_CURRENCY_ALPHABETIC_ISO = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY_ALPHABETIC_ISO = "BBBBBBBBBB";

    private static final String DEFAULT_CURRENCY_NUMERIC_ISO = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY_NUMERIC_ISO = "BBBBBBBBBB";

    private static final String DEFAULT_CURRENCY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CURRENCY_ENGLISH_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY_ENGLISH_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CURRENCY_SYMBOL = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY_SYMBOL = "BBBBBBBBBB";

    private static final Integer DEFAULT_FLOATING_POINT = 1;
    private static final Integer UPDATED_FLOATING_POINT = 2;
    private static final Integer SMALLER_FLOATING_POINT = 1 - 1;

    private static final Integer DEFAULT_IS_BASE_CURRENCY = 1;
    private static final Integer UPDATED_IS_BASE_CURRENCY = 2;
    private static final Integer SMALLER_IS_BASE_CURRENCY = 1 - 1;

    private static final Integer DEFAULT_IS_DEFAULT_CURRENCY = 1;
    private static final Integer UPDATED_IS_DEFAULT_CURRENCY = 2;
    private static final Integer SMALLER_IS_DEFAULT_CURRENCY = 1 - 1;

    private static final Integer DEFAULT_CONVERSION_METHOD = 1;
    private static final Integer UPDATED_CONVERSION_METHOD = 2;
    private static final Integer SMALLER_CONVERSION_METHOD = 1 - 1;

    private static final Integer DEFAULT_IS_ACTIVE = 1;
    private static final Integer UPDATED_IS_ACTIVE = 2;
    private static final Integer SMALLER_IS_ACTIVE = 1 - 1;

    private static final Integer DEFAULT_SORT_ORDER = 1;
    private static final Integer UPDATED_SORT_ORDER = 2;
    private static final Integer SMALLER_SORT_ORDER = 1 - 1;

    private static final String ENTITY_API_URL = "/api/currencies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private CurrencyMapper currencyMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCurrencyMockMvc;

    private Currency currency;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Currency createEntity(EntityManager em) {
        Currency currency = new Currency()
            .currencyId(DEFAULT_CURRENCY_ID)
            .currencyAlphabeticIso(DEFAULT_CURRENCY_ALPHABETIC_ISO)
            .currencyNumericIso(DEFAULT_CURRENCY_NUMERIC_ISO)
            .currencyName(DEFAULT_CURRENCY_NAME)
            .currencyEnglishName(DEFAULT_CURRENCY_ENGLISH_NAME)
            .currencySymbol(DEFAULT_CURRENCY_SYMBOL)
            .floatingPoint(DEFAULT_FLOATING_POINT)
            .isBaseCurrency(DEFAULT_IS_BASE_CURRENCY)
            .isDefaultCurrency(DEFAULT_IS_DEFAULT_CURRENCY)
            .conversionMethod(DEFAULT_CONVERSION_METHOD)
            .isActive(DEFAULT_IS_ACTIVE)
            .sortOrder(DEFAULT_SORT_ORDER);
        return currency;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Currency createUpdatedEntity(EntityManager em) {
        Currency currency = new Currency()
            .currencyId(UPDATED_CURRENCY_ID)
            .currencyAlphabeticIso(UPDATED_CURRENCY_ALPHABETIC_ISO)
            .currencyNumericIso(UPDATED_CURRENCY_NUMERIC_ISO)
            .currencyName(UPDATED_CURRENCY_NAME)
            .currencyEnglishName(UPDATED_CURRENCY_ENGLISH_NAME)
            .currencySymbol(UPDATED_CURRENCY_SYMBOL)
            .floatingPoint(UPDATED_FLOATING_POINT)
            .isBaseCurrency(UPDATED_IS_BASE_CURRENCY)
            .isDefaultCurrency(UPDATED_IS_DEFAULT_CURRENCY)
            .conversionMethod(UPDATED_CONVERSION_METHOD)
            .isActive(UPDATED_IS_ACTIVE)
            .sortOrder(UPDATED_SORT_ORDER);
        return currency;
    }

    @BeforeEach
    public void initTest() {
        currency = createEntity(em);
    }

    @Test
    @Transactional
    void createCurrency() throws Exception {
        int databaseSizeBeforeCreate = currencyRepository.findAll().size();
        // Create the Currency
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);
        restCurrencyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(currencyDTO)))
            .andExpect(status().isCreated());

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeCreate + 1);
        Currency testCurrency = currencyList.get(currencyList.size() - 1);
        assertThat(testCurrency.getCurrencyId()).isEqualTo(DEFAULT_CURRENCY_ID);
        assertThat(testCurrency.getCurrencyAlphabeticIso()).isEqualTo(DEFAULT_CURRENCY_ALPHABETIC_ISO);
        assertThat(testCurrency.getCurrencyNumericIso()).isEqualTo(DEFAULT_CURRENCY_NUMERIC_ISO);
        assertThat(testCurrency.getCurrencyName()).isEqualTo(DEFAULT_CURRENCY_NAME);
        assertThat(testCurrency.getCurrencyEnglishName()).isEqualTo(DEFAULT_CURRENCY_ENGLISH_NAME);
        assertThat(testCurrency.getCurrencySymbol()).isEqualTo(DEFAULT_CURRENCY_SYMBOL);
        assertThat(testCurrency.getFloatingPoint()).isEqualTo(DEFAULT_FLOATING_POINT);
        assertThat(testCurrency.getIsBaseCurrency()).isEqualTo(DEFAULT_IS_BASE_CURRENCY);
        assertThat(testCurrency.getIsDefaultCurrency()).isEqualTo(DEFAULT_IS_DEFAULT_CURRENCY);
        assertThat(testCurrency.getConversionMethod()).isEqualTo(DEFAULT_CONVERSION_METHOD);
        assertThat(testCurrency.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testCurrency.getSortOrder()).isEqualTo(DEFAULT_SORT_ORDER);
    }

    @Test
    @Transactional
    void createCurrencyWithExistingId() throws Exception {
        // Create the Currency with an existing ID
        currency.setId(1L);
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);

        int databaseSizeBeforeCreate = currencyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCurrencyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(currencyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCurrencyIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = currencyRepository.findAll().size();
        // set the field null
        currency.setCurrencyId(null);

        // Create the Currency, which fails.
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);

        restCurrencyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(currencyDTO)))
            .andExpect(status().isBadRequest());

        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCurrencyAlphabeticIsoIsRequired() throws Exception {
        int databaseSizeBeforeTest = currencyRepository.findAll().size();
        // set the field null
        currency.setCurrencyAlphabeticIso(null);

        // Create the Currency, which fails.
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);

        restCurrencyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(currencyDTO)))
            .andExpect(status().isBadRequest());

        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCurrencyNumericIsoIsRequired() throws Exception {
        int databaseSizeBeforeTest = currencyRepository.findAll().size();
        // set the field null
        currency.setCurrencyNumericIso(null);

        // Create the Currency, which fails.
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);

        restCurrencyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(currencyDTO)))
            .andExpect(status().isBadRequest());

        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCurrencyNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = currencyRepository.findAll().size();
        // set the field null
        currency.setCurrencyName(null);

        // Create the Currency, which fails.
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);

        restCurrencyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(currencyDTO)))
            .andExpect(status().isBadRequest());

        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCurrencies() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList
        restCurrencyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(currency.getId().intValue())))
            .andExpect(jsonPath("$.[*].currencyId").value(hasItem(DEFAULT_CURRENCY_ID)))
            .andExpect(jsonPath("$.[*].currencyAlphabeticIso").value(hasItem(DEFAULT_CURRENCY_ALPHABETIC_ISO)))
            .andExpect(jsonPath("$.[*].currencyNumericIso").value(hasItem(DEFAULT_CURRENCY_NUMERIC_ISO)))
            .andExpect(jsonPath("$.[*].currencyName").value(hasItem(DEFAULT_CURRENCY_NAME)))
            .andExpect(jsonPath("$.[*].currencyEnglishName").value(hasItem(DEFAULT_CURRENCY_ENGLISH_NAME)))
            .andExpect(jsonPath("$.[*].currencySymbol").value(hasItem(DEFAULT_CURRENCY_SYMBOL)))
            .andExpect(jsonPath("$.[*].floatingPoint").value(hasItem(DEFAULT_FLOATING_POINT)))
            .andExpect(jsonPath("$.[*].isBaseCurrency").value(hasItem(DEFAULT_IS_BASE_CURRENCY)))
            .andExpect(jsonPath("$.[*].isDefaultCurrency").value(hasItem(DEFAULT_IS_DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].conversionMethod").value(hasItem(DEFAULT_CONVERSION_METHOD)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)));
    }

    @Test
    @Transactional
    void getCurrency() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get the currency
        restCurrencyMockMvc
            .perform(get(ENTITY_API_URL_ID, currency.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(currency.getId().intValue()))
            .andExpect(jsonPath("$.currencyId").value(DEFAULT_CURRENCY_ID))
            .andExpect(jsonPath("$.currencyAlphabeticIso").value(DEFAULT_CURRENCY_ALPHABETIC_ISO))
            .andExpect(jsonPath("$.currencyNumericIso").value(DEFAULT_CURRENCY_NUMERIC_ISO))
            .andExpect(jsonPath("$.currencyName").value(DEFAULT_CURRENCY_NAME))
            .andExpect(jsonPath("$.currencyEnglishName").value(DEFAULT_CURRENCY_ENGLISH_NAME))
            .andExpect(jsonPath("$.currencySymbol").value(DEFAULT_CURRENCY_SYMBOL))
            .andExpect(jsonPath("$.floatingPoint").value(DEFAULT_FLOATING_POINT))
            .andExpect(jsonPath("$.isBaseCurrency").value(DEFAULT_IS_BASE_CURRENCY))
            .andExpect(jsonPath("$.isDefaultCurrency").value(DEFAULT_IS_DEFAULT_CURRENCY))
            .andExpect(jsonPath("$.conversionMethod").value(DEFAULT_CONVERSION_METHOD))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE))
            .andExpect(jsonPath("$.sortOrder").value(DEFAULT_SORT_ORDER));
    }

    @Test
    @Transactional
    void getCurrenciesByIdFiltering() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        Long id = currency.getId();

        defaultCurrencyShouldBeFound("id.equals=" + id);
        defaultCurrencyShouldNotBeFound("id.notEquals=" + id);

        defaultCurrencyShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCurrencyShouldNotBeFound("id.greaterThan=" + id);

        defaultCurrencyShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCurrencyShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCurrencyIdIsEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where currencyId equals to DEFAULT_CURRENCY_ID
        defaultCurrencyShouldBeFound("currencyId.equals=" + DEFAULT_CURRENCY_ID);

        // Get all the currencyList where currencyId equals to UPDATED_CURRENCY_ID
        defaultCurrencyShouldNotBeFound("currencyId.equals=" + UPDATED_CURRENCY_ID);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCurrencyIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where currencyId not equals to DEFAULT_CURRENCY_ID
        defaultCurrencyShouldNotBeFound("currencyId.notEquals=" + DEFAULT_CURRENCY_ID);

        // Get all the currencyList where currencyId not equals to UPDATED_CURRENCY_ID
        defaultCurrencyShouldBeFound("currencyId.notEquals=" + UPDATED_CURRENCY_ID);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCurrencyIdIsInShouldWork() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where currencyId in DEFAULT_CURRENCY_ID or UPDATED_CURRENCY_ID
        defaultCurrencyShouldBeFound("currencyId.in=" + DEFAULT_CURRENCY_ID + "," + UPDATED_CURRENCY_ID);

        // Get all the currencyList where currencyId equals to UPDATED_CURRENCY_ID
        defaultCurrencyShouldNotBeFound("currencyId.in=" + UPDATED_CURRENCY_ID);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCurrencyIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where currencyId is not null
        defaultCurrencyShouldBeFound("currencyId.specified=true");

        // Get all the currencyList where currencyId is null
        defaultCurrencyShouldNotBeFound("currencyId.specified=false");
    }

    @Test
    @Transactional
    void getAllCurrenciesByCurrencyIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where currencyId is greater than or equal to DEFAULT_CURRENCY_ID
        defaultCurrencyShouldBeFound("currencyId.greaterThanOrEqual=" + DEFAULT_CURRENCY_ID);

        // Get all the currencyList where currencyId is greater than or equal to UPDATED_CURRENCY_ID
        defaultCurrencyShouldNotBeFound("currencyId.greaterThanOrEqual=" + UPDATED_CURRENCY_ID);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCurrencyIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where currencyId is less than or equal to DEFAULT_CURRENCY_ID
        defaultCurrencyShouldBeFound("currencyId.lessThanOrEqual=" + DEFAULT_CURRENCY_ID);

        // Get all the currencyList where currencyId is less than or equal to SMALLER_CURRENCY_ID
        defaultCurrencyShouldNotBeFound("currencyId.lessThanOrEqual=" + SMALLER_CURRENCY_ID);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCurrencyIdIsLessThanSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where currencyId is less than DEFAULT_CURRENCY_ID
        defaultCurrencyShouldNotBeFound("currencyId.lessThan=" + DEFAULT_CURRENCY_ID);

        // Get all the currencyList where currencyId is less than UPDATED_CURRENCY_ID
        defaultCurrencyShouldBeFound("currencyId.lessThan=" + UPDATED_CURRENCY_ID);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCurrencyIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where currencyId is greater than DEFAULT_CURRENCY_ID
        defaultCurrencyShouldNotBeFound("currencyId.greaterThan=" + DEFAULT_CURRENCY_ID);

        // Get all the currencyList where currencyId is greater than SMALLER_CURRENCY_ID
        defaultCurrencyShouldBeFound("currencyId.greaterThan=" + SMALLER_CURRENCY_ID);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCurrencyAlphabeticIsoIsEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where currencyAlphabeticIso equals to DEFAULT_CURRENCY_ALPHABETIC_ISO
        defaultCurrencyShouldBeFound("currencyAlphabeticIso.equals=" + DEFAULT_CURRENCY_ALPHABETIC_ISO);

        // Get all the currencyList where currencyAlphabeticIso equals to UPDATED_CURRENCY_ALPHABETIC_ISO
        defaultCurrencyShouldNotBeFound("currencyAlphabeticIso.equals=" + UPDATED_CURRENCY_ALPHABETIC_ISO);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCurrencyAlphabeticIsoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where currencyAlphabeticIso not equals to DEFAULT_CURRENCY_ALPHABETIC_ISO
        defaultCurrencyShouldNotBeFound("currencyAlphabeticIso.notEquals=" + DEFAULT_CURRENCY_ALPHABETIC_ISO);

        // Get all the currencyList where currencyAlphabeticIso not equals to UPDATED_CURRENCY_ALPHABETIC_ISO
        defaultCurrencyShouldBeFound("currencyAlphabeticIso.notEquals=" + UPDATED_CURRENCY_ALPHABETIC_ISO);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCurrencyAlphabeticIsoIsInShouldWork() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where currencyAlphabeticIso in DEFAULT_CURRENCY_ALPHABETIC_ISO or UPDATED_CURRENCY_ALPHABETIC_ISO
        defaultCurrencyShouldBeFound("currencyAlphabeticIso.in=" + DEFAULT_CURRENCY_ALPHABETIC_ISO + "," + UPDATED_CURRENCY_ALPHABETIC_ISO);

        // Get all the currencyList where currencyAlphabeticIso equals to UPDATED_CURRENCY_ALPHABETIC_ISO
        defaultCurrencyShouldNotBeFound("currencyAlphabeticIso.in=" + UPDATED_CURRENCY_ALPHABETIC_ISO);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCurrencyAlphabeticIsoIsNullOrNotNull() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where currencyAlphabeticIso is not null
        defaultCurrencyShouldBeFound("currencyAlphabeticIso.specified=true");

        // Get all the currencyList where currencyAlphabeticIso is null
        defaultCurrencyShouldNotBeFound("currencyAlphabeticIso.specified=false");
    }

    @Test
    @Transactional
    void getAllCurrenciesByCurrencyAlphabeticIsoContainsSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where currencyAlphabeticIso contains DEFAULT_CURRENCY_ALPHABETIC_ISO
        defaultCurrencyShouldBeFound("currencyAlphabeticIso.contains=" + DEFAULT_CURRENCY_ALPHABETIC_ISO);

        // Get all the currencyList where currencyAlphabeticIso contains UPDATED_CURRENCY_ALPHABETIC_ISO
        defaultCurrencyShouldNotBeFound("currencyAlphabeticIso.contains=" + UPDATED_CURRENCY_ALPHABETIC_ISO);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCurrencyAlphabeticIsoNotContainsSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where currencyAlphabeticIso does not contain DEFAULT_CURRENCY_ALPHABETIC_ISO
        defaultCurrencyShouldNotBeFound("currencyAlphabeticIso.doesNotContain=" + DEFAULT_CURRENCY_ALPHABETIC_ISO);

        // Get all the currencyList where currencyAlphabeticIso does not contain UPDATED_CURRENCY_ALPHABETIC_ISO
        defaultCurrencyShouldBeFound("currencyAlphabeticIso.doesNotContain=" + UPDATED_CURRENCY_ALPHABETIC_ISO);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCurrencyNumericIsoIsEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where currencyNumericIso equals to DEFAULT_CURRENCY_NUMERIC_ISO
        defaultCurrencyShouldBeFound("currencyNumericIso.equals=" + DEFAULT_CURRENCY_NUMERIC_ISO);

        // Get all the currencyList where currencyNumericIso equals to UPDATED_CURRENCY_NUMERIC_ISO
        defaultCurrencyShouldNotBeFound("currencyNumericIso.equals=" + UPDATED_CURRENCY_NUMERIC_ISO);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCurrencyNumericIsoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where currencyNumericIso not equals to DEFAULT_CURRENCY_NUMERIC_ISO
        defaultCurrencyShouldNotBeFound("currencyNumericIso.notEquals=" + DEFAULT_CURRENCY_NUMERIC_ISO);

        // Get all the currencyList where currencyNumericIso not equals to UPDATED_CURRENCY_NUMERIC_ISO
        defaultCurrencyShouldBeFound("currencyNumericIso.notEquals=" + UPDATED_CURRENCY_NUMERIC_ISO);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCurrencyNumericIsoIsInShouldWork() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where currencyNumericIso in DEFAULT_CURRENCY_NUMERIC_ISO or UPDATED_CURRENCY_NUMERIC_ISO
        defaultCurrencyShouldBeFound("currencyNumericIso.in=" + DEFAULT_CURRENCY_NUMERIC_ISO + "," + UPDATED_CURRENCY_NUMERIC_ISO);

        // Get all the currencyList where currencyNumericIso equals to UPDATED_CURRENCY_NUMERIC_ISO
        defaultCurrencyShouldNotBeFound("currencyNumericIso.in=" + UPDATED_CURRENCY_NUMERIC_ISO);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCurrencyNumericIsoIsNullOrNotNull() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where currencyNumericIso is not null
        defaultCurrencyShouldBeFound("currencyNumericIso.specified=true");

        // Get all the currencyList where currencyNumericIso is null
        defaultCurrencyShouldNotBeFound("currencyNumericIso.specified=false");
    }

    @Test
    @Transactional
    void getAllCurrenciesByCurrencyNumericIsoContainsSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where currencyNumericIso contains DEFAULT_CURRENCY_NUMERIC_ISO
        defaultCurrencyShouldBeFound("currencyNumericIso.contains=" + DEFAULT_CURRENCY_NUMERIC_ISO);

        // Get all the currencyList where currencyNumericIso contains UPDATED_CURRENCY_NUMERIC_ISO
        defaultCurrencyShouldNotBeFound("currencyNumericIso.contains=" + UPDATED_CURRENCY_NUMERIC_ISO);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCurrencyNumericIsoNotContainsSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where currencyNumericIso does not contain DEFAULT_CURRENCY_NUMERIC_ISO
        defaultCurrencyShouldNotBeFound("currencyNumericIso.doesNotContain=" + DEFAULT_CURRENCY_NUMERIC_ISO);

        // Get all the currencyList where currencyNumericIso does not contain UPDATED_CURRENCY_NUMERIC_ISO
        defaultCurrencyShouldBeFound("currencyNumericIso.doesNotContain=" + UPDATED_CURRENCY_NUMERIC_ISO);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCurrencyNameIsEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where currencyName equals to DEFAULT_CURRENCY_NAME
        defaultCurrencyShouldBeFound("currencyName.equals=" + DEFAULT_CURRENCY_NAME);

        // Get all the currencyList where currencyName equals to UPDATED_CURRENCY_NAME
        defaultCurrencyShouldNotBeFound("currencyName.equals=" + UPDATED_CURRENCY_NAME);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCurrencyNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where currencyName not equals to DEFAULT_CURRENCY_NAME
        defaultCurrencyShouldNotBeFound("currencyName.notEquals=" + DEFAULT_CURRENCY_NAME);

        // Get all the currencyList where currencyName not equals to UPDATED_CURRENCY_NAME
        defaultCurrencyShouldBeFound("currencyName.notEquals=" + UPDATED_CURRENCY_NAME);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCurrencyNameIsInShouldWork() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where currencyName in DEFAULT_CURRENCY_NAME or UPDATED_CURRENCY_NAME
        defaultCurrencyShouldBeFound("currencyName.in=" + DEFAULT_CURRENCY_NAME + "," + UPDATED_CURRENCY_NAME);

        // Get all the currencyList where currencyName equals to UPDATED_CURRENCY_NAME
        defaultCurrencyShouldNotBeFound("currencyName.in=" + UPDATED_CURRENCY_NAME);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCurrencyNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where currencyName is not null
        defaultCurrencyShouldBeFound("currencyName.specified=true");

        // Get all the currencyList where currencyName is null
        defaultCurrencyShouldNotBeFound("currencyName.specified=false");
    }

    @Test
    @Transactional
    void getAllCurrenciesByCurrencyNameContainsSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where currencyName contains DEFAULT_CURRENCY_NAME
        defaultCurrencyShouldBeFound("currencyName.contains=" + DEFAULT_CURRENCY_NAME);

        // Get all the currencyList where currencyName contains UPDATED_CURRENCY_NAME
        defaultCurrencyShouldNotBeFound("currencyName.contains=" + UPDATED_CURRENCY_NAME);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCurrencyNameNotContainsSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where currencyName does not contain DEFAULT_CURRENCY_NAME
        defaultCurrencyShouldNotBeFound("currencyName.doesNotContain=" + DEFAULT_CURRENCY_NAME);

        // Get all the currencyList where currencyName does not contain UPDATED_CURRENCY_NAME
        defaultCurrencyShouldBeFound("currencyName.doesNotContain=" + UPDATED_CURRENCY_NAME);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCurrencyEnglishNameIsEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where currencyEnglishName equals to DEFAULT_CURRENCY_ENGLISH_NAME
        defaultCurrencyShouldBeFound("currencyEnglishName.equals=" + DEFAULT_CURRENCY_ENGLISH_NAME);

        // Get all the currencyList where currencyEnglishName equals to UPDATED_CURRENCY_ENGLISH_NAME
        defaultCurrencyShouldNotBeFound("currencyEnglishName.equals=" + UPDATED_CURRENCY_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCurrencyEnglishNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where currencyEnglishName not equals to DEFAULT_CURRENCY_ENGLISH_NAME
        defaultCurrencyShouldNotBeFound("currencyEnglishName.notEquals=" + DEFAULT_CURRENCY_ENGLISH_NAME);

        // Get all the currencyList where currencyEnglishName not equals to UPDATED_CURRENCY_ENGLISH_NAME
        defaultCurrencyShouldBeFound("currencyEnglishName.notEquals=" + UPDATED_CURRENCY_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCurrencyEnglishNameIsInShouldWork() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where currencyEnglishName in DEFAULT_CURRENCY_ENGLISH_NAME or UPDATED_CURRENCY_ENGLISH_NAME
        defaultCurrencyShouldBeFound("currencyEnglishName.in=" + DEFAULT_CURRENCY_ENGLISH_NAME + "," + UPDATED_CURRENCY_ENGLISH_NAME);

        // Get all the currencyList where currencyEnglishName equals to UPDATED_CURRENCY_ENGLISH_NAME
        defaultCurrencyShouldNotBeFound("currencyEnglishName.in=" + UPDATED_CURRENCY_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCurrencyEnglishNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where currencyEnglishName is not null
        defaultCurrencyShouldBeFound("currencyEnglishName.specified=true");

        // Get all the currencyList where currencyEnglishName is null
        defaultCurrencyShouldNotBeFound("currencyEnglishName.specified=false");
    }

    @Test
    @Transactional
    void getAllCurrenciesByCurrencyEnglishNameContainsSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where currencyEnglishName contains DEFAULT_CURRENCY_ENGLISH_NAME
        defaultCurrencyShouldBeFound("currencyEnglishName.contains=" + DEFAULT_CURRENCY_ENGLISH_NAME);

        // Get all the currencyList where currencyEnglishName contains UPDATED_CURRENCY_ENGLISH_NAME
        defaultCurrencyShouldNotBeFound("currencyEnglishName.contains=" + UPDATED_CURRENCY_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCurrencyEnglishNameNotContainsSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where currencyEnglishName does not contain DEFAULT_CURRENCY_ENGLISH_NAME
        defaultCurrencyShouldNotBeFound("currencyEnglishName.doesNotContain=" + DEFAULT_CURRENCY_ENGLISH_NAME);

        // Get all the currencyList where currencyEnglishName does not contain UPDATED_CURRENCY_ENGLISH_NAME
        defaultCurrencyShouldBeFound("currencyEnglishName.doesNotContain=" + UPDATED_CURRENCY_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCurrencySymbolIsEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where currencySymbol equals to DEFAULT_CURRENCY_SYMBOL
        defaultCurrencyShouldBeFound("currencySymbol.equals=" + DEFAULT_CURRENCY_SYMBOL);

        // Get all the currencyList where currencySymbol equals to UPDATED_CURRENCY_SYMBOL
        defaultCurrencyShouldNotBeFound("currencySymbol.equals=" + UPDATED_CURRENCY_SYMBOL);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCurrencySymbolIsNotEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where currencySymbol not equals to DEFAULT_CURRENCY_SYMBOL
        defaultCurrencyShouldNotBeFound("currencySymbol.notEquals=" + DEFAULT_CURRENCY_SYMBOL);

        // Get all the currencyList where currencySymbol not equals to UPDATED_CURRENCY_SYMBOL
        defaultCurrencyShouldBeFound("currencySymbol.notEquals=" + UPDATED_CURRENCY_SYMBOL);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCurrencySymbolIsInShouldWork() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where currencySymbol in DEFAULT_CURRENCY_SYMBOL or UPDATED_CURRENCY_SYMBOL
        defaultCurrencyShouldBeFound("currencySymbol.in=" + DEFAULT_CURRENCY_SYMBOL + "," + UPDATED_CURRENCY_SYMBOL);

        // Get all the currencyList where currencySymbol equals to UPDATED_CURRENCY_SYMBOL
        defaultCurrencyShouldNotBeFound("currencySymbol.in=" + UPDATED_CURRENCY_SYMBOL);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCurrencySymbolIsNullOrNotNull() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where currencySymbol is not null
        defaultCurrencyShouldBeFound("currencySymbol.specified=true");

        // Get all the currencyList where currencySymbol is null
        defaultCurrencyShouldNotBeFound("currencySymbol.specified=false");
    }

    @Test
    @Transactional
    void getAllCurrenciesByCurrencySymbolContainsSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where currencySymbol contains DEFAULT_CURRENCY_SYMBOL
        defaultCurrencyShouldBeFound("currencySymbol.contains=" + DEFAULT_CURRENCY_SYMBOL);

        // Get all the currencyList where currencySymbol contains UPDATED_CURRENCY_SYMBOL
        defaultCurrencyShouldNotBeFound("currencySymbol.contains=" + UPDATED_CURRENCY_SYMBOL);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCurrencySymbolNotContainsSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where currencySymbol does not contain DEFAULT_CURRENCY_SYMBOL
        defaultCurrencyShouldNotBeFound("currencySymbol.doesNotContain=" + DEFAULT_CURRENCY_SYMBOL);

        // Get all the currencyList where currencySymbol does not contain UPDATED_CURRENCY_SYMBOL
        defaultCurrencyShouldBeFound("currencySymbol.doesNotContain=" + UPDATED_CURRENCY_SYMBOL);
    }

    @Test
    @Transactional
    void getAllCurrenciesByFloatingPointIsEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where floatingPoint equals to DEFAULT_FLOATING_POINT
        defaultCurrencyShouldBeFound("floatingPoint.equals=" + DEFAULT_FLOATING_POINT);

        // Get all the currencyList where floatingPoint equals to UPDATED_FLOATING_POINT
        defaultCurrencyShouldNotBeFound("floatingPoint.equals=" + UPDATED_FLOATING_POINT);
    }

    @Test
    @Transactional
    void getAllCurrenciesByFloatingPointIsNotEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where floatingPoint not equals to DEFAULT_FLOATING_POINT
        defaultCurrencyShouldNotBeFound("floatingPoint.notEquals=" + DEFAULT_FLOATING_POINT);

        // Get all the currencyList where floatingPoint not equals to UPDATED_FLOATING_POINT
        defaultCurrencyShouldBeFound("floatingPoint.notEquals=" + UPDATED_FLOATING_POINT);
    }

    @Test
    @Transactional
    void getAllCurrenciesByFloatingPointIsInShouldWork() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where floatingPoint in DEFAULT_FLOATING_POINT or UPDATED_FLOATING_POINT
        defaultCurrencyShouldBeFound("floatingPoint.in=" + DEFAULT_FLOATING_POINT + "," + UPDATED_FLOATING_POINT);

        // Get all the currencyList where floatingPoint equals to UPDATED_FLOATING_POINT
        defaultCurrencyShouldNotBeFound("floatingPoint.in=" + UPDATED_FLOATING_POINT);
    }

    @Test
    @Transactional
    void getAllCurrenciesByFloatingPointIsNullOrNotNull() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where floatingPoint is not null
        defaultCurrencyShouldBeFound("floatingPoint.specified=true");

        // Get all the currencyList where floatingPoint is null
        defaultCurrencyShouldNotBeFound("floatingPoint.specified=false");
    }

    @Test
    @Transactional
    void getAllCurrenciesByFloatingPointIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where floatingPoint is greater than or equal to DEFAULT_FLOATING_POINT
        defaultCurrencyShouldBeFound("floatingPoint.greaterThanOrEqual=" + DEFAULT_FLOATING_POINT);

        // Get all the currencyList where floatingPoint is greater than or equal to UPDATED_FLOATING_POINT
        defaultCurrencyShouldNotBeFound("floatingPoint.greaterThanOrEqual=" + UPDATED_FLOATING_POINT);
    }

    @Test
    @Transactional
    void getAllCurrenciesByFloatingPointIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where floatingPoint is less than or equal to DEFAULT_FLOATING_POINT
        defaultCurrencyShouldBeFound("floatingPoint.lessThanOrEqual=" + DEFAULT_FLOATING_POINT);

        // Get all the currencyList where floatingPoint is less than or equal to SMALLER_FLOATING_POINT
        defaultCurrencyShouldNotBeFound("floatingPoint.lessThanOrEqual=" + SMALLER_FLOATING_POINT);
    }

    @Test
    @Transactional
    void getAllCurrenciesByFloatingPointIsLessThanSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where floatingPoint is less than DEFAULT_FLOATING_POINT
        defaultCurrencyShouldNotBeFound("floatingPoint.lessThan=" + DEFAULT_FLOATING_POINT);

        // Get all the currencyList where floatingPoint is less than UPDATED_FLOATING_POINT
        defaultCurrencyShouldBeFound("floatingPoint.lessThan=" + UPDATED_FLOATING_POINT);
    }

    @Test
    @Transactional
    void getAllCurrenciesByFloatingPointIsGreaterThanSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where floatingPoint is greater than DEFAULT_FLOATING_POINT
        defaultCurrencyShouldNotBeFound("floatingPoint.greaterThan=" + DEFAULT_FLOATING_POINT);

        // Get all the currencyList where floatingPoint is greater than SMALLER_FLOATING_POINT
        defaultCurrencyShouldBeFound("floatingPoint.greaterThan=" + SMALLER_FLOATING_POINT);
    }

    @Test
    @Transactional
    void getAllCurrenciesByIsBaseCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where isBaseCurrency equals to DEFAULT_IS_BASE_CURRENCY
        defaultCurrencyShouldBeFound("isBaseCurrency.equals=" + DEFAULT_IS_BASE_CURRENCY);

        // Get all the currencyList where isBaseCurrency equals to UPDATED_IS_BASE_CURRENCY
        defaultCurrencyShouldNotBeFound("isBaseCurrency.equals=" + UPDATED_IS_BASE_CURRENCY);
    }

    @Test
    @Transactional
    void getAllCurrenciesByIsBaseCurrencyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where isBaseCurrency not equals to DEFAULT_IS_BASE_CURRENCY
        defaultCurrencyShouldNotBeFound("isBaseCurrency.notEquals=" + DEFAULT_IS_BASE_CURRENCY);

        // Get all the currencyList where isBaseCurrency not equals to UPDATED_IS_BASE_CURRENCY
        defaultCurrencyShouldBeFound("isBaseCurrency.notEquals=" + UPDATED_IS_BASE_CURRENCY);
    }

    @Test
    @Transactional
    void getAllCurrenciesByIsBaseCurrencyIsInShouldWork() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where isBaseCurrency in DEFAULT_IS_BASE_CURRENCY or UPDATED_IS_BASE_CURRENCY
        defaultCurrencyShouldBeFound("isBaseCurrency.in=" + DEFAULT_IS_BASE_CURRENCY + "," + UPDATED_IS_BASE_CURRENCY);

        // Get all the currencyList where isBaseCurrency equals to UPDATED_IS_BASE_CURRENCY
        defaultCurrencyShouldNotBeFound("isBaseCurrency.in=" + UPDATED_IS_BASE_CURRENCY);
    }

    @Test
    @Transactional
    void getAllCurrenciesByIsBaseCurrencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where isBaseCurrency is not null
        defaultCurrencyShouldBeFound("isBaseCurrency.specified=true");

        // Get all the currencyList where isBaseCurrency is null
        defaultCurrencyShouldNotBeFound("isBaseCurrency.specified=false");
    }

    @Test
    @Transactional
    void getAllCurrenciesByIsBaseCurrencyIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where isBaseCurrency is greater than or equal to DEFAULT_IS_BASE_CURRENCY
        defaultCurrencyShouldBeFound("isBaseCurrency.greaterThanOrEqual=" + DEFAULT_IS_BASE_CURRENCY);

        // Get all the currencyList where isBaseCurrency is greater than or equal to UPDATED_IS_BASE_CURRENCY
        defaultCurrencyShouldNotBeFound("isBaseCurrency.greaterThanOrEqual=" + UPDATED_IS_BASE_CURRENCY);
    }

    @Test
    @Transactional
    void getAllCurrenciesByIsBaseCurrencyIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where isBaseCurrency is less than or equal to DEFAULT_IS_BASE_CURRENCY
        defaultCurrencyShouldBeFound("isBaseCurrency.lessThanOrEqual=" + DEFAULT_IS_BASE_CURRENCY);

        // Get all the currencyList where isBaseCurrency is less than or equal to SMALLER_IS_BASE_CURRENCY
        defaultCurrencyShouldNotBeFound("isBaseCurrency.lessThanOrEqual=" + SMALLER_IS_BASE_CURRENCY);
    }

    @Test
    @Transactional
    void getAllCurrenciesByIsBaseCurrencyIsLessThanSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where isBaseCurrency is less than DEFAULT_IS_BASE_CURRENCY
        defaultCurrencyShouldNotBeFound("isBaseCurrency.lessThan=" + DEFAULT_IS_BASE_CURRENCY);

        // Get all the currencyList where isBaseCurrency is less than UPDATED_IS_BASE_CURRENCY
        defaultCurrencyShouldBeFound("isBaseCurrency.lessThan=" + UPDATED_IS_BASE_CURRENCY);
    }

    @Test
    @Transactional
    void getAllCurrenciesByIsBaseCurrencyIsGreaterThanSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where isBaseCurrency is greater than DEFAULT_IS_BASE_CURRENCY
        defaultCurrencyShouldNotBeFound("isBaseCurrency.greaterThan=" + DEFAULT_IS_BASE_CURRENCY);

        // Get all the currencyList where isBaseCurrency is greater than SMALLER_IS_BASE_CURRENCY
        defaultCurrencyShouldBeFound("isBaseCurrency.greaterThan=" + SMALLER_IS_BASE_CURRENCY);
    }

    @Test
    @Transactional
    void getAllCurrenciesByIsDefaultCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where isDefaultCurrency equals to DEFAULT_IS_DEFAULT_CURRENCY
        defaultCurrencyShouldBeFound("isDefaultCurrency.equals=" + DEFAULT_IS_DEFAULT_CURRENCY);

        // Get all the currencyList where isDefaultCurrency equals to UPDATED_IS_DEFAULT_CURRENCY
        defaultCurrencyShouldNotBeFound("isDefaultCurrency.equals=" + UPDATED_IS_DEFAULT_CURRENCY);
    }

    @Test
    @Transactional
    void getAllCurrenciesByIsDefaultCurrencyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where isDefaultCurrency not equals to DEFAULT_IS_DEFAULT_CURRENCY
        defaultCurrencyShouldNotBeFound("isDefaultCurrency.notEquals=" + DEFAULT_IS_DEFAULT_CURRENCY);

        // Get all the currencyList where isDefaultCurrency not equals to UPDATED_IS_DEFAULT_CURRENCY
        defaultCurrencyShouldBeFound("isDefaultCurrency.notEquals=" + UPDATED_IS_DEFAULT_CURRENCY);
    }

    @Test
    @Transactional
    void getAllCurrenciesByIsDefaultCurrencyIsInShouldWork() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where isDefaultCurrency in DEFAULT_IS_DEFAULT_CURRENCY or UPDATED_IS_DEFAULT_CURRENCY
        defaultCurrencyShouldBeFound("isDefaultCurrency.in=" + DEFAULT_IS_DEFAULT_CURRENCY + "," + UPDATED_IS_DEFAULT_CURRENCY);

        // Get all the currencyList where isDefaultCurrency equals to UPDATED_IS_DEFAULT_CURRENCY
        defaultCurrencyShouldNotBeFound("isDefaultCurrency.in=" + UPDATED_IS_DEFAULT_CURRENCY);
    }

    @Test
    @Transactional
    void getAllCurrenciesByIsDefaultCurrencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where isDefaultCurrency is not null
        defaultCurrencyShouldBeFound("isDefaultCurrency.specified=true");

        // Get all the currencyList where isDefaultCurrency is null
        defaultCurrencyShouldNotBeFound("isDefaultCurrency.specified=false");
    }

    @Test
    @Transactional
    void getAllCurrenciesByIsDefaultCurrencyIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where isDefaultCurrency is greater than or equal to DEFAULT_IS_DEFAULT_CURRENCY
        defaultCurrencyShouldBeFound("isDefaultCurrency.greaterThanOrEqual=" + DEFAULT_IS_DEFAULT_CURRENCY);

        // Get all the currencyList where isDefaultCurrency is greater than or equal to UPDATED_IS_DEFAULT_CURRENCY
        defaultCurrencyShouldNotBeFound("isDefaultCurrency.greaterThanOrEqual=" + UPDATED_IS_DEFAULT_CURRENCY);
    }

    @Test
    @Transactional
    void getAllCurrenciesByIsDefaultCurrencyIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where isDefaultCurrency is less than or equal to DEFAULT_IS_DEFAULT_CURRENCY
        defaultCurrencyShouldBeFound("isDefaultCurrency.lessThanOrEqual=" + DEFAULT_IS_DEFAULT_CURRENCY);

        // Get all the currencyList where isDefaultCurrency is less than or equal to SMALLER_IS_DEFAULT_CURRENCY
        defaultCurrencyShouldNotBeFound("isDefaultCurrency.lessThanOrEqual=" + SMALLER_IS_DEFAULT_CURRENCY);
    }

    @Test
    @Transactional
    void getAllCurrenciesByIsDefaultCurrencyIsLessThanSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where isDefaultCurrency is less than DEFAULT_IS_DEFAULT_CURRENCY
        defaultCurrencyShouldNotBeFound("isDefaultCurrency.lessThan=" + DEFAULT_IS_DEFAULT_CURRENCY);

        // Get all the currencyList where isDefaultCurrency is less than UPDATED_IS_DEFAULT_CURRENCY
        defaultCurrencyShouldBeFound("isDefaultCurrency.lessThan=" + UPDATED_IS_DEFAULT_CURRENCY);
    }

    @Test
    @Transactional
    void getAllCurrenciesByIsDefaultCurrencyIsGreaterThanSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where isDefaultCurrency is greater than DEFAULT_IS_DEFAULT_CURRENCY
        defaultCurrencyShouldNotBeFound("isDefaultCurrency.greaterThan=" + DEFAULT_IS_DEFAULT_CURRENCY);

        // Get all the currencyList where isDefaultCurrency is greater than SMALLER_IS_DEFAULT_CURRENCY
        defaultCurrencyShouldBeFound("isDefaultCurrency.greaterThan=" + SMALLER_IS_DEFAULT_CURRENCY);
    }

    @Test
    @Transactional
    void getAllCurrenciesByConversionMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where conversionMethod equals to DEFAULT_CONVERSION_METHOD
        defaultCurrencyShouldBeFound("conversionMethod.equals=" + DEFAULT_CONVERSION_METHOD);

        // Get all the currencyList where conversionMethod equals to UPDATED_CONVERSION_METHOD
        defaultCurrencyShouldNotBeFound("conversionMethod.equals=" + UPDATED_CONVERSION_METHOD);
    }

    @Test
    @Transactional
    void getAllCurrenciesByConversionMethodIsNotEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where conversionMethod not equals to DEFAULT_CONVERSION_METHOD
        defaultCurrencyShouldNotBeFound("conversionMethod.notEquals=" + DEFAULT_CONVERSION_METHOD);

        // Get all the currencyList where conversionMethod not equals to UPDATED_CONVERSION_METHOD
        defaultCurrencyShouldBeFound("conversionMethod.notEquals=" + UPDATED_CONVERSION_METHOD);
    }

    @Test
    @Transactional
    void getAllCurrenciesByConversionMethodIsInShouldWork() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where conversionMethod in DEFAULT_CONVERSION_METHOD or UPDATED_CONVERSION_METHOD
        defaultCurrencyShouldBeFound("conversionMethod.in=" + DEFAULT_CONVERSION_METHOD + "," + UPDATED_CONVERSION_METHOD);

        // Get all the currencyList where conversionMethod equals to UPDATED_CONVERSION_METHOD
        defaultCurrencyShouldNotBeFound("conversionMethod.in=" + UPDATED_CONVERSION_METHOD);
    }

    @Test
    @Transactional
    void getAllCurrenciesByConversionMethodIsNullOrNotNull() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where conversionMethod is not null
        defaultCurrencyShouldBeFound("conversionMethod.specified=true");

        // Get all the currencyList where conversionMethod is null
        defaultCurrencyShouldNotBeFound("conversionMethod.specified=false");
    }

    @Test
    @Transactional
    void getAllCurrenciesByConversionMethodIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where conversionMethod is greater than or equal to DEFAULT_CONVERSION_METHOD
        defaultCurrencyShouldBeFound("conversionMethod.greaterThanOrEqual=" + DEFAULT_CONVERSION_METHOD);

        // Get all the currencyList where conversionMethod is greater than or equal to UPDATED_CONVERSION_METHOD
        defaultCurrencyShouldNotBeFound("conversionMethod.greaterThanOrEqual=" + UPDATED_CONVERSION_METHOD);
    }

    @Test
    @Transactional
    void getAllCurrenciesByConversionMethodIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where conversionMethod is less than or equal to DEFAULT_CONVERSION_METHOD
        defaultCurrencyShouldBeFound("conversionMethod.lessThanOrEqual=" + DEFAULT_CONVERSION_METHOD);

        // Get all the currencyList where conversionMethod is less than or equal to SMALLER_CONVERSION_METHOD
        defaultCurrencyShouldNotBeFound("conversionMethod.lessThanOrEqual=" + SMALLER_CONVERSION_METHOD);
    }

    @Test
    @Transactional
    void getAllCurrenciesByConversionMethodIsLessThanSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where conversionMethod is less than DEFAULT_CONVERSION_METHOD
        defaultCurrencyShouldNotBeFound("conversionMethod.lessThan=" + DEFAULT_CONVERSION_METHOD);

        // Get all the currencyList where conversionMethod is less than UPDATED_CONVERSION_METHOD
        defaultCurrencyShouldBeFound("conversionMethod.lessThan=" + UPDATED_CONVERSION_METHOD);
    }

    @Test
    @Transactional
    void getAllCurrenciesByConversionMethodIsGreaterThanSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where conversionMethod is greater than DEFAULT_CONVERSION_METHOD
        defaultCurrencyShouldNotBeFound("conversionMethod.greaterThan=" + DEFAULT_CONVERSION_METHOD);

        // Get all the currencyList where conversionMethod is greater than SMALLER_CONVERSION_METHOD
        defaultCurrencyShouldBeFound("conversionMethod.greaterThan=" + SMALLER_CONVERSION_METHOD);
    }

    @Test
    @Transactional
    void getAllCurrenciesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where isActive equals to DEFAULT_IS_ACTIVE
        defaultCurrencyShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the currencyList where isActive equals to UPDATED_IS_ACTIVE
        defaultCurrencyShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllCurrenciesByIsActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where isActive not equals to DEFAULT_IS_ACTIVE
        defaultCurrencyShouldNotBeFound("isActive.notEquals=" + DEFAULT_IS_ACTIVE);

        // Get all the currencyList where isActive not equals to UPDATED_IS_ACTIVE
        defaultCurrencyShouldBeFound("isActive.notEquals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllCurrenciesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultCurrencyShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the currencyList where isActive equals to UPDATED_IS_ACTIVE
        defaultCurrencyShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllCurrenciesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where isActive is not null
        defaultCurrencyShouldBeFound("isActive.specified=true");

        // Get all the currencyList where isActive is null
        defaultCurrencyShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllCurrenciesByIsActiveIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where isActive is greater than or equal to DEFAULT_IS_ACTIVE
        defaultCurrencyShouldBeFound("isActive.greaterThanOrEqual=" + DEFAULT_IS_ACTIVE);

        // Get all the currencyList where isActive is greater than or equal to UPDATED_IS_ACTIVE
        defaultCurrencyShouldNotBeFound("isActive.greaterThanOrEqual=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllCurrenciesByIsActiveIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where isActive is less than or equal to DEFAULT_IS_ACTIVE
        defaultCurrencyShouldBeFound("isActive.lessThanOrEqual=" + DEFAULT_IS_ACTIVE);

        // Get all the currencyList where isActive is less than or equal to SMALLER_IS_ACTIVE
        defaultCurrencyShouldNotBeFound("isActive.lessThanOrEqual=" + SMALLER_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllCurrenciesByIsActiveIsLessThanSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where isActive is less than DEFAULT_IS_ACTIVE
        defaultCurrencyShouldNotBeFound("isActive.lessThan=" + DEFAULT_IS_ACTIVE);

        // Get all the currencyList where isActive is less than UPDATED_IS_ACTIVE
        defaultCurrencyShouldBeFound("isActive.lessThan=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllCurrenciesByIsActiveIsGreaterThanSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where isActive is greater than DEFAULT_IS_ACTIVE
        defaultCurrencyShouldNotBeFound("isActive.greaterThan=" + DEFAULT_IS_ACTIVE);

        // Get all the currencyList where isActive is greater than SMALLER_IS_ACTIVE
        defaultCurrencyShouldBeFound("isActive.greaterThan=" + SMALLER_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllCurrenciesBySortOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where sortOrder equals to DEFAULT_SORT_ORDER
        defaultCurrencyShouldBeFound("sortOrder.equals=" + DEFAULT_SORT_ORDER);

        // Get all the currencyList where sortOrder equals to UPDATED_SORT_ORDER
        defaultCurrencyShouldNotBeFound("sortOrder.equals=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    void getAllCurrenciesBySortOrderIsNotEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where sortOrder not equals to DEFAULT_SORT_ORDER
        defaultCurrencyShouldNotBeFound("sortOrder.notEquals=" + DEFAULT_SORT_ORDER);

        // Get all the currencyList where sortOrder not equals to UPDATED_SORT_ORDER
        defaultCurrencyShouldBeFound("sortOrder.notEquals=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    void getAllCurrenciesBySortOrderIsInShouldWork() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where sortOrder in DEFAULT_SORT_ORDER or UPDATED_SORT_ORDER
        defaultCurrencyShouldBeFound("sortOrder.in=" + DEFAULT_SORT_ORDER + "," + UPDATED_SORT_ORDER);

        // Get all the currencyList where sortOrder equals to UPDATED_SORT_ORDER
        defaultCurrencyShouldNotBeFound("sortOrder.in=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    void getAllCurrenciesBySortOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where sortOrder is not null
        defaultCurrencyShouldBeFound("sortOrder.specified=true");

        // Get all the currencyList where sortOrder is null
        defaultCurrencyShouldNotBeFound("sortOrder.specified=false");
    }

    @Test
    @Transactional
    void getAllCurrenciesBySortOrderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where sortOrder is greater than or equal to DEFAULT_SORT_ORDER
        defaultCurrencyShouldBeFound("sortOrder.greaterThanOrEqual=" + DEFAULT_SORT_ORDER);

        // Get all the currencyList where sortOrder is greater than or equal to UPDATED_SORT_ORDER
        defaultCurrencyShouldNotBeFound("sortOrder.greaterThanOrEqual=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    void getAllCurrenciesBySortOrderIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where sortOrder is less than or equal to DEFAULT_SORT_ORDER
        defaultCurrencyShouldBeFound("sortOrder.lessThanOrEqual=" + DEFAULT_SORT_ORDER);

        // Get all the currencyList where sortOrder is less than or equal to SMALLER_SORT_ORDER
        defaultCurrencyShouldNotBeFound("sortOrder.lessThanOrEqual=" + SMALLER_SORT_ORDER);
    }

    @Test
    @Transactional
    void getAllCurrenciesBySortOrderIsLessThanSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where sortOrder is less than DEFAULT_SORT_ORDER
        defaultCurrencyShouldNotBeFound("sortOrder.lessThan=" + DEFAULT_SORT_ORDER);

        // Get all the currencyList where sortOrder is less than UPDATED_SORT_ORDER
        defaultCurrencyShouldBeFound("sortOrder.lessThan=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    void getAllCurrenciesBySortOrderIsGreaterThanSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where sortOrder is greater than DEFAULT_SORT_ORDER
        defaultCurrencyShouldNotBeFound("sortOrder.greaterThan=" + DEFAULT_SORT_ORDER);

        // Get all the currencyList where sortOrder is greater than SMALLER_SORT_ORDER
        defaultCurrencyShouldBeFound("sortOrder.greaterThan=" + SMALLER_SORT_ORDER);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCurrencyShouldBeFound(String filter) throws Exception {
        restCurrencyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(currency.getId().intValue())))
            .andExpect(jsonPath("$.[*].currencyId").value(hasItem(DEFAULT_CURRENCY_ID)))
            .andExpect(jsonPath("$.[*].currencyAlphabeticIso").value(hasItem(DEFAULT_CURRENCY_ALPHABETIC_ISO)))
            .andExpect(jsonPath("$.[*].currencyNumericIso").value(hasItem(DEFAULT_CURRENCY_NUMERIC_ISO)))
            .andExpect(jsonPath("$.[*].currencyName").value(hasItem(DEFAULT_CURRENCY_NAME)))
            .andExpect(jsonPath("$.[*].currencyEnglishName").value(hasItem(DEFAULT_CURRENCY_ENGLISH_NAME)))
            .andExpect(jsonPath("$.[*].currencySymbol").value(hasItem(DEFAULT_CURRENCY_SYMBOL)))
            .andExpect(jsonPath("$.[*].floatingPoint").value(hasItem(DEFAULT_FLOATING_POINT)))
            .andExpect(jsonPath("$.[*].isBaseCurrency").value(hasItem(DEFAULT_IS_BASE_CURRENCY)))
            .andExpect(jsonPath("$.[*].isDefaultCurrency").value(hasItem(DEFAULT_IS_DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].conversionMethod").value(hasItem(DEFAULT_CONVERSION_METHOD)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)));

        // Check, that the count call also returns 1
        restCurrencyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCurrencyShouldNotBeFound(String filter) throws Exception {
        restCurrencyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCurrencyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCurrency() throws Exception {
        // Get the currency
        restCurrencyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCurrency() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        int databaseSizeBeforeUpdate = currencyRepository.findAll().size();

        // Update the currency
        Currency updatedCurrency = currencyRepository.findById(currency.getId()).get();
        // Disconnect from session so that the updates on updatedCurrency are not directly saved in db
        em.detach(updatedCurrency);
        updatedCurrency
            .currencyId(UPDATED_CURRENCY_ID)
            .currencyAlphabeticIso(UPDATED_CURRENCY_ALPHABETIC_ISO)
            .currencyNumericIso(UPDATED_CURRENCY_NUMERIC_ISO)
            .currencyName(UPDATED_CURRENCY_NAME)
            .currencyEnglishName(UPDATED_CURRENCY_ENGLISH_NAME)
            .currencySymbol(UPDATED_CURRENCY_SYMBOL)
            .floatingPoint(UPDATED_FLOATING_POINT)
            .isBaseCurrency(UPDATED_IS_BASE_CURRENCY)
            .isDefaultCurrency(UPDATED_IS_DEFAULT_CURRENCY)
            .conversionMethod(UPDATED_CONVERSION_METHOD)
            .isActive(UPDATED_IS_ACTIVE)
            .sortOrder(UPDATED_SORT_ORDER);
        CurrencyDTO currencyDTO = currencyMapper.toDto(updatedCurrency);

        restCurrencyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, currencyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(currencyDTO))
            )
            .andExpect(status().isOk());

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeUpdate);
        Currency testCurrency = currencyList.get(currencyList.size() - 1);
        assertThat(testCurrency.getCurrencyId()).isEqualTo(UPDATED_CURRENCY_ID);
        assertThat(testCurrency.getCurrencyAlphabeticIso()).isEqualTo(UPDATED_CURRENCY_ALPHABETIC_ISO);
        assertThat(testCurrency.getCurrencyNumericIso()).isEqualTo(UPDATED_CURRENCY_NUMERIC_ISO);
        assertThat(testCurrency.getCurrencyName()).isEqualTo(UPDATED_CURRENCY_NAME);
        assertThat(testCurrency.getCurrencyEnglishName()).isEqualTo(UPDATED_CURRENCY_ENGLISH_NAME);
        assertThat(testCurrency.getCurrencySymbol()).isEqualTo(UPDATED_CURRENCY_SYMBOL);
        assertThat(testCurrency.getFloatingPoint()).isEqualTo(UPDATED_FLOATING_POINT);
        assertThat(testCurrency.getIsBaseCurrency()).isEqualTo(UPDATED_IS_BASE_CURRENCY);
        assertThat(testCurrency.getIsDefaultCurrency()).isEqualTo(UPDATED_IS_DEFAULT_CURRENCY);
        assertThat(testCurrency.getConversionMethod()).isEqualTo(UPDATED_CONVERSION_METHOD);
        assertThat(testCurrency.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testCurrency.getSortOrder()).isEqualTo(UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    void putNonExistingCurrency() throws Exception {
        int databaseSizeBeforeUpdate = currencyRepository.findAll().size();
        currency.setId(count.incrementAndGet());

        // Create the Currency
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCurrencyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, currencyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(currencyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCurrency() throws Exception {
        int databaseSizeBeforeUpdate = currencyRepository.findAll().size();
        currency.setId(count.incrementAndGet());

        // Create the Currency
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCurrencyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(currencyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCurrency() throws Exception {
        int databaseSizeBeforeUpdate = currencyRepository.findAll().size();
        currency.setId(count.incrementAndGet());

        // Create the Currency
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCurrencyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(currencyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCurrencyWithPatch() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        int databaseSizeBeforeUpdate = currencyRepository.findAll().size();

        // Update the currency using partial update
        Currency partialUpdatedCurrency = new Currency();
        partialUpdatedCurrency.setId(currency.getId());

        partialUpdatedCurrency
            .currencyAlphabeticIso(UPDATED_CURRENCY_ALPHABETIC_ISO)
            .currencyName(UPDATED_CURRENCY_NAME)
            .currencyEnglishName(UPDATED_CURRENCY_ENGLISH_NAME)
            .isBaseCurrency(UPDATED_IS_BASE_CURRENCY)
            .conversionMethod(UPDATED_CONVERSION_METHOD)
            .isActive(UPDATED_IS_ACTIVE)
            .sortOrder(UPDATED_SORT_ORDER);

        restCurrencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCurrency.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCurrency))
            )
            .andExpect(status().isOk());

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeUpdate);
        Currency testCurrency = currencyList.get(currencyList.size() - 1);
        assertThat(testCurrency.getCurrencyId()).isEqualTo(DEFAULT_CURRENCY_ID);
        assertThat(testCurrency.getCurrencyAlphabeticIso()).isEqualTo(UPDATED_CURRENCY_ALPHABETIC_ISO);
        assertThat(testCurrency.getCurrencyNumericIso()).isEqualTo(DEFAULT_CURRENCY_NUMERIC_ISO);
        assertThat(testCurrency.getCurrencyName()).isEqualTo(UPDATED_CURRENCY_NAME);
        assertThat(testCurrency.getCurrencyEnglishName()).isEqualTo(UPDATED_CURRENCY_ENGLISH_NAME);
        assertThat(testCurrency.getCurrencySymbol()).isEqualTo(DEFAULT_CURRENCY_SYMBOL);
        assertThat(testCurrency.getFloatingPoint()).isEqualTo(DEFAULT_FLOATING_POINT);
        assertThat(testCurrency.getIsBaseCurrency()).isEqualTo(UPDATED_IS_BASE_CURRENCY);
        assertThat(testCurrency.getIsDefaultCurrency()).isEqualTo(DEFAULT_IS_DEFAULT_CURRENCY);
        assertThat(testCurrency.getConversionMethod()).isEqualTo(UPDATED_CONVERSION_METHOD);
        assertThat(testCurrency.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testCurrency.getSortOrder()).isEqualTo(UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    void fullUpdateCurrencyWithPatch() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        int databaseSizeBeforeUpdate = currencyRepository.findAll().size();

        // Update the currency using partial update
        Currency partialUpdatedCurrency = new Currency();
        partialUpdatedCurrency.setId(currency.getId());

        partialUpdatedCurrency
            .currencyId(UPDATED_CURRENCY_ID)
            .currencyAlphabeticIso(UPDATED_CURRENCY_ALPHABETIC_ISO)
            .currencyNumericIso(UPDATED_CURRENCY_NUMERIC_ISO)
            .currencyName(UPDATED_CURRENCY_NAME)
            .currencyEnglishName(UPDATED_CURRENCY_ENGLISH_NAME)
            .currencySymbol(UPDATED_CURRENCY_SYMBOL)
            .floatingPoint(UPDATED_FLOATING_POINT)
            .isBaseCurrency(UPDATED_IS_BASE_CURRENCY)
            .isDefaultCurrency(UPDATED_IS_DEFAULT_CURRENCY)
            .conversionMethod(UPDATED_CONVERSION_METHOD)
            .isActive(UPDATED_IS_ACTIVE)
            .sortOrder(UPDATED_SORT_ORDER);

        restCurrencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCurrency.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCurrency))
            )
            .andExpect(status().isOk());

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeUpdate);
        Currency testCurrency = currencyList.get(currencyList.size() - 1);
        assertThat(testCurrency.getCurrencyId()).isEqualTo(UPDATED_CURRENCY_ID);
        assertThat(testCurrency.getCurrencyAlphabeticIso()).isEqualTo(UPDATED_CURRENCY_ALPHABETIC_ISO);
        assertThat(testCurrency.getCurrencyNumericIso()).isEqualTo(UPDATED_CURRENCY_NUMERIC_ISO);
        assertThat(testCurrency.getCurrencyName()).isEqualTo(UPDATED_CURRENCY_NAME);
        assertThat(testCurrency.getCurrencyEnglishName()).isEqualTo(UPDATED_CURRENCY_ENGLISH_NAME);
        assertThat(testCurrency.getCurrencySymbol()).isEqualTo(UPDATED_CURRENCY_SYMBOL);
        assertThat(testCurrency.getFloatingPoint()).isEqualTo(UPDATED_FLOATING_POINT);
        assertThat(testCurrency.getIsBaseCurrency()).isEqualTo(UPDATED_IS_BASE_CURRENCY);
        assertThat(testCurrency.getIsDefaultCurrency()).isEqualTo(UPDATED_IS_DEFAULT_CURRENCY);
        assertThat(testCurrency.getConversionMethod()).isEqualTo(UPDATED_CONVERSION_METHOD);
        assertThat(testCurrency.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testCurrency.getSortOrder()).isEqualTo(UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    void patchNonExistingCurrency() throws Exception {
        int databaseSizeBeforeUpdate = currencyRepository.findAll().size();
        currency.setId(count.incrementAndGet());

        // Create the Currency
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCurrencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, currencyDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(currencyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCurrency() throws Exception {
        int databaseSizeBeforeUpdate = currencyRepository.findAll().size();
        currency.setId(count.incrementAndGet());

        // Create the Currency
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCurrencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(currencyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCurrency() throws Exception {
        int databaseSizeBeforeUpdate = currencyRepository.findAll().size();
        currency.setId(count.incrementAndGet());

        // Create the Currency
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCurrencyMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(currencyDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCurrency() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        int databaseSizeBeforeDelete = currencyRepository.findAll().size();

        // Delete the currency
        restCurrencyMockMvc
            .perform(delete(ENTITY_API_URL_ID, currency.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
