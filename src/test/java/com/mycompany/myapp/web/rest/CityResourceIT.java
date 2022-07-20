package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.City;
import com.mycompany.myapp.domain.District;
import com.mycompany.myapp.repository.CityRepository;
import com.mycompany.myapp.service.criteria.CityCriteria;
import com.mycompany.myapp.service.dto.CityDTO;
import com.mycompany.myapp.service.mapper.CityMapper;
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
 * Integration tests for the {@link CityResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CityResourceIT {

    private static final Integer DEFAULT_CITY_ID = 1;
    private static final Integer UPDATED_CITY_ID = 2;
    private static final Integer SMALLER_CITY_ID = 1 - 1;

    private static final String DEFAULT_CITY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CITY_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CITY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CITY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CITY_ENGLISH_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CITY_ENGLISH_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CityMapper cityMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCityMockMvc;

    private City city;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static City createEntity(EntityManager em) {
        City city = new City()
            .cityId(DEFAULT_CITY_ID)
            .cityCode(DEFAULT_CITY_CODE)
            .cityName(DEFAULT_CITY_NAME)
            .cityEnglishName(DEFAULT_CITY_ENGLISH_NAME);
        return city;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static City createUpdatedEntity(EntityManager em) {
        City city = new City()
            .cityId(UPDATED_CITY_ID)
            .cityCode(UPDATED_CITY_CODE)
            .cityName(UPDATED_CITY_NAME)
            .cityEnglishName(UPDATED_CITY_ENGLISH_NAME);
        return city;
    }

    @BeforeEach
    public void initTest() {
        city = createEntity(em);
    }

    @Test
    @Transactional
    void createCity() throws Exception {
        int databaseSizeBeforeCreate = cityRepository.findAll().size();
        // Create the City
        CityDTO cityDTO = cityMapper.toDto(city);
        restCityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cityDTO)))
            .andExpect(status().isCreated());

        // Validate the City in the database
        List<City> cityList = cityRepository.findAll();
        assertThat(cityList).hasSize(databaseSizeBeforeCreate + 1);
        City testCity = cityList.get(cityList.size() - 1);
        assertThat(testCity.getCityId()).isEqualTo(DEFAULT_CITY_ID);
        assertThat(testCity.getCityCode()).isEqualTo(DEFAULT_CITY_CODE);
        assertThat(testCity.getCityName()).isEqualTo(DEFAULT_CITY_NAME);
        assertThat(testCity.getCityEnglishName()).isEqualTo(DEFAULT_CITY_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void createCityWithExistingId() throws Exception {
        // Create the City with an existing ID
        city.setId(1L);
        CityDTO cityDTO = cityMapper.toDto(city);

        int databaseSizeBeforeCreate = cityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the City in the database
        List<City> cityList = cityRepository.findAll();
        assertThat(cityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCityIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = cityRepository.findAll().size();
        // set the field null
        city.setCityId(null);

        // Create the City, which fails.
        CityDTO cityDTO = cityMapper.toDto(city);

        restCityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cityDTO)))
            .andExpect(status().isBadRequest());

        List<City> cityList = cityRepository.findAll();
        assertThat(cityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCityCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = cityRepository.findAll().size();
        // set the field null
        city.setCityCode(null);

        // Create the City, which fails.
        CityDTO cityDTO = cityMapper.toDto(city);

        restCityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cityDTO)))
            .andExpect(status().isBadRequest());

        List<City> cityList = cityRepository.findAll();
        assertThat(cityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCities() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList
        restCityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(city.getId().intValue())))
            .andExpect(jsonPath("$.[*].cityId").value(hasItem(DEFAULT_CITY_ID)))
            .andExpect(jsonPath("$.[*].cityCode").value(hasItem(DEFAULT_CITY_CODE)))
            .andExpect(jsonPath("$.[*].cityName").value(hasItem(DEFAULT_CITY_NAME)))
            .andExpect(jsonPath("$.[*].cityEnglishName").value(hasItem(DEFAULT_CITY_ENGLISH_NAME)));
    }

    @Test
    @Transactional
    void getCity() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get the city
        restCityMockMvc
            .perform(get(ENTITY_API_URL_ID, city.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(city.getId().intValue()))
            .andExpect(jsonPath("$.cityId").value(DEFAULT_CITY_ID))
            .andExpect(jsonPath("$.cityCode").value(DEFAULT_CITY_CODE))
            .andExpect(jsonPath("$.cityName").value(DEFAULT_CITY_NAME))
            .andExpect(jsonPath("$.cityEnglishName").value(DEFAULT_CITY_ENGLISH_NAME));
    }

    @Test
    @Transactional
    void getCitiesByIdFiltering() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        Long id = city.getId();

        defaultCityShouldBeFound("id.equals=" + id);
        defaultCityShouldNotBeFound("id.notEquals=" + id);

        defaultCityShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCityShouldNotBeFound("id.greaterThan=" + id);

        defaultCityShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCityShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCitiesByCityIdIsEqualToSomething() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where cityId equals to DEFAULT_CITY_ID
        defaultCityShouldBeFound("cityId.equals=" + DEFAULT_CITY_ID);

        // Get all the cityList where cityId equals to UPDATED_CITY_ID
        defaultCityShouldNotBeFound("cityId.equals=" + UPDATED_CITY_ID);
    }

    @Test
    @Transactional
    void getAllCitiesByCityIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where cityId not equals to DEFAULT_CITY_ID
        defaultCityShouldNotBeFound("cityId.notEquals=" + DEFAULT_CITY_ID);

        // Get all the cityList where cityId not equals to UPDATED_CITY_ID
        defaultCityShouldBeFound("cityId.notEquals=" + UPDATED_CITY_ID);
    }

    @Test
    @Transactional
    void getAllCitiesByCityIdIsInShouldWork() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where cityId in DEFAULT_CITY_ID or UPDATED_CITY_ID
        defaultCityShouldBeFound("cityId.in=" + DEFAULT_CITY_ID + "," + UPDATED_CITY_ID);

        // Get all the cityList where cityId equals to UPDATED_CITY_ID
        defaultCityShouldNotBeFound("cityId.in=" + UPDATED_CITY_ID);
    }

    @Test
    @Transactional
    void getAllCitiesByCityIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where cityId is not null
        defaultCityShouldBeFound("cityId.specified=true");

        // Get all the cityList where cityId is null
        defaultCityShouldNotBeFound("cityId.specified=false");
    }

    @Test
    @Transactional
    void getAllCitiesByCityIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where cityId is greater than or equal to DEFAULT_CITY_ID
        defaultCityShouldBeFound("cityId.greaterThanOrEqual=" + DEFAULT_CITY_ID);

        // Get all the cityList where cityId is greater than or equal to UPDATED_CITY_ID
        defaultCityShouldNotBeFound("cityId.greaterThanOrEqual=" + UPDATED_CITY_ID);
    }

    @Test
    @Transactional
    void getAllCitiesByCityIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where cityId is less than or equal to DEFAULT_CITY_ID
        defaultCityShouldBeFound("cityId.lessThanOrEqual=" + DEFAULT_CITY_ID);

        // Get all the cityList where cityId is less than or equal to SMALLER_CITY_ID
        defaultCityShouldNotBeFound("cityId.lessThanOrEqual=" + SMALLER_CITY_ID);
    }

    @Test
    @Transactional
    void getAllCitiesByCityIdIsLessThanSomething() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where cityId is less than DEFAULT_CITY_ID
        defaultCityShouldNotBeFound("cityId.lessThan=" + DEFAULT_CITY_ID);

        // Get all the cityList where cityId is less than UPDATED_CITY_ID
        defaultCityShouldBeFound("cityId.lessThan=" + UPDATED_CITY_ID);
    }

    @Test
    @Transactional
    void getAllCitiesByCityIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where cityId is greater than DEFAULT_CITY_ID
        defaultCityShouldNotBeFound("cityId.greaterThan=" + DEFAULT_CITY_ID);

        // Get all the cityList where cityId is greater than SMALLER_CITY_ID
        defaultCityShouldBeFound("cityId.greaterThan=" + SMALLER_CITY_ID);
    }

    @Test
    @Transactional
    void getAllCitiesByCityCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where cityCode equals to DEFAULT_CITY_CODE
        defaultCityShouldBeFound("cityCode.equals=" + DEFAULT_CITY_CODE);

        // Get all the cityList where cityCode equals to UPDATED_CITY_CODE
        defaultCityShouldNotBeFound("cityCode.equals=" + UPDATED_CITY_CODE);
    }

    @Test
    @Transactional
    void getAllCitiesByCityCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where cityCode not equals to DEFAULT_CITY_CODE
        defaultCityShouldNotBeFound("cityCode.notEquals=" + DEFAULT_CITY_CODE);

        // Get all the cityList where cityCode not equals to UPDATED_CITY_CODE
        defaultCityShouldBeFound("cityCode.notEquals=" + UPDATED_CITY_CODE);
    }

    @Test
    @Transactional
    void getAllCitiesByCityCodeIsInShouldWork() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where cityCode in DEFAULT_CITY_CODE or UPDATED_CITY_CODE
        defaultCityShouldBeFound("cityCode.in=" + DEFAULT_CITY_CODE + "," + UPDATED_CITY_CODE);

        // Get all the cityList where cityCode equals to UPDATED_CITY_CODE
        defaultCityShouldNotBeFound("cityCode.in=" + UPDATED_CITY_CODE);
    }

    @Test
    @Transactional
    void getAllCitiesByCityCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where cityCode is not null
        defaultCityShouldBeFound("cityCode.specified=true");

        // Get all the cityList where cityCode is null
        defaultCityShouldNotBeFound("cityCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCitiesByCityCodeContainsSomething() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where cityCode contains DEFAULT_CITY_CODE
        defaultCityShouldBeFound("cityCode.contains=" + DEFAULT_CITY_CODE);

        // Get all the cityList where cityCode contains UPDATED_CITY_CODE
        defaultCityShouldNotBeFound("cityCode.contains=" + UPDATED_CITY_CODE);
    }

    @Test
    @Transactional
    void getAllCitiesByCityCodeNotContainsSomething() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where cityCode does not contain DEFAULT_CITY_CODE
        defaultCityShouldNotBeFound("cityCode.doesNotContain=" + DEFAULT_CITY_CODE);

        // Get all the cityList where cityCode does not contain UPDATED_CITY_CODE
        defaultCityShouldBeFound("cityCode.doesNotContain=" + UPDATED_CITY_CODE);
    }

    @Test
    @Transactional
    void getAllCitiesByCityNameIsEqualToSomething() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where cityName equals to DEFAULT_CITY_NAME
        defaultCityShouldBeFound("cityName.equals=" + DEFAULT_CITY_NAME);

        // Get all the cityList where cityName equals to UPDATED_CITY_NAME
        defaultCityShouldNotBeFound("cityName.equals=" + UPDATED_CITY_NAME);
    }

    @Test
    @Transactional
    void getAllCitiesByCityNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where cityName not equals to DEFAULT_CITY_NAME
        defaultCityShouldNotBeFound("cityName.notEquals=" + DEFAULT_CITY_NAME);

        // Get all the cityList where cityName not equals to UPDATED_CITY_NAME
        defaultCityShouldBeFound("cityName.notEquals=" + UPDATED_CITY_NAME);
    }

    @Test
    @Transactional
    void getAllCitiesByCityNameIsInShouldWork() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where cityName in DEFAULT_CITY_NAME or UPDATED_CITY_NAME
        defaultCityShouldBeFound("cityName.in=" + DEFAULT_CITY_NAME + "," + UPDATED_CITY_NAME);

        // Get all the cityList where cityName equals to UPDATED_CITY_NAME
        defaultCityShouldNotBeFound("cityName.in=" + UPDATED_CITY_NAME);
    }

    @Test
    @Transactional
    void getAllCitiesByCityNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where cityName is not null
        defaultCityShouldBeFound("cityName.specified=true");

        // Get all the cityList where cityName is null
        defaultCityShouldNotBeFound("cityName.specified=false");
    }

    @Test
    @Transactional
    void getAllCitiesByCityNameContainsSomething() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where cityName contains DEFAULT_CITY_NAME
        defaultCityShouldBeFound("cityName.contains=" + DEFAULT_CITY_NAME);

        // Get all the cityList where cityName contains UPDATED_CITY_NAME
        defaultCityShouldNotBeFound("cityName.contains=" + UPDATED_CITY_NAME);
    }

    @Test
    @Transactional
    void getAllCitiesByCityNameNotContainsSomething() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where cityName does not contain DEFAULT_CITY_NAME
        defaultCityShouldNotBeFound("cityName.doesNotContain=" + DEFAULT_CITY_NAME);

        // Get all the cityList where cityName does not contain UPDATED_CITY_NAME
        defaultCityShouldBeFound("cityName.doesNotContain=" + UPDATED_CITY_NAME);
    }

    @Test
    @Transactional
    void getAllCitiesByCityEnglishNameIsEqualToSomething() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where cityEnglishName equals to DEFAULT_CITY_ENGLISH_NAME
        defaultCityShouldBeFound("cityEnglishName.equals=" + DEFAULT_CITY_ENGLISH_NAME);

        // Get all the cityList where cityEnglishName equals to UPDATED_CITY_ENGLISH_NAME
        defaultCityShouldNotBeFound("cityEnglishName.equals=" + UPDATED_CITY_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void getAllCitiesByCityEnglishNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where cityEnglishName not equals to DEFAULT_CITY_ENGLISH_NAME
        defaultCityShouldNotBeFound("cityEnglishName.notEquals=" + DEFAULT_CITY_ENGLISH_NAME);

        // Get all the cityList where cityEnglishName not equals to UPDATED_CITY_ENGLISH_NAME
        defaultCityShouldBeFound("cityEnglishName.notEquals=" + UPDATED_CITY_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void getAllCitiesByCityEnglishNameIsInShouldWork() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where cityEnglishName in DEFAULT_CITY_ENGLISH_NAME or UPDATED_CITY_ENGLISH_NAME
        defaultCityShouldBeFound("cityEnglishName.in=" + DEFAULT_CITY_ENGLISH_NAME + "," + UPDATED_CITY_ENGLISH_NAME);

        // Get all the cityList where cityEnglishName equals to UPDATED_CITY_ENGLISH_NAME
        defaultCityShouldNotBeFound("cityEnglishName.in=" + UPDATED_CITY_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void getAllCitiesByCityEnglishNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where cityEnglishName is not null
        defaultCityShouldBeFound("cityEnglishName.specified=true");

        // Get all the cityList where cityEnglishName is null
        defaultCityShouldNotBeFound("cityEnglishName.specified=false");
    }

    @Test
    @Transactional
    void getAllCitiesByCityEnglishNameContainsSomething() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where cityEnglishName contains DEFAULT_CITY_ENGLISH_NAME
        defaultCityShouldBeFound("cityEnglishName.contains=" + DEFAULT_CITY_ENGLISH_NAME);

        // Get all the cityList where cityEnglishName contains UPDATED_CITY_ENGLISH_NAME
        defaultCityShouldNotBeFound("cityEnglishName.contains=" + UPDATED_CITY_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void getAllCitiesByCityEnglishNameNotContainsSomething() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get all the cityList where cityEnglishName does not contain DEFAULT_CITY_ENGLISH_NAME
        defaultCityShouldNotBeFound("cityEnglishName.doesNotContain=" + DEFAULT_CITY_ENGLISH_NAME);

        // Get all the cityList where cityEnglishName does not contain UPDATED_CITY_ENGLISH_NAME
        defaultCityShouldBeFound("cityEnglishName.doesNotContain=" + UPDATED_CITY_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void getAllCitiesByDistrictIsEqualToSomething() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);
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
        city.setDistrict(district);
        cityRepository.saveAndFlush(city);
        Long districtId = district.getId();

        // Get all the cityList where district equals to districtId
        defaultCityShouldBeFound("districtId.equals=" + districtId);

        // Get all the cityList where district equals to (districtId + 1)
        defaultCityShouldNotBeFound("districtId.equals=" + (districtId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCityShouldBeFound(String filter) throws Exception {
        restCityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(city.getId().intValue())))
            .andExpect(jsonPath("$.[*].cityId").value(hasItem(DEFAULT_CITY_ID)))
            .andExpect(jsonPath("$.[*].cityCode").value(hasItem(DEFAULT_CITY_CODE)))
            .andExpect(jsonPath("$.[*].cityName").value(hasItem(DEFAULT_CITY_NAME)))
            .andExpect(jsonPath("$.[*].cityEnglishName").value(hasItem(DEFAULT_CITY_ENGLISH_NAME)));

        // Check, that the count call also returns 1
        restCityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCityShouldNotBeFound(String filter) throws Exception {
        restCityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCity() throws Exception {
        // Get the city
        restCityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCity() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        int databaseSizeBeforeUpdate = cityRepository.findAll().size();

        // Update the city
        City updatedCity = cityRepository.findById(city.getId()).get();
        // Disconnect from session so that the updates on updatedCity are not directly saved in db
        em.detach(updatedCity);
        updatedCity
            .cityId(UPDATED_CITY_ID)
            .cityCode(UPDATED_CITY_CODE)
            .cityName(UPDATED_CITY_NAME)
            .cityEnglishName(UPDATED_CITY_ENGLISH_NAME);
        CityDTO cityDTO = cityMapper.toDto(updatedCity);

        restCityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cityDTO))
            )
            .andExpect(status().isOk());

        // Validate the City in the database
        List<City> cityList = cityRepository.findAll();
        assertThat(cityList).hasSize(databaseSizeBeforeUpdate);
        City testCity = cityList.get(cityList.size() - 1);
        assertThat(testCity.getCityId()).isEqualTo(UPDATED_CITY_ID);
        assertThat(testCity.getCityCode()).isEqualTo(UPDATED_CITY_CODE);
        assertThat(testCity.getCityName()).isEqualTo(UPDATED_CITY_NAME);
        assertThat(testCity.getCityEnglishName()).isEqualTo(UPDATED_CITY_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void putNonExistingCity() throws Exception {
        int databaseSizeBeforeUpdate = cityRepository.findAll().size();
        city.setId(count.incrementAndGet());

        // Create the City
        CityDTO cityDTO = cityMapper.toDto(city);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the City in the database
        List<City> cityList = cityRepository.findAll();
        assertThat(cityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCity() throws Exception {
        int databaseSizeBeforeUpdate = cityRepository.findAll().size();
        city.setId(count.incrementAndGet());

        // Create the City
        CityDTO cityDTO = cityMapper.toDto(city);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the City in the database
        List<City> cityList = cityRepository.findAll();
        assertThat(cityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCity() throws Exception {
        int databaseSizeBeforeUpdate = cityRepository.findAll().size();
        city.setId(count.incrementAndGet());

        // Create the City
        CityDTO cityDTO = cityMapper.toDto(city);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cityDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the City in the database
        List<City> cityList = cityRepository.findAll();
        assertThat(cityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCityWithPatch() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        int databaseSizeBeforeUpdate = cityRepository.findAll().size();

        // Update the city using partial update
        City partialUpdatedCity = new City();
        partialUpdatedCity.setId(city.getId());

        partialUpdatedCity.cityCode(UPDATED_CITY_CODE);

        restCityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCity))
            )
            .andExpect(status().isOk());

        // Validate the City in the database
        List<City> cityList = cityRepository.findAll();
        assertThat(cityList).hasSize(databaseSizeBeforeUpdate);
        City testCity = cityList.get(cityList.size() - 1);
        assertThat(testCity.getCityId()).isEqualTo(DEFAULT_CITY_ID);
        assertThat(testCity.getCityCode()).isEqualTo(UPDATED_CITY_CODE);
        assertThat(testCity.getCityName()).isEqualTo(DEFAULT_CITY_NAME);
        assertThat(testCity.getCityEnglishName()).isEqualTo(DEFAULT_CITY_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void fullUpdateCityWithPatch() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        int databaseSizeBeforeUpdate = cityRepository.findAll().size();

        // Update the city using partial update
        City partialUpdatedCity = new City();
        partialUpdatedCity.setId(city.getId());

        partialUpdatedCity
            .cityId(UPDATED_CITY_ID)
            .cityCode(UPDATED_CITY_CODE)
            .cityName(UPDATED_CITY_NAME)
            .cityEnglishName(UPDATED_CITY_ENGLISH_NAME);

        restCityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCity))
            )
            .andExpect(status().isOk());

        // Validate the City in the database
        List<City> cityList = cityRepository.findAll();
        assertThat(cityList).hasSize(databaseSizeBeforeUpdate);
        City testCity = cityList.get(cityList.size() - 1);
        assertThat(testCity.getCityId()).isEqualTo(UPDATED_CITY_ID);
        assertThat(testCity.getCityCode()).isEqualTo(UPDATED_CITY_CODE);
        assertThat(testCity.getCityName()).isEqualTo(UPDATED_CITY_NAME);
        assertThat(testCity.getCityEnglishName()).isEqualTo(UPDATED_CITY_ENGLISH_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingCity() throws Exception {
        int databaseSizeBeforeUpdate = cityRepository.findAll().size();
        city.setId(count.incrementAndGet());

        // Create the City
        CityDTO cityDTO = cityMapper.toDto(city);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cityDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the City in the database
        List<City> cityList = cityRepository.findAll();
        assertThat(cityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCity() throws Exception {
        int databaseSizeBeforeUpdate = cityRepository.findAll().size();
        city.setId(count.incrementAndGet());

        // Create the City
        CityDTO cityDTO = cityMapper.toDto(city);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the City in the database
        List<City> cityList = cityRepository.findAll();
        assertThat(cityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCity() throws Exception {
        int databaseSizeBeforeUpdate = cityRepository.findAll().size();
        city.setId(count.incrementAndGet());

        // Create the City
        CityDTO cityDTO = cityMapper.toDto(city);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCityMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cityDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the City in the database
        List<City> cityList = cityRepository.findAll();
        assertThat(cityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCity() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        int databaseSizeBeforeDelete = cityRepository.findAll().size();

        // Delete the city
        restCityMockMvc
            .perform(delete(ENTITY_API_URL_ID, city.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<City> cityList = cityRepository.findAll();
        assertThat(cityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
