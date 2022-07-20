package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Country;
import com.mycompany.myapp.domain.Province;
import com.mycompany.myapp.repository.ProvinceRepository;
import com.mycompany.myapp.service.criteria.ProvinceCriteria;
import com.mycompany.myapp.service.dto.ProvinceDTO;
import com.mycompany.myapp.service.mapper.ProvinceMapper;
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
 * Integration tests for the {@link ProvinceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProvinceResourceIT {

    private static final Integer DEFAULT_PROVINCE_ID = 1;
    private static final Integer UPDATED_PROVINCE_ID = 2;
    private static final Integer SMALLER_PROVINCE_ID = 1 - 1;

    private static final String DEFAULT_PROVINCE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PROVINCE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_PROVINCE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PROVINCE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PROVINCE_ENGLISH_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PROVINCE_ENGLISH_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DIALLING_CODE = "AAAAAAAAAA";
    private static final String UPDATED_DIALLING_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/provinces";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private ProvinceMapper provinceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProvinceMockMvc;

    private Province province;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Province createEntity(EntityManager em) {
        Province province = new Province()
            .provinceId(DEFAULT_PROVINCE_ID)
            .provinceCode(DEFAULT_PROVINCE_CODE)
            .provinceName(DEFAULT_PROVINCE_NAME)
            .provinceEnglishName(DEFAULT_PROVINCE_ENGLISH_NAME)
            .diallingCode(DEFAULT_DIALLING_CODE);
        return province;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Province createUpdatedEntity(EntityManager em) {
        Province province = new Province()
            .provinceId(UPDATED_PROVINCE_ID)
            .provinceCode(UPDATED_PROVINCE_CODE)
            .provinceName(UPDATED_PROVINCE_NAME)
            .provinceEnglishName(UPDATED_PROVINCE_ENGLISH_NAME)
            .diallingCode(UPDATED_DIALLING_CODE);
        return province;
    }

    @BeforeEach
    public void initTest() {
        province = createEntity(em);
    }

    @Test
    @Transactional
    void createProvince() throws Exception {
        int databaseSizeBeforeCreate = provinceRepository.findAll().size();
        // Create the Province
        ProvinceDTO provinceDTO = provinceMapper.toDto(province);
        restProvinceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(provinceDTO)))
            .andExpect(status().isCreated());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeCreate + 1);
        Province testProvince = provinceList.get(provinceList.size() - 1);
        assertThat(testProvince.getProvinceId()).isEqualTo(DEFAULT_PROVINCE_ID);
        assertThat(testProvince.getProvinceCode()).isEqualTo(DEFAULT_PROVINCE_CODE);
        assertThat(testProvince.getProvinceName()).isEqualTo(DEFAULT_PROVINCE_NAME);
        assertThat(testProvince.getProvinceEnglishName()).isEqualTo(DEFAULT_PROVINCE_ENGLISH_NAME);
        assertThat(testProvince.getDiallingCode()).isEqualTo(DEFAULT_DIALLING_CODE);
    }

    @Test
    @Transactional
    void createProvinceWithExistingId() throws Exception {
        // Create the Province with an existing ID
        province.setId(1L);
        ProvinceDTO provinceDTO = provinceMapper.toDto(province);

        int databaseSizeBeforeCreate = provinceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProvinceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(provinceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkProvinceIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = provinceRepository.findAll().size();
        // set the field null
        province.setProvinceId(null);

        // Create the Province, which fails.
        ProvinceDTO provinceDTO = provinceMapper.toDto(province);

        restProvinceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(provinceDTO)))
            .andExpect(status().isBadRequest());

        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProvinceCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = provinceRepository.findAll().size();
        // set the field null
        province.setProvinceCode(null);

        // Create the Province, which fails.
        ProvinceDTO provinceDTO = provinceMapper.toDto(province);

        restProvinceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(provinceDTO)))
            .andExpect(status().isBadRequest());

        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProvinces() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList
        restProvinceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(province.getId().intValue())))
            .andExpect(jsonPath("$.[*].provinceId").value(hasItem(DEFAULT_PROVINCE_ID)))
            .andExpect(jsonPath("$.[*].provinceCode").value(hasItem(DEFAULT_PROVINCE_CODE)))
            .andExpect(jsonPath("$.[*].provinceName").value(hasItem(DEFAULT_PROVINCE_NAME)))
            .andExpect(jsonPath("$.[*].provinceEnglishName").value(hasItem(DEFAULT_PROVINCE_ENGLISH_NAME)))
            .andExpect(jsonPath("$.[*].diallingCode").value(hasItem(DEFAULT_DIALLING_CODE)));
    }

    @Test
    @Transactional
    void getProvince() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get the province
        restProvinceMockMvc
            .perform(get(ENTITY_API_URL_ID, province.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(province.getId().intValue()))
            .andExpect(jsonPath("$.provinceId").value(DEFAULT_PROVINCE_ID))
            .andExpect(jsonPath("$.provinceCode").value(DEFAULT_PROVINCE_CODE))
            .andExpect(jsonPath("$.provinceName").value(DEFAULT_PROVINCE_NAME))
            .andExpect(jsonPath("$.provinceEnglishName").value(DEFAULT_PROVINCE_ENGLISH_NAME))
            .andExpect(jsonPath("$.diallingCode").value(DEFAULT_DIALLING_CODE));
    }

    @Test
    @Transactional
    void getProvincesByIdFiltering() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        Long id = province.getId();

        defaultProvinceShouldBeFound("id.equals=" + id);
        defaultProvinceShouldNotBeFound("id.notEquals=" + id);

        defaultProvinceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProvinceShouldNotBeFound("id.greaterThan=" + id);

        defaultProvinceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProvinceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProvincesByProvinceIdIsEqualToSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where provinceId equals to DEFAULT_PROVINCE_ID
        defaultProvinceShouldBeFound("provinceId.equals=" + DEFAULT_PROVINCE_ID);

        // Get all the provinceList where provinceId equals to UPDATED_PROVINCE_ID
        defaultProvinceShouldNotBeFound("provinceId.equals=" + UPDATED_PROVINCE_ID);
    }

    @Test
    @Transactional
    void getAllProvincesByProvinceIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where provinceId not equals to DEFAULT_PROVINCE_ID
        defaultProvinceShouldNotBeFound("provinceId.notEquals=" + DEFAULT_PROVINCE_ID);

        // Get all the provinceList where provinceId not equals to UPDATED_PROVINCE_ID
        defaultProvinceShouldBeFound("provinceId.notEquals=" + UPDATED_PROVINCE_ID);
    }

    @Test
    @Transactional
    void getAllProvincesByProvinceIdIsInShouldWork() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where provinceId in DEFAULT_PROVINCE_ID or UPDATED_PROVINCE_ID
        defaultProvinceShouldBeFound("provinceId.in=" + DEFAULT_PROVINCE_ID + "," + UPDATED_PROVINCE_ID);

        // Get all the provinceList where provinceId equals to UPDATED_PROVINCE_ID
        defaultProvinceShouldNotBeFound("provinceId.in=" + UPDATED_PROVINCE_ID);
    }

    @Test
    @Transactional
    void getAllProvincesByProvinceIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where provinceId is not null
        defaultProvinceShouldBeFound("provinceId.specified=true");

        // Get all the provinceList where provinceId is null
        defaultProvinceShouldNotBeFound("provinceId.specified=false");
    }

    @Test
    @Transactional
    void getAllProvincesByProvinceIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where provinceId is greater than or equal to DEFAULT_PROVINCE_ID
        defaultProvinceShouldBeFound("provinceId.greaterThanOrEqual=" + DEFAULT_PROVINCE_ID);

        // Get all the provinceList where provinceId is greater than or equal to UPDATED_PROVINCE_ID
        defaultProvinceShouldNotBeFound("provinceId.greaterThanOrEqual=" + UPDATED_PROVINCE_ID);
    }

    @Test
    @Transactional
    void getAllProvincesByProvinceIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where provinceId is less than or equal to DEFAULT_PROVINCE_ID
        defaultProvinceShouldBeFound("provinceId.lessThanOrEqual=" + DEFAULT_PROVINCE_ID);

        // Get all the provinceList where provinceId is less than or equal to SMALLER_PROVINCE_ID
        defaultProvinceShouldNotBeFound("provinceId.lessThanOrEqual=" + SMALLER_PROVINCE_ID);
    }

    @Test
    @Transactional
    void getAllProvincesByProvinceIdIsLessThanSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where provinceId is less than DEFAULT_PROVINCE_ID
        defaultProvinceShouldNotBeFound("provinceId.lessThan=" + DEFAULT_PROVINCE_ID);

        // Get all the provinceList where provinceId is less than UPDATED_PROVINCE_ID
        defaultProvinceShouldBeFound("provinceId.lessThan=" + UPDATED_PROVINCE_ID);
    }

    @Test
    @Transactional
    void getAllProvincesByProvinceIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where provinceId is greater than DEFAULT_PROVINCE_ID
        defaultProvinceShouldNotBeFound("provinceId.greaterThan=" + DEFAULT_PROVINCE_ID);

        // Get all the provinceList where provinceId is greater than SMALLER_PROVINCE_ID
        defaultProvinceShouldBeFound("provinceId.greaterThan=" + SMALLER_PROVINCE_ID);
    }

    @Test
    @Transactional
    void getAllProvincesByProvinceCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where provinceCode equals to DEFAULT_PROVINCE_CODE
        defaultProvinceShouldBeFound("provinceCode.equals=" + DEFAULT_PROVINCE_CODE);

        // Get all the provinceList where provinceCode equals to UPDATED_PROVINCE_CODE
        defaultProvinceShouldNotBeFound("provinceCode.equals=" + UPDATED_PROVINCE_CODE);
    }

    @Test
    @Transactional
    void getAllProvincesByProvinceCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where provinceCode not equals to DEFAULT_PROVINCE_CODE
        defaultProvinceShouldNotBeFound("provinceCode.notEquals=" + DEFAULT_PROVINCE_CODE);

        // Get all the provinceList where provinceCode not equals to UPDATED_PROVINCE_CODE
        defaultProvinceShouldBeFound("provinceCode.notEquals=" + UPDATED_PROVINCE_CODE);
    }

    @Test
    @Transactional
    void getAllProvincesByProvinceCodeIsInShouldWork() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where provinceCode in DEFAULT_PROVINCE_CODE or UPDATED_PROVINCE_CODE
        defaultProvinceShouldBeFound("provinceCode.in=" + DEFAULT_PROVINCE_CODE + "," + UPDATED_PROVINCE_CODE);

        // Get all the provinceList where provinceCode equals to UPDATED_PROVINCE_CODE
        defaultProvinceShouldNotBeFound("provinceCode.in=" + UPDATED_PROVINCE_CODE);
    }

    @Test
    @Transactional
    void getAllProvincesByProvinceCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where provinceCode is not null
        defaultProvinceShouldBeFound("provinceCode.specified=true");

        // Get all the provinceList where provinceCode is null
        defaultProvinceShouldNotBeFound("provinceCode.specified=false");
    }

    @Test
    @Transactional
    void getAllProvincesByProvinceCodeContainsSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where provinceCode contains DEFAULT_PROVINCE_CODE
        defaultProvinceShouldBeFound("provinceCode.contains=" + DEFAULT_PROVINCE_CODE);

        // Get all the provinceList where provinceCode contains UPDATED_PROVINCE_CODE
        defaultProvinceShouldNotBeFound("provinceCode.contains=" + UPDATED_PROVINCE_CODE);
    }

    @Test
    @Transactional
    void getAllProvincesByProvinceCodeNotContainsSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where provinceCode does not contain DEFAULT_PROVINCE_CODE
        defaultProvinceShouldNotBeFound("provinceCode.doesNotContain=" + DEFAULT_PROVINCE_CODE);

        // Get all the provinceList where provinceCode does not contain UPDATED_PROVINCE_CODE
        defaultProvinceShouldBeFound("provinceCode.doesNotContain=" + UPDATED_PROVINCE_CODE);
    }

    @Test
    @Transactional
    void getAllProvincesByProvinceNameIsEqualToSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where provinceName equals to DEFAULT_PROVINCE_NAME
        defaultProvinceShouldBeFound("provinceName.equals=" + DEFAULT_PROVINCE_NAME);

        // Get all the provinceList where provinceName equals to UPDATED_PROVINCE_NAME
        defaultProvinceShouldNotBeFound("provinceName.equals=" + UPDATED_PROVINCE_NAME);
    }

    @Test
    @Transactional
    void getAllProvincesByProvinceNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where provinceName not equals to DEFAULT_PROVINCE_NAME
        defaultProvinceShouldNotBeFound("provinceName.notEquals=" + DEFAULT_PROVINCE_NAME);

        // Get all the provinceList where provinceName not equals to UPDATED_PROVINCE_NAME
        defaultProvinceShouldBeFound("provinceName.notEquals=" + UPDATED_PROVINCE_NAME);
    }

    @Test
    @Transactional
    void getAllProvincesByProvinceNameIsInShouldWork() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where provinceName in DEFAULT_PROVINCE_NAME or UPDATED_PROVINCE_NAME
        defaultProvinceShouldBeFound("provinceName.in=" + DEFAULT_PROVINCE_NAME + "," + UPDATED_PROVINCE_NAME);

        // Get all the provinceList where provinceName equals to UPDATED_PROVINCE_NAME
        defaultProvinceShouldNotBeFound("provinceName.in=" + UPDATED_PROVINCE_NAME);
    }

    @Test
    @Transactional
    void getAllProvincesByProvinceNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where provinceName is not null
        defaultProvinceShouldBeFound("provinceName.specified=true");

        // Get all the provinceList where provinceName is null
        defaultProvinceShouldNotBeFound("provinceName.specified=false");
    }

    @Test
    @Transactional
    void getAllProvincesByProvinceNameContainsSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where provinceName contains DEFAULT_PROVINCE_NAME
        defaultProvinceShouldBeFound("provinceName.contains=" + DEFAULT_PROVINCE_NAME);

        // Get all the provinceList where provinceName contains UPDATED_PROVINCE_NAME
        defaultProvinceShouldNotBeFound("provinceName.contains=" + UPDATED_PROVINCE_NAME);
    }

    @Test
    @Transactional
    void getAllProvincesByProvinceNameNotContainsSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where provinceName does not contain DEFAULT_PROVINCE_NAME
        defaultProvinceShouldNotBeFound("provinceName.doesNotContain=" + DEFAULT_PROVINCE_NAME);

        // Get all the provinceList where provinceName does not contain UPDATED_PROVINCE_NAME
        defaultProvinceShouldBeFound("provinceName.doesNotContain=" + UPDATED_PROVINCE_NAME);
    }

    @Test
    @Transactional
    void getAllProvincesByProvinceEnglishNameIsEqualToSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where provinceEnglishName equals to DEFAULT_PROVINCE_ENGLISH_NAME
        defaultProvinceShouldBeFound("provinceEnglishName.equals=" + DEFAULT_PROVINCE_ENGLISH_NAME);

        // Get all the provinceList where provinceEnglishName equals to UPDATED_PROVINCE_ENGLISH_NAME
        defaultProvinceShouldNotBeFound("provinceEnglishName.equals=" + UPDATED_PROVINCE_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void getAllProvincesByProvinceEnglishNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where provinceEnglishName not equals to DEFAULT_PROVINCE_ENGLISH_NAME
        defaultProvinceShouldNotBeFound("provinceEnglishName.notEquals=" + DEFAULT_PROVINCE_ENGLISH_NAME);

        // Get all the provinceList where provinceEnglishName not equals to UPDATED_PROVINCE_ENGLISH_NAME
        defaultProvinceShouldBeFound("provinceEnglishName.notEquals=" + UPDATED_PROVINCE_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void getAllProvincesByProvinceEnglishNameIsInShouldWork() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where provinceEnglishName in DEFAULT_PROVINCE_ENGLISH_NAME or UPDATED_PROVINCE_ENGLISH_NAME
        defaultProvinceShouldBeFound("provinceEnglishName.in=" + DEFAULT_PROVINCE_ENGLISH_NAME + "," + UPDATED_PROVINCE_ENGLISH_NAME);

        // Get all the provinceList where provinceEnglishName equals to UPDATED_PROVINCE_ENGLISH_NAME
        defaultProvinceShouldNotBeFound("provinceEnglishName.in=" + UPDATED_PROVINCE_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void getAllProvincesByProvinceEnglishNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where provinceEnglishName is not null
        defaultProvinceShouldBeFound("provinceEnglishName.specified=true");

        // Get all the provinceList where provinceEnglishName is null
        defaultProvinceShouldNotBeFound("provinceEnglishName.specified=false");
    }

    @Test
    @Transactional
    void getAllProvincesByProvinceEnglishNameContainsSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where provinceEnglishName contains DEFAULT_PROVINCE_ENGLISH_NAME
        defaultProvinceShouldBeFound("provinceEnglishName.contains=" + DEFAULT_PROVINCE_ENGLISH_NAME);

        // Get all the provinceList where provinceEnglishName contains UPDATED_PROVINCE_ENGLISH_NAME
        defaultProvinceShouldNotBeFound("provinceEnglishName.contains=" + UPDATED_PROVINCE_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void getAllProvincesByProvinceEnglishNameNotContainsSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where provinceEnglishName does not contain DEFAULT_PROVINCE_ENGLISH_NAME
        defaultProvinceShouldNotBeFound("provinceEnglishName.doesNotContain=" + DEFAULT_PROVINCE_ENGLISH_NAME);

        // Get all the provinceList where provinceEnglishName does not contain UPDATED_PROVINCE_ENGLISH_NAME
        defaultProvinceShouldBeFound("provinceEnglishName.doesNotContain=" + UPDATED_PROVINCE_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void getAllProvincesByDiallingCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where diallingCode equals to DEFAULT_DIALLING_CODE
        defaultProvinceShouldBeFound("diallingCode.equals=" + DEFAULT_DIALLING_CODE);

        // Get all the provinceList where diallingCode equals to UPDATED_DIALLING_CODE
        defaultProvinceShouldNotBeFound("diallingCode.equals=" + UPDATED_DIALLING_CODE);
    }

    @Test
    @Transactional
    void getAllProvincesByDiallingCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where diallingCode not equals to DEFAULT_DIALLING_CODE
        defaultProvinceShouldNotBeFound("diallingCode.notEquals=" + DEFAULT_DIALLING_CODE);

        // Get all the provinceList where diallingCode not equals to UPDATED_DIALLING_CODE
        defaultProvinceShouldBeFound("diallingCode.notEquals=" + UPDATED_DIALLING_CODE);
    }

    @Test
    @Transactional
    void getAllProvincesByDiallingCodeIsInShouldWork() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where diallingCode in DEFAULT_DIALLING_CODE or UPDATED_DIALLING_CODE
        defaultProvinceShouldBeFound("diallingCode.in=" + DEFAULT_DIALLING_CODE + "," + UPDATED_DIALLING_CODE);

        // Get all the provinceList where diallingCode equals to UPDATED_DIALLING_CODE
        defaultProvinceShouldNotBeFound("diallingCode.in=" + UPDATED_DIALLING_CODE);
    }

    @Test
    @Transactional
    void getAllProvincesByDiallingCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where diallingCode is not null
        defaultProvinceShouldBeFound("diallingCode.specified=true");

        // Get all the provinceList where diallingCode is null
        defaultProvinceShouldNotBeFound("diallingCode.specified=false");
    }

    @Test
    @Transactional
    void getAllProvincesByDiallingCodeContainsSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where diallingCode contains DEFAULT_DIALLING_CODE
        defaultProvinceShouldBeFound("diallingCode.contains=" + DEFAULT_DIALLING_CODE);

        // Get all the provinceList where diallingCode contains UPDATED_DIALLING_CODE
        defaultProvinceShouldNotBeFound("diallingCode.contains=" + UPDATED_DIALLING_CODE);
    }

    @Test
    @Transactional
    void getAllProvincesByDiallingCodeNotContainsSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where diallingCode does not contain DEFAULT_DIALLING_CODE
        defaultProvinceShouldNotBeFound("diallingCode.doesNotContain=" + DEFAULT_DIALLING_CODE);

        // Get all the provinceList where diallingCode does not contain UPDATED_DIALLING_CODE
        defaultProvinceShouldBeFound("diallingCode.doesNotContain=" + UPDATED_DIALLING_CODE);
    }

    @Test
    @Transactional
    void getAllProvincesByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);
        Country country;
        if (TestUtil.findAll(em, Country.class).isEmpty()) {
            country = CountryResourceIT.createEntity(em);
            em.persist(country);
            em.flush();
        } else {
            country = TestUtil.findAll(em, Country.class).get(0);
        }
        em.persist(country);
        em.flush();
        province.setCountry(country);
        provinceRepository.saveAndFlush(province);
        Long countryId = country.getId();

        // Get all the provinceList where country equals to countryId
        defaultProvinceShouldBeFound("countryId.equals=" + countryId);

        // Get all the provinceList where country equals to (countryId + 1)
        defaultProvinceShouldNotBeFound("countryId.equals=" + (countryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProvinceShouldBeFound(String filter) throws Exception {
        restProvinceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(province.getId().intValue())))
            .andExpect(jsonPath("$.[*].provinceId").value(hasItem(DEFAULT_PROVINCE_ID)))
            .andExpect(jsonPath("$.[*].provinceCode").value(hasItem(DEFAULT_PROVINCE_CODE)))
            .andExpect(jsonPath("$.[*].provinceName").value(hasItem(DEFAULT_PROVINCE_NAME)))
            .andExpect(jsonPath("$.[*].provinceEnglishName").value(hasItem(DEFAULT_PROVINCE_ENGLISH_NAME)))
            .andExpect(jsonPath("$.[*].diallingCode").value(hasItem(DEFAULT_DIALLING_CODE)));

        // Check, that the count call also returns 1
        restProvinceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProvinceShouldNotBeFound(String filter) throws Exception {
        restProvinceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProvinceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProvince() throws Exception {
        // Get the province
        restProvinceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProvince() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        int databaseSizeBeforeUpdate = provinceRepository.findAll().size();

        // Update the province
        Province updatedProvince = provinceRepository.findById(province.getId()).get();
        // Disconnect from session so that the updates on updatedProvince are not directly saved in db
        em.detach(updatedProvince);
        updatedProvince
            .provinceId(UPDATED_PROVINCE_ID)
            .provinceCode(UPDATED_PROVINCE_CODE)
            .provinceName(UPDATED_PROVINCE_NAME)
            .provinceEnglishName(UPDATED_PROVINCE_ENGLISH_NAME)
            .diallingCode(UPDATED_DIALLING_CODE);
        ProvinceDTO provinceDTO = provinceMapper.toDto(updatedProvince);

        restProvinceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, provinceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(provinceDTO))
            )
            .andExpect(status().isOk());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
        Province testProvince = provinceList.get(provinceList.size() - 1);
        assertThat(testProvince.getProvinceId()).isEqualTo(UPDATED_PROVINCE_ID);
        assertThat(testProvince.getProvinceCode()).isEqualTo(UPDATED_PROVINCE_CODE);
        assertThat(testProvince.getProvinceName()).isEqualTo(UPDATED_PROVINCE_NAME);
        assertThat(testProvince.getProvinceEnglishName()).isEqualTo(UPDATED_PROVINCE_ENGLISH_NAME);
        assertThat(testProvince.getDiallingCode()).isEqualTo(UPDATED_DIALLING_CODE);
    }

    @Test
    @Transactional
    void putNonExistingProvince() throws Exception {
        int databaseSizeBeforeUpdate = provinceRepository.findAll().size();
        province.setId(count.incrementAndGet());

        // Create the Province
        ProvinceDTO provinceDTO = provinceMapper.toDto(province);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProvinceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, provinceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(provinceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProvince() throws Exception {
        int databaseSizeBeforeUpdate = provinceRepository.findAll().size();
        province.setId(count.incrementAndGet());

        // Create the Province
        ProvinceDTO provinceDTO = provinceMapper.toDto(province);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProvinceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(provinceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProvince() throws Exception {
        int databaseSizeBeforeUpdate = provinceRepository.findAll().size();
        province.setId(count.incrementAndGet());

        // Create the Province
        ProvinceDTO provinceDTO = provinceMapper.toDto(province);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProvinceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(provinceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProvinceWithPatch() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        int databaseSizeBeforeUpdate = provinceRepository.findAll().size();

        // Update the province using partial update
        Province partialUpdatedProvince = new Province();
        partialUpdatedProvince.setId(province.getId());

        partialUpdatedProvince.provinceId(UPDATED_PROVINCE_ID).provinceName(UPDATED_PROVINCE_NAME);

        restProvinceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProvince.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProvince))
            )
            .andExpect(status().isOk());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
        Province testProvince = provinceList.get(provinceList.size() - 1);
        assertThat(testProvince.getProvinceId()).isEqualTo(UPDATED_PROVINCE_ID);
        assertThat(testProvince.getProvinceCode()).isEqualTo(DEFAULT_PROVINCE_CODE);
        assertThat(testProvince.getProvinceName()).isEqualTo(UPDATED_PROVINCE_NAME);
        assertThat(testProvince.getProvinceEnglishName()).isEqualTo(DEFAULT_PROVINCE_ENGLISH_NAME);
        assertThat(testProvince.getDiallingCode()).isEqualTo(DEFAULT_DIALLING_CODE);
    }

    @Test
    @Transactional
    void fullUpdateProvinceWithPatch() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        int databaseSizeBeforeUpdate = provinceRepository.findAll().size();

        // Update the province using partial update
        Province partialUpdatedProvince = new Province();
        partialUpdatedProvince.setId(province.getId());

        partialUpdatedProvince
            .provinceId(UPDATED_PROVINCE_ID)
            .provinceCode(UPDATED_PROVINCE_CODE)
            .provinceName(UPDATED_PROVINCE_NAME)
            .provinceEnglishName(UPDATED_PROVINCE_ENGLISH_NAME)
            .diallingCode(UPDATED_DIALLING_CODE);

        restProvinceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProvince.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProvince))
            )
            .andExpect(status().isOk());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
        Province testProvince = provinceList.get(provinceList.size() - 1);
        assertThat(testProvince.getProvinceId()).isEqualTo(UPDATED_PROVINCE_ID);
        assertThat(testProvince.getProvinceCode()).isEqualTo(UPDATED_PROVINCE_CODE);
        assertThat(testProvince.getProvinceName()).isEqualTo(UPDATED_PROVINCE_NAME);
        assertThat(testProvince.getProvinceEnglishName()).isEqualTo(UPDATED_PROVINCE_ENGLISH_NAME);
        assertThat(testProvince.getDiallingCode()).isEqualTo(UPDATED_DIALLING_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingProvince() throws Exception {
        int databaseSizeBeforeUpdate = provinceRepository.findAll().size();
        province.setId(count.incrementAndGet());

        // Create the Province
        ProvinceDTO provinceDTO = provinceMapper.toDto(province);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProvinceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, provinceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(provinceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProvince() throws Exception {
        int databaseSizeBeforeUpdate = provinceRepository.findAll().size();
        province.setId(count.incrementAndGet());

        // Create the Province
        ProvinceDTO provinceDTO = provinceMapper.toDto(province);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProvinceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(provinceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProvince() throws Exception {
        int databaseSizeBeforeUpdate = provinceRepository.findAll().size();
        province.setId(count.incrementAndGet());

        // Create the Province
        ProvinceDTO provinceDTO = provinceMapper.toDto(province);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProvinceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(provinceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProvince() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        int databaseSizeBeforeDelete = provinceRepository.findAll().size();

        // Delete the province
        restProvinceMockMvc
            .perform(delete(ENTITY_API_URL_ID, province.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
