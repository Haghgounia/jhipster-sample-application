package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.AssetAssign;
import com.mycompany.myapp.repository.AssetAssignRepository;
import com.mycompany.myapp.service.criteria.AssetAssignCriteria;
import com.mycompany.myapp.service.dto.AssetAssignDTO;
import com.mycompany.myapp.service.mapper.AssetAssignMapper;
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
 * Integration tests for the {@link AssetAssignResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AssetAssignResourceIT {

    private static final Integer DEFAULT_ASSET_ID = 1;
    private static final Integer UPDATED_ASSET_ID = 2;
    private static final Integer SMALLER_ASSET_ID = 1 - 1;

    private static final Integer DEFAULT_EMPLOYEE_ID = 1;
    private static final Integer UPDATED_EMPLOYEE_ID = 2;
    private static final Integer SMALLER_EMPLOYEE_ID = 1 - 1;

    private static final Integer DEFAULT_ASSIGN_DATE = 1;
    private static final Integer UPDATED_ASSIGN_DATE = 2;
    private static final Integer SMALLER_ASSIGN_DATE = 1 - 1;

    private static final Integer DEFAULT_RETURN_DATE = 1;
    private static final Integer UPDATED_RETURN_DATE = 2;
    private static final Integer SMALLER_RETURN_DATE = 1 - 1;

    private static final String ENTITY_API_URL = "/api/asset-assigns";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AssetAssignRepository assetAssignRepository;

    @Autowired
    private AssetAssignMapper assetAssignMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAssetAssignMockMvc;

    private AssetAssign assetAssign;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssetAssign createEntity(EntityManager em) {
        AssetAssign assetAssign = new AssetAssign()
            .assetId(DEFAULT_ASSET_ID)
            .employeeId(DEFAULT_EMPLOYEE_ID)
            .assignDate(DEFAULT_ASSIGN_DATE)
            .returnDate(DEFAULT_RETURN_DATE);
        return assetAssign;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssetAssign createUpdatedEntity(EntityManager em) {
        AssetAssign assetAssign = new AssetAssign()
            .assetId(UPDATED_ASSET_ID)
            .employeeId(UPDATED_EMPLOYEE_ID)
            .assignDate(UPDATED_ASSIGN_DATE)
            .returnDate(UPDATED_RETURN_DATE);
        return assetAssign;
    }

    @BeforeEach
    public void initTest() {
        assetAssign = createEntity(em);
    }

    @Test
    @Transactional
    void createAssetAssign() throws Exception {
        int databaseSizeBeforeCreate = assetAssignRepository.findAll().size();
        // Create the AssetAssign
        AssetAssignDTO assetAssignDTO = assetAssignMapper.toDto(assetAssign);
        restAssetAssignMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetAssignDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AssetAssign in the database
        List<AssetAssign> assetAssignList = assetAssignRepository.findAll();
        assertThat(assetAssignList).hasSize(databaseSizeBeforeCreate + 1);
        AssetAssign testAssetAssign = assetAssignList.get(assetAssignList.size() - 1);
        assertThat(testAssetAssign.getAssetId()).isEqualTo(DEFAULT_ASSET_ID);
        assertThat(testAssetAssign.getEmployeeId()).isEqualTo(DEFAULT_EMPLOYEE_ID);
        assertThat(testAssetAssign.getAssignDate()).isEqualTo(DEFAULT_ASSIGN_DATE);
        assertThat(testAssetAssign.getReturnDate()).isEqualTo(DEFAULT_RETURN_DATE);
    }

    @Test
    @Transactional
    void createAssetAssignWithExistingId() throws Exception {
        // Create the AssetAssign with an existing ID
        assetAssign.setId(1L);
        AssetAssignDTO assetAssignDTO = assetAssignMapper.toDto(assetAssign);

        int databaseSizeBeforeCreate = assetAssignRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssetAssignMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetAssignDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetAssign in the database
        List<AssetAssign> assetAssignList = assetAssignRepository.findAll();
        assertThat(assetAssignList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAssetAssigns() throws Exception {
        // Initialize the database
        assetAssignRepository.saveAndFlush(assetAssign);

        // Get all the assetAssignList
        restAssetAssignMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetAssign.getId().intValue())))
            .andExpect(jsonPath("$.[*].assetId").value(hasItem(DEFAULT_ASSET_ID)))
            .andExpect(jsonPath("$.[*].employeeId").value(hasItem(DEFAULT_EMPLOYEE_ID)))
            .andExpect(jsonPath("$.[*].assignDate").value(hasItem(DEFAULT_ASSIGN_DATE)))
            .andExpect(jsonPath("$.[*].returnDate").value(hasItem(DEFAULT_RETURN_DATE)));
    }

    @Test
    @Transactional
    void getAssetAssign() throws Exception {
        // Initialize the database
        assetAssignRepository.saveAndFlush(assetAssign);

        // Get the assetAssign
        restAssetAssignMockMvc
            .perform(get(ENTITY_API_URL_ID, assetAssign.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(assetAssign.getId().intValue()))
            .andExpect(jsonPath("$.assetId").value(DEFAULT_ASSET_ID))
            .andExpect(jsonPath("$.employeeId").value(DEFAULT_EMPLOYEE_ID))
            .andExpect(jsonPath("$.assignDate").value(DEFAULT_ASSIGN_DATE))
            .andExpect(jsonPath("$.returnDate").value(DEFAULT_RETURN_DATE));
    }

    @Test
    @Transactional
    void getAssetAssignsByIdFiltering() throws Exception {
        // Initialize the database
        assetAssignRepository.saveAndFlush(assetAssign);

        Long id = assetAssign.getId();

        defaultAssetAssignShouldBeFound("id.equals=" + id);
        defaultAssetAssignShouldNotBeFound("id.notEquals=" + id);

        defaultAssetAssignShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAssetAssignShouldNotBeFound("id.greaterThan=" + id);

        defaultAssetAssignShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAssetAssignShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAssetAssignsByAssetIdIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAssignRepository.saveAndFlush(assetAssign);

        // Get all the assetAssignList where assetId equals to DEFAULT_ASSET_ID
        defaultAssetAssignShouldBeFound("assetId.equals=" + DEFAULT_ASSET_ID);

        // Get all the assetAssignList where assetId equals to UPDATED_ASSET_ID
        defaultAssetAssignShouldNotBeFound("assetId.equals=" + UPDATED_ASSET_ID);
    }

    @Test
    @Transactional
    void getAllAssetAssignsByAssetIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetAssignRepository.saveAndFlush(assetAssign);

        // Get all the assetAssignList where assetId not equals to DEFAULT_ASSET_ID
        defaultAssetAssignShouldNotBeFound("assetId.notEquals=" + DEFAULT_ASSET_ID);

        // Get all the assetAssignList where assetId not equals to UPDATED_ASSET_ID
        defaultAssetAssignShouldBeFound("assetId.notEquals=" + UPDATED_ASSET_ID);
    }

    @Test
    @Transactional
    void getAllAssetAssignsByAssetIdIsInShouldWork() throws Exception {
        // Initialize the database
        assetAssignRepository.saveAndFlush(assetAssign);

        // Get all the assetAssignList where assetId in DEFAULT_ASSET_ID or UPDATED_ASSET_ID
        defaultAssetAssignShouldBeFound("assetId.in=" + DEFAULT_ASSET_ID + "," + UPDATED_ASSET_ID);

        // Get all the assetAssignList where assetId equals to UPDATED_ASSET_ID
        defaultAssetAssignShouldNotBeFound("assetId.in=" + UPDATED_ASSET_ID);
    }

    @Test
    @Transactional
    void getAllAssetAssignsByAssetIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetAssignRepository.saveAndFlush(assetAssign);

        // Get all the assetAssignList where assetId is not null
        defaultAssetAssignShouldBeFound("assetId.specified=true");

        // Get all the assetAssignList where assetId is null
        defaultAssetAssignShouldNotBeFound("assetId.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetAssignsByAssetIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetAssignRepository.saveAndFlush(assetAssign);

        // Get all the assetAssignList where assetId is greater than or equal to DEFAULT_ASSET_ID
        defaultAssetAssignShouldBeFound("assetId.greaterThanOrEqual=" + DEFAULT_ASSET_ID);

        // Get all the assetAssignList where assetId is greater than or equal to UPDATED_ASSET_ID
        defaultAssetAssignShouldNotBeFound("assetId.greaterThanOrEqual=" + UPDATED_ASSET_ID);
    }

    @Test
    @Transactional
    void getAllAssetAssignsByAssetIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetAssignRepository.saveAndFlush(assetAssign);

        // Get all the assetAssignList where assetId is less than or equal to DEFAULT_ASSET_ID
        defaultAssetAssignShouldBeFound("assetId.lessThanOrEqual=" + DEFAULT_ASSET_ID);

        // Get all the assetAssignList where assetId is less than or equal to SMALLER_ASSET_ID
        defaultAssetAssignShouldNotBeFound("assetId.lessThanOrEqual=" + SMALLER_ASSET_ID);
    }

    @Test
    @Transactional
    void getAllAssetAssignsByAssetIdIsLessThanSomething() throws Exception {
        // Initialize the database
        assetAssignRepository.saveAndFlush(assetAssign);

        // Get all the assetAssignList where assetId is less than DEFAULT_ASSET_ID
        defaultAssetAssignShouldNotBeFound("assetId.lessThan=" + DEFAULT_ASSET_ID);

        // Get all the assetAssignList where assetId is less than UPDATED_ASSET_ID
        defaultAssetAssignShouldBeFound("assetId.lessThan=" + UPDATED_ASSET_ID);
    }

    @Test
    @Transactional
    void getAllAssetAssignsByAssetIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetAssignRepository.saveAndFlush(assetAssign);

        // Get all the assetAssignList where assetId is greater than DEFAULT_ASSET_ID
        defaultAssetAssignShouldNotBeFound("assetId.greaterThan=" + DEFAULT_ASSET_ID);

        // Get all the assetAssignList where assetId is greater than SMALLER_ASSET_ID
        defaultAssetAssignShouldBeFound("assetId.greaterThan=" + SMALLER_ASSET_ID);
    }

    @Test
    @Transactional
    void getAllAssetAssignsByEmployeeIdIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAssignRepository.saveAndFlush(assetAssign);

        // Get all the assetAssignList where employeeId equals to DEFAULT_EMPLOYEE_ID
        defaultAssetAssignShouldBeFound("employeeId.equals=" + DEFAULT_EMPLOYEE_ID);

        // Get all the assetAssignList where employeeId equals to UPDATED_EMPLOYEE_ID
        defaultAssetAssignShouldNotBeFound("employeeId.equals=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllAssetAssignsByEmployeeIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetAssignRepository.saveAndFlush(assetAssign);

        // Get all the assetAssignList where employeeId not equals to DEFAULT_EMPLOYEE_ID
        defaultAssetAssignShouldNotBeFound("employeeId.notEquals=" + DEFAULT_EMPLOYEE_ID);

        // Get all the assetAssignList where employeeId not equals to UPDATED_EMPLOYEE_ID
        defaultAssetAssignShouldBeFound("employeeId.notEquals=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllAssetAssignsByEmployeeIdIsInShouldWork() throws Exception {
        // Initialize the database
        assetAssignRepository.saveAndFlush(assetAssign);

        // Get all the assetAssignList where employeeId in DEFAULT_EMPLOYEE_ID or UPDATED_EMPLOYEE_ID
        defaultAssetAssignShouldBeFound("employeeId.in=" + DEFAULT_EMPLOYEE_ID + "," + UPDATED_EMPLOYEE_ID);

        // Get all the assetAssignList where employeeId equals to UPDATED_EMPLOYEE_ID
        defaultAssetAssignShouldNotBeFound("employeeId.in=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllAssetAssignsByEmployeeIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetAssignRepository.saveAndFlush(assetAssign);

        // Get all the assetAssignList where employeeId is not null
        defaultAssetAssignShouldBeFound("employeeId.specified=true");

        // Get all the assetAssignList where employeeId is null
        defaultAssetAssignShouldNotBeFound("employeeId.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetAssignsByEmployeeIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetAssignRepository.saveAndFlush(assetAssign);

        // Get all the assetAssignList where employeeId is greater than or equal to DEFAULT_EMPLOYEE_ID
        defaultAssetAssignShouldBeFound("employeeId.greaterThanOrEqual=" + DEFAULT_EMPLOYEE_ID);

        // Get all the assetAssignList where employeeId is greater than or equal to UPDATED_EMPLOYEE_ID
        defaultAssetAssignShouldNotBeFound("employeeId.greaterThanOrEqual=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllAssetAssignsByEmployeeIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetAssignRepository.saveAndFlush(assetAssign);

        // Get all the assetAssignList where employeeId is less than or equal to DEFAULT_EMPLOYEE_ID
        defaultAssetAssignShouldBeFound("employeeId.lessThanOrEqual=" + DEFAULT_EMPLOYEE_ID);

        // Get all the assetAssignList where employeeId is less than or equal to SMALLER_EMPLOYEE_ID
        defaultAssetAssignShouldNotBeFound("employeeId.lessThanOrEqual=" + SMALLER_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllAssetAssignsByEmployeeIdIsLessThanSomething() throws Exception {
        // Initialize the database
        assetAssignRepository.saveAndFlush(assetAssign);

        // Get all the assetAssignList where employeeId is less than DEFAULT_EMPLOYEE_ID
        defaultAssetAssignShouldNotBeFound("employeeId.lessThan=" + DEFAULT_EMPLOYEE_ID);

        // Get all the assetAssignList where employeeId is less than UPDATED_EMPLOYEE_ID
        defaultAssetAssignShouldBeFound("employeeId.lessThan=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllAssetAssignsByEmployeeIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetAssignRepository.saveAndFlush(assetAssign);

        // Get all the assetAssignList where employeeId is greater than DEFAULT_EMPLOYEE_ID
        defaultAssetAssignShouldNotBeFound("employeeId.greaterThan=" + DEFAULT_EMPLOYEE_ID);

        // Get all the assetAssignList where employeeId is greater than SMALLER_EMPLOYEE_ID
        defaultAssetAssignShouldBeFound("employeeId.greaterThan=" + SMALLER_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllAssetAssignsByAssignDateIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAssignRepository.saveAndFlush(assetAssign);

        // Get all the assetAssignList where assignDate equals to DEFAULT_ASSIGN_DATE
        defaultAssetAssignShouldBeFound("assignDate.equals=" + DEFAULT_ASSIGN_DATE);

        // Get all the assetAssignList where assignDate equals to UPDATED_ASSIGN_DATE
        defaultAssetAssignShouldNotBeFound("assignDate.equals=" + UPDATED_ASSIGN_DATE);
    }

    @Test
    @Transactional
    void getAllAssetAssignsByAssignDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetAssignRepository.saveAndFlush(assetAssign);

        // Get all the assetAssignList where assignDate not equals to DEFAULT_ASSIGN_DATE
        defaultAssetAssignShouldNotBeFound("assignDate.notEquals=" + DEFAULT_ASSIGN_DATE);

        // Get all the assetAssignList where assignDate not equals to UPDATED_ASSIGN_DATE
        defaultAssetAssignShouldBeFound("assignDate.notEquals=" + UPDATED_ASSIGN_DATE);
    }

    @Test
    @Transactional
    void getAllAssetAssignsByAssignDateIsInShouldWork() throws Exception {
        // Initialize the database
        assetAssignRepository.saveAndFlush(assetAssign);

        // Get all the assetAssignList where assignDate in DEFAULT_ASSIGN_DATE or UPDATED_ASSIGN_DATE
        defaultAssetAssignShouldBeFound("assignDate.in=" + DEFAULT_ASSIGN_DATE + "," + UPDATED_ASSIGN_DATE);

        // Get all the assetAssignList where assignDate equals to UPDATED_ASSIGN_DATE
        defaultAssetAssignShouldNotBeFound("assignDate.in=" + UPDATED_ASSIGN_DATE);
    }

    @Test
    @Transactional
    void getAllAssetAssignsByAssignDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetAssignRepository.saveAndFlush(assetAssign);

        // Get all the assetAssignList where assignDate is not null
        defaultAssetAssignShouldBeFound("assignDate.specified=true");

        // Get all the assetAssignList where assignDate is null
        defaultAssetAssignShouldNotBeFound("assignDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetAssignsByAssignDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetAssignRepository.saveAndFlush(assetAssign);

        // Get all the assetAssignList where assignDate is greater than or equal to DEFAULT_ASSIGN_DATE
        defaultAssetAssignShouldBeFound("assignDate.greaterThanOrEqual=" + DEFAULT_ASSIGN_DATE);

        // Get all the assetAssignList where assignDate is greater than or equal to UPDATED_ASSIGN_DATE
        defaultAssetAssignShouldNotBeFound("assignDate.greaterThanOrEqual=" + UPDATED_ASSIGN_DATE);
    }

    @Test
    @Transactional
    void getAllAssetAssignsByAssignDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetAssignRepository.saveAndFlush(assetAssign);

        // Get all the assetAssignList where assignDate is less than or equal to DEFAULT_ASSIGN_DATE
        defaultAssetAssignShouldBeFound("assignDate.lessThanOrEqual=" + DEFAULT_ASSIGN_DATE);

        // Get all the assetAssignList where assignDate is less than or equal to SMALLER_ASSIGN_DATE
        defaultAssetAssignShouldNotBeFound("assignDate.lessThanOrEqual=" + SMALLER_ASSIGN_DATE);
    }

    @Test
    @Transactional
    void getAllAssetAssignsByAssignDateIsLessThanSomething() throws Exception {
        // Initialize the database
        assetAssignRepository.saveAndFlush(assetAssign);

        // Get all the assetAssignList where assignDate is less than DEFAULT_ASSIGN_DATE
        defaultAssetAssignShouldNotBeFound("assignDate.lessThan=" + DEFAULT_ASSIGN_DATE);

        // Get all the assetAssignList where assignDate is less than UPDATED_ASSIGN_DATE
        defaultAssetAssignShouldBeFound("assignDate.lessThan=" + UPDATED_ASSIGN_DATE);
    }

    @Test
    @Transactional
    void getAllAssetAssignsByAssignDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetAssignRepository.saveAndFlush(assetAssign);

        // Get all the assetAssignList where assignDate is greater than DEFAULT_ASSIGN_DATE
        defaultAssetAssignShouldNotBeFound("assignDate.greaterThan=" + DEFAULT_ASSIGN_DATE);

        // Get all the assetAssignList where assignDate is greater than SMALLER_ASSIGN_DATE
        defaultAssetAssignShouldBeFound("assignDate.greaterThan=" + SMALLER_ASSIGN_DATE);
    }

    @Test
    @Transactional
    void getAllAssetAssignsByReturnDateIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAssignRepository.saveAndFlush(assetAssign);

        // Get all the assetAssignList where returnDate equals to DEFAULT_RETURN_DATE
        defaultAssetAssignShouldBeFound("returnDate.equals=" + DEFAULT_RETURN_DATE);

        // Get all the assetAssignList where returnDate equals to UPDATED_RETURN_DATE
        defaultAssetAssignShouldNotBeFound("returnDate.equals=" + UPDATED_RETURN_DATE);
    }

    @Test
    @Transactional
    void getAllAssetAssignsByReturnDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetAssignRepository.saveAndFlush(assetAssign);

        // Get all the assetAssignList where returnDate not equals to DEFAULT_RETURN_DATE
        defaultAssetAssignShouldNotBeFound("returnDate.notEquals=" + DEFAULT_RETURN_DATE);

        // Get all the assetAssignList where returnDate not equals to UPDATED_RETURN_DATE
        defaultAssetAssignShouldBeFound("returnDate.notEquals=" + UPDATED_RETURN_DATE);
    }

    @Test
    @Transactional
    void getAllAssetAssignsByReturnDateIsInShouldWork() throws Exception {
        // Initialize the database
        assetAssignRepository.saveAndFlush(assetAssign);

        // Get all the assetAssignList where returnDate in DEFAULT_RETURN_DATE or UPDATED_RETURN_DATE
        defaultAssetAssignShouldBeFound("returnDate.in=" + DEFAULT_RETURN_DATE + "," + UPDATED_RETURN_DATE);

        // Get all the assetAssignList where returnDate equals to UPDATED_RETURN_DATE
        defaultAssetAssignShouldNotBeFound("returnDate.in=" + UPDATED_RETURN_DATE);
    }

    @Test
    @Transactional
    void getAllAssetAssignsByReturnDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetAssignRepository.saveAndFlush(assetAssign);

        // Get all the assetAssignList where returnDate is not null
        defaultAssetAssignShouldBeFound("returnDate.specified=true");

        // Get all the assetAssignList where returnDate is null
        defaultAssetAssignShouldNotBeFound("returnDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetAssignsByReturnDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetAssignRepository.saveAndFlush(assetAssign);

        // Get all the assetAssignList where returnDate is greater than or equal to DEFAULT_RETURN_DATE
        defaultAssetAssignShouldBeFound("returnDate.greaterThanOrEqual=" + DEFAULT_RETURN_DATE);

        // Get all the assetAssignList where returnDate is greater than or equal to UPDATED_RETURN_DATE
        defaultAssetAssignShouldNotBeFound("returnDate.greaterThanOrEqual=" + UPDATED_RETURN_DATE);
    }

    @Test
    @Transactional
    void getAllAssetAssignsByReturnDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetAssignRepository.saveAndFlush(assetAssign);

        // Get all the assetAssignList where returnDate is less than or equal to DEFAULT_RETURN_DATE
        defaultAssetAssignShouldBeFound("returnDate.lessThanOrEqual=" + DEFAULT_RETURN_DATE);

        // Get all the assetAssignList where returnDate is less than or equal to SMALLER_RETURN_DATE
        defaultAssetAssignShouldNotBeFound("returnDate.lessThanOrEqual=" + SMALLER_RETURN_DATE);
    }

    @Test
    @Transactional
    void getAllAssetAssignsByReturnDateIsLessThanSomething() throws Exception {
        // Initialize the database
        assetAssignRepository.saveAndFlush(assetAssign);

        // Get all the assetAssignList where returnDate is less than DEFAULT_RETURN_DATE
        defaultAssetAssignShouldNotBeFound("returnDate.lessThan=" + DEFAULT_RETURN_DATE);

        // Get all the assetAssignList where returnDate is less than UPDATED_RETURN_DATE
        defaultAssetAssignShouldBeFound("returnDate.lessThan=" + UPDATED_RETURN_DATE);
    }

    @Test
    @Transactional
    void getAllAssetAssignsByReturnDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetAssignRepository.saveAndFlush(assetAssign);

        // Get all the assetAssignList where returnDate is greater than DEFAULT_RETURN_DATE
        defaultAssetAssignShouldNotBeFound("returnDate.greaterThan=" + DEFAULT_RETURN_DATE);

        // Get all the assetAssignList where returnDate is greater than SMALLER_RETURN_DATE
        defaultAssetAssignShouldBeFound("returnDate.greaterThan=" + SMALLER_RETURN_DATE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAssetAssignShouldBeFound(String filter) throws Exception {
        restAssetAssignMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetAssign.getId().intValue())))
            .andExpect(jsonPath("$.[*].assetId").value(hasItem(DEFAULT_ASSET_ID)))
            .andExpect(jsonPath("$.[*].employeeId").value(hasItem(DEFAULT_EMPLOYEE_ID)))
            .andExpect(jsonPath("$.[*].assignDate").value(hasItem(DEFAULT_ASSIGN_DATE)))
            .andExpect(jsonPath("$.[*].returnDate").value(hasItem(DEFAULT_RETURN_DATE)));

        // Check, that the count call also returns 1
        restAssetAssignMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAssetAssignShouldNotBeFound(String filter) throws Exception {
        restAssetAssignMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAssetAssignMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAssetAssign() throws Exception {
        // Get the assetAssign
        restAssetAssignMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAssetAssign() throws Exception {
        // Initialize the database
        assetAssignRepository.saveAndFlush(assetAssign);

        int databaseSizeBeforeUpdate = assetAssignRepository.findAll().size();

        // Update the assetAssign
        AssetAssign updatedAssetAssign = assetAssignRepository.findById(assetAssign.getId()).get();
        // Disconnect from session so that the updates on updatedAssetAssign are not directly saved in db
        em.detach(updatedAssetAssign);
        updatedAssetAssign
            .assetId(UPDATED_ASSET_ID)
            .employeeId(UPDATED_EMPLOYEE_ID)
            .assignDate(UPDATED_ASSIGN_DATE)
            .returnDate(UPDATED_RETURN_DATE);
        AssetAssignDTO assetAssignDTO = assetAssignMapper.toDto(updatedAssetAssign);

        restAssetAssignMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assetAssignDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetAssignDTO))
            )
            .andExpect(status().isOk());

        // Validate the AssetAssign in the database
        List<AssetAssign> assetAssignList = assetAssignRepository.findAll();
        assertThat(assetAssignList).hasSize(databaseSizeBeforeUpdate);
        AssetAssign testAssetAssign = assetAssignList.get(assetAssignList.size() - 1);
        assertThat(testAssetAssign.getAssetId()).isEqualTo(UPDATED_ASSET_ID);
        assertThat(testAssetAssign.getEmployeeId()).isEqualTo(UPDATED_EMPLOYEE_ID);
        assertThat(testAssetAssign.getAssignDate()).isEqualTo(UPDATED_ASSIGN_DATE);
        assertThat(testAssetAssign.getReturnDate()).isEqualTo(UPDATED_RETURN_DATE);
    }

    @Test
    @Transactional
    void putNonExistingAssetAssign() throws Exception {
        int databaseSizeBeforeUpdate = assetAssignRepository.findAll().size();
        assetAssign.setId(count.incrementAndGet());

        // Create the AssetAssign
        AssetAssignDTO assetAssignDTO = assetAssignMapper.toDto(assetAssign);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetAssignMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assetAssignDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetAssignDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetAssign in the database
        List<AssetAssign> assetAssignList = assetAssignRepository.findAll();
        assertThat(assetAssignList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAssetAssign() throws Exception {
        int databaseSizeBeforeUpdate = assetAssignRepository.findAll().size();
        assetAssign.setId(count.incrementAndGet());

        // Create the AssetAssign
        AssetAssignDTO assetAssignDTO = assetAssignMapper.toDto(assetAssign);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetAssignMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetAssignDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetAssign in the database
        List<AssetAssign> assetAssignList = assetAssignRepository.findAll();
        assertThat(assetAssignList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAssetAssign() throws Exception {
        int databaseSizeBeforeUpdate = assetAssignRepository.findAll().size();
        assetAssign.setId(count.incrementAndGet());

        // Create the AssetAssign
        AssetAssignDTO assetAssignDTO = assetAssignMapper.toDto(assetAssign);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetAssignMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetAssignDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssetAssign in the database
        List<AssetAssign> assetAssignList = assetAssignRepository.findAll();
        assertThat(assetAssignList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAssetAssignWithPatch() throws Exception {
        // Initialize the database
        assetAssignRepository.saveAndFlush(assetAssign);

        int databaseSizeBeforeUpdate = assetAssignRepository.findAll().size();

        // Update the assetAssign using partial update
        AssetAssign partialUpdatedAssetAssign = new AssetAssign();
        partialUpdatedAssetAssign.setId(assetAssign.getId());

        partialUpdatedAssetAssign.assetId(UPDATED_ASSET_ID).assignDate(UPDATED_ASSIGN_DATE);

        restAssetAssignMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssetAssign.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssetAssign))
            )
            .andExpect(status().isOk());

        // Validate the AssetAssign in the database
        List<AssetAssign> assetAssignList = assetAssignRepository.findAll();
        assertThat(assetAssignList).hasSize(databaseSizeBeforeUpdate);
        AssetAssign testAssetAssign = assetAssignList.get(assetAssignList.size() - 1);
        assertThat(testAssetAssign.getAssetId()).isEqualTo(UPDATED_ASSET_ID);
        assertThat(testAssetAssign.getEmployeeId()).isEqualTo(DEFAULT_EMPLOYEE_ID);
        assertThat(testAssetAssign.getAssignDate()).isEqualTo(UPDATED_ASSIGN_DATE);
        assertThat(testAssetAssign.getReturnDate()).isEqualTo(DEFAULT_RETURN_DATE);
    }

    @Test
    @Transactional
    void fullUpdateAssetAssignWithPatch() throws Exception {
        // Initialize the database
        assetAssignRepository.saveAndFlush(assetAssign);

        int databaseSizeBeforeUpdate = assetAssignRepository.findAll().size();

        // Update the assetAssign using partial update
        AssetAssign partialUpdatedAssetAssign = new AssetAssign();
        partialUpdatedAssetAssign.setId(assetAssign.getId());

        partialUpdatedAssetAssign
            .assetId(UPDATED_ASSET_ID)
            .employeeId(UPDATED_EMPLOYEE_ID)
            .assignDate(UPDATED_ASSIGN_DATE)
            .returnDate(UPDATED_RETURN_DATE);

        restAssetAssignMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssetAssign.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssetAssign))
            )
            .andExpect(status().isOk());

        // Validate the AssetAssign in the database
        List<AssetAssign> assetAssignList = assetAssignRepository.findAll();
        assertThat(assetAssignList).hasSize(databaseSizeBeforeUpdate);
        AssetAssign testAssetAssign = assetAssignList.get(assetAssignList.size() - 1);
        assertThat(testAssetAssign.getAssetId()).isEqualTo(UPDATED_ASSET_ID);
        assertThat(testAssetAssign.getEmployeeId()).isEqualTo(UPDATED_EMPLOYEE_ID);
        assertThat(testAssetAssign.getAssignDate()).isEqualTo(UPDATED_ASSIGN_DATE);
        assertThat(testAssetAssign.getReturnDate()).isEqualTo(UPDATED_RETURN_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingAssetAssign() throws Exception {
        int databaseSizeBeforeUpdate = assetAssignRepository.findAll().size();
        assetAssign.setId(count.incrementAndGet());

        // Create the AssetAssign
        AssetAssignDTO assetAssignDTO = assetAssignMapper.toDto(assetAssign);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetAssignMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, assetAssignDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assetAssignDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetAssign in the database
        List<AssetAssign> assetAssignList = assetAssignRepository.findAll();
        assertThat(assetAssignList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAssetAssign() throws Exception {
        int databaseSizeBeforeUpdate = assetAssignRepository.findAll().size();
        assetAssign.setId(count.incrementAndGet());

        // Create the AssetAssign
        AssetAssignDTO assetAssignDTO = assetAssignMapper.toDto(assetAssign);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetAssignMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assetAssignDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetAssign in the database
        List<AssetAssign> assetAssignList = assetAssignRepository.findAll();
        assertThat(assetAssignList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAssetAssign() throws Exception {
        int databaseSizeBeforeUpdate = assetAssignRepository.findAll().size();
        assetAssign.setId(count.incrementAndGet());

        // Create the AssetAssign
        AssetAssignDTO assetAssignDTO = assetAssignMapper.toDto(assetAssign);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetAssignMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(assetAssignDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssetAssign in the database
        List<AssetAssign> assetAssignList = assetAssignRepository.findAll();
        assertThat(assetAssignList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAssetAssign() throws Exception {
        // Initialize the database
        assetAssignRepository.saveAndFlush(assetAssign);

        int databaseSizeBeforeDelete = assetAssignRepository.findAll().size();

        // Delete the assetAssign
        restAssetAssignMockMvc
            .perform(delete(ENTITY_API_URL_ID, assetAssign.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AssetAssign> assetAssignList = assetAssignRepository.findAll();
        assertThat(assetAssignList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
