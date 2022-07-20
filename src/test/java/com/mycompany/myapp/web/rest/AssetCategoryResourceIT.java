package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.AssetCategory;
import com.mycompany.myapp.repository.AssetCategoryRepository;
import com.mycompany.myapp.service.criteria.AssetCategoryCriteria;
import com.mycompany.myapp.service.dto.AssetCategoryDTO;
import com.mycompany.myapp.service.mapper.AssetCategoryMapper;
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
 * Integration tests for the {@link AssetCategoryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AssetCategoryResourceIT {

    private static final Integer DEFAULT_ASSET_CATEGORY_ID = 1;
    private static final Integer UPDATED_ASSET_CATEGORY_ID = 2;
    private static final Integer SMALLER_ASSET_CATEGORY_ID = 1 - 1;

    private static final String DEFAULT_ASSET_CATEGORY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_CATEGORY_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/asset-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AssetCategoryRepository assetCategoryRepository;

    @Autowired
    private AssetCategoryMapper assetCategoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAssetCategoryMockMvc;

    private AssetCategory assetCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssetCategory createEntity(EntityManager em) {
        AssetCategory assetCategory = new AssetCategory()
            .assetCategoryId(DEFAULT_ASSET_CATEGORY_ID)
            .assetCategoryName(DEFAULT_ASSET_CATEGORY_NAME);
        return assetCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssetCategory createUpdatedEntity(EntityManager em) {
        AssetCategory assetCategory = new AssetCategory()
            .assetCategoryId(UPDATED_ASSET_CATEGORY_ID)
            .assetCategoryName(UPDATED_ASSET_CATEGORY_NAME);
        return assetCategory;
    }

    @BeforeEach
    public void initTest() {
        assetCategory = createEntity(em);
    }

    @Test
    @Transactional
    void createAssetCategory() throws Exception {
        int databaseSizeBeforeCreate = assetCategoryRepository.findAll().size();
        // Create the AssetCategory
        AssetCategoryDTO assetCategoryDTO = assetCategoryMapper.toDto(assetCategory);
        restAssetCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetCategoryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AssetCategory in the database
        List<AssetCategory> assetCategoryList = assetCategoryRepository.findAll();
        assertThat(assetCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        AssetCategory testAssetCategory = assetCategoryList.get(assetCategoryList.size() - 1);
        assertThat(testAssetCategory.getAssetCategoryId()).isEqualTo(DEFAULT_ASSET_CATEGORY_ID);
        assertThat(testAssetCategory.getAssetCategoryName()).isEqualTo(DEFAULT_ASSET_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void createAssetCategoryWithExistingId() throws Exception {
        // Create the AssetCategory with an existing ID
        assetCategory.setId(1L);
        AssetCategoryDTO assetCategoryDTO = assetCategoryMapper.toDto(assetCategory);

        int databaseSizeBeforeCreate = assetCategoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssetCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetCategory in the database
        List<AssetCategory> assetCategoryList = assetCategoryRepository.findAll();
        assertThat(assetCategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAssetCategories() throws Exception {
        // Initialize the database
        assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList
        restAssetCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].assetCategoryId").value(hasItem(DEFAULT_ASSET_CATEGORY_ID)))
            .andExpect(jsonPath("$.[*].assetCategoryName").value(hasItem(DEFAULT_ASSET_CATEGORY_NAME)));
    }

    @Test
    @Transactional
    void getAssetCategory() throws Exception {
        // Initialize the database
        assetCategoryRepository.saveAndFlush(assetCategory);

        // Get the assetCategory
        restAssetCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, assetCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(assetCategory.getId().intValue()))
            .andExpect(jsonPath("$.assetCategoryId").value(DEFAULT_ASSET_CATEGORY_ID))
            .andExpect(jsonPath("$.assetCategoryName").value(DEFAULT_ASSET_CATEGORY_NAME));
    }

    @Test
    @Transactional
    void getAssetCategoriesByIdFiltering() throws Exception {
        // Initialize the database
        assetCategoryRepository.saveAndFlush(assetCategory);

        Long id = assetCategory.getId();

        defaultAssetCategoryShouldBeFound("id.equals=" + id);
        defaultAssetCategoryShouldNotBeFound("id.notEquals=" + id);

        defaultAssetCategoryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAssetCategoryShouldNotBeFound("id.greaterThan=" + id);

        defaultAssetCategoryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAssetCategoryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByAssetCategoryIdIsEqualToSomething() throws Exception {
        // Initialize the database
        assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where assetCategoryId equals to DEFAULT_ASSET_CATEGORY_ID
        defaultAssetCategoryShouldBeFound("assetCategoryId.equals=" + DEFAULT_ASSET_CATEGORY_ID);

        // Get all the assetCategoryList where assetCategoryId equals to UPDATED_ASSET_CATEGORY_ID
        defaultAssetCategoryShouldNotBeFound("assetCategoryId.equals=" + UPDATED_ASSET_CATEGORY_ID);
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByAssetCategoryIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where assetCategoryId not equals to DEFAULT_ASSET_CATEGORY_ID
        defaultAssetCategoryShouldNotBeFound("assetCategoryId.notEquals=" + DEFAULT_ASSET_CATEGORY_ID);

        // Get all the assetCategoryList where assetCategoryId not equals to UPDATED_ASSET_CATEGORY_ID
        defaultAssetCategoryShouldBeFound("assetCategoryId.notEquals=" + UPDATED_ASSET_CATEGORY_ID);
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByAssetCategoryIdIsInShouldWork() throws Exception {
        // Initialize the database
        assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where assetCategoryId in DEFAULT_ASSET_CATEGORY_ID or UPDATED_ASSET_CATEGORY_ID
        defaultAssetCategoryShouldBeFound("assetCategoryId.in=" + DEFAULT_ASSET_CATEGORY_ID + "," + UPDATED_ASSET_CATEGORY_ID);

        // Get all the assetCategoryList where assetCategoryId equals to UPDATED_ASSET_CATEGORY_ID
        defaultAssetCategoryShouldNotBeFound("assetCategoryId.in=" + UPDATED_ASSET_CATEGORY_ID);
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByAssetCategoryIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where assetCategoryId is not null
        defaultAssetCategoryShouldBeFound("assetCategoryId.specified=true");

        // Get all the assetCategoryList where assetCategoryId is null
        defaultAssetCategoryShouldNotBeFound("assetCategoryId.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByAssetCategoryIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where assetCategoryId is greater than or equal to DEFAULT_ASSET_CATEGORY_ID
        defaultAssetCategoryShouldBeFound("assetCategoryId.greaterThanOrEqual=" + DEFAULT_ASSET_CATEGORY_ID);

        // Get all the assetCategoryList where assetCategoryId is greater than or equal to UPDATED_ASSET_CATEGORY_ID
        defaultAssetCategoryShouldNotBeFound("assetCategoryId.greaterThanOrEqual=" + UPDATED_ASSET_CATEGORY_ID);
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByAssetCategoryIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where assetCategoryId is less than or equal to DEFAULT_ASSET_CATEGORY_ID
        defaultAssetCategoryShouldBeFound("assetCategoryId.lessThanOrEqual=" + DEFAULT_ASSET_CATEGORY_ID);

        // Get all the assetCategoryList where assetCategoryId is less than or equal to SMALLER_ASSET_CATEGORY_ID
        defaultAssetCategoryShouldNotBeFound("assetCategoryId.lessThanOrEqual=" + SMALLER_ASSET_CATEGORY_ID);
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByAssetCategoryIdIsLessThanSomething() throws Exception {
        // Initialize the database
        assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where assetCategoryId is less than DEFAULT_ASSET_CATEGORY_ID
        defaultAssetCategoryShouldNotBeFound("assetCategoryId.lessThan=" + DEFAULT_ASSET_CATEGORY_ID);

        // Get all the assetCategoryList where assetCategoryId is less than UPDATED_ASSET_CATEGORY_ID
        defaultAssetCategoryShouldBeFound("assetCategoryId.lessThan=" + UPDATED_ASSET_CATEGORY_ID);
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByAssetCategoryIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where assetCategoryId is greater than DEFAULT_ASSET_CATEGORY_ID
        defaultAssetCategoryShouldNotBeFound("assetCategoryId.greaterThan=" + DEFAULT_ASSET_CATEGORY_ID);

        // Get all the assetCategoryList where assetCategoryId is greater than SMALLER_ASSET_CATEGORY_ID
        defaultAssetCategoryShouldBeFound("assetCategoryId.greaterThan=" + SMALLER_ASSET_CATEGORY_ID);
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByAssetCategoryNameIsEqualToSomething() throws Exception {
        // Initialize the database
        assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where assetCategoryName equals to DEFAULT_ASSET_CATEGORY_NAME
        defaultAssetCategoryShouldBeFound("assetCategoryName.equals=" + DEFAULT_ASSET_CATEGORY_NAME);

        // Get all the assetCategoryList where assetCategoryName equals to UPDATED_ASSET_CATEGORY_NAME
        defaultAssetCategoryShouldNotBeFound("assetCategoryName.equals=" + UPDATED_ASSET_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByAssetCategoryNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where assetCategoryName not equals to DEFAULT_ASSET_CATEGORY_NAME
        defaultAssetCategoryShouldNotBeFound("assetCategoryName.notEquals=" + DEFAULT_ASSET_CATEGORY_NAME);

        // Get all the assetCategoryList where assetCategoryName not equals to UPDATED_ASSET_CATEGORY_NAME
        defaultAssetCategoryShouldBeFound("assetCategoryName.notEquals=" + UPDATED_ASSET_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByAssetCategoryNameIsInShouldWork() throws Exception {
        // Initialize the database
        assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where assetCategoryName in DEFAULT_ASSET_CATEGORY_NAME or UPDATED_ASSET_CATEGORY_NAME
        defaultAssetCategoryShouldBeFound("assetCategoryName.in=" + DEFAULT_ASSET_CATEGORY_NAME + "," + UPDATED_ASSET_CATEGORY_NAME);

        // Get all the assetCategoryList where assetCategoryName equals to UPDATED_ASSET_CATEGORY_NAME
        defaultAssetCategoryShouldNotBeFound("assetCategoryName.in=" + UPDATED_ASSET_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByAssetCategoryNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where assetCategoryName is not null
        defaultAssetCategoryShouldBeFound("assetCategoryName.specified=true");

        // Get all the assetCategoryList where assetCategoryName is null
        defaultAssetCategoryShouldNotBeFound("assetCategoryName.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByAssetCategoryNameContainsSomething() throws Exception {
        // Initialize the database
        assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where assetCategoryName contains DEFAULT_ASSET_CATEGORY_NAME
        defaultAssetCategoryShouldBeFound("assetCategoryName.contains=" + DEFAULT_ASSET_CATEGORY_NAME);

        // Get all the assetCategoryList where assetCategoryName contains UPDATED_ASSET_CATEGORY_NAME
        defaultAssetCategoryShouldNotBeFound("assetCategoryName.contains=" + UPDATED_ASSET_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByAssetCategoryNameNotContainsSomething() throws Exception {
        // Initialize the database
        assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where assetCategoryName does not contain DEFAULT_ASSET_CATEGORY_NAME
        defaultAssetCategoryShouldNotBeFound("assetCategoryName.doesNotContain=" + DEFAULT_ASSET_CATEGORY_NAME);

        // Get all the assetCategoryList where assetCategoryName does not contain UPDATED_ASSET_CATEGORY_NAME
        defaultAssetCategoryShouldBeFound("assetCategoryName.doesNotContain=" + UPDATED_ASSET_CATEGORY_NAME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAssetCategoryShouldBeFound(String filter) throws Exception {
        restAssetCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].assetCategoryId").value(hasItem(DEFAULT_ASSET_CATEGORY_ID)))
            .andExpect(jsonPath("$.[*].assetCategoryName").value(hasItem(DEFAULT_ASSET_CATEGORY_NAME)));

        // Check, that the count call also returns 1
        restAssetCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAssetCategoryShouldNotBeFound(String filter) throws Exception {
        restAssetCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAssetCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAssetCategory() throws Exception {
        // Get the assetCategory
        restAssetCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAssetCategory() throws Exception {
        // Initialize the database
        assetCategoryRepository.saveAndFlush(assetCategory);

        int databaseSizeBeforeUpdate = assetCategoryRepository.findAll().size();

        // Update the assetCategory
        AssetCategory updatedAssetCategory = assetCategoryRepository.findById(assetCategory.getId()).get();
        // Disconnect from session so that the updates on updatedAssetCategory are not directly saved in db
        em.detach(updatedAssetCategory);
        updatedAssetCategory.assetCategoryId(UPDATED_ASSET_CATEGORY_ID).assetCategoryName(UPDATED_ASSET_CATEGORY_NAME);
        AssetCategoryDTO assetCategoryDTO = assetCategoryMapper.toDto(updatedAssetCategory);

        restAssetCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assetCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetCategoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the AssetCategory in the database
        List<AssetCategory> assetCategoryList = assetCategoryRepository.findAll();
        assertThat(assetCategoryList).hasSize(databaseSizeBeforeUpdate);
        AssetCategory testAssetCategory = assetCategoryList.get(assetCategoryList.size() - 1);
        assertThat(testAssetCategory.getAssetCategoryId()).isEqualTo(UPDATED_ASSET_CATEGORY_ID);
        assertThat(testAssetCategory.getAssetCategoryName()).isEqualTo(UPDATED_ASSET_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void putNonExistingAssetCategory() throws Exception {
        int databaseSizeBeforeUpdate = assetCategoryRepository.findAll().size();
        assetCategory.setId(count.incrementAndGet());

        // Create the AssetCategory
        AssetCategoryDTO assetCategoryDTO = assetCategoryMapper.toDto(assetCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assetCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetCategory in the database
        List<AssetCategory> assetCategoryList = assetCategoryRepository.findAll();
        assertThat(assetCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAssetCategory() throws Exception {
        int databaseSizeBeforeUpdate = assetCategoryRepository.findAll().size();
        assetCategory.setId(count.incrementAndGet());

        // Create the AssetCategory
        AssetCategoryDTO assetCategoryDTO = assetCategoryMapper.toDto(assetCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetCategory in the database
        List<AssetCategory> assetCategoryList = assetCategoryRepository.findAll();
        assertThat(assetCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAssetCategory() throws Exception {
        int databaseSizeBeforeUpdate = assetCategoryRepository.findAll().size();
        assetCategory.setId(count.incrementAndGet());

        // Create the AssetCategory
        AssetCategoryDTO assetCategoryDTO = assetCategoryMapper.toDto(assetCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetCategoryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetCategoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssetCategory in the database
        List<AssetCategory> assetCategoryList = assetCategoryRepository.findAll();
        assertThat(assetCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAssetCategoryWithPatch() throws Exception {
        // Initialize the database
        assetCategoryRepository.saveAndFlush(assetCategory);

        int databaseSizeBeforeUpdate = assetCategoryRepository.findAll().size();

        // Update the assetCategory using partial update
        AssetCategory partialUpdatedAssetCategory = new AssetCategory();
        partialUpdatedAssetCategory.setId(assetCategory.getId());

        partialUpdatedAssetCategory.assetCategoryId(UPDATED_ASSET_CATEGORY_ID).assetCategoryName(UPDATED_ASSET_CATEGORY_NAME);

        restAssetCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssetCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssetCategory))
            )
            .andExpect(status().isOk());

        // Validate the AssetCategory in the database
        List<AssetCategory> assetCategoryList = assetCategoryRepository.findAll();
        assertThat(assetCategoryList).hasSize(databaseSizeBeforeUpdate);
        AssetCategory testAssetCategory = assetCategoryList.get(assetCategoryList.size() - 1);
        assertThat(testAssetCategory.getAssetCategoryId()).isEqualTo(UPDATED_ASSET_CATEGORY_ID);
        assertThat(testAssetCategory.getAssetCategoryName()).isEqualTo(UPDATED_ASSET_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void fullUpdateAssetCategoryWithPatch() throws Exception {
        // Initialize the database
        assetCategoryRepository.saveAndFlush(assetCategory);

        int databaseSizeBeforeUpdate = assetCategoryRepository.findAll().size();

        // Update the assetCategory using partial update
        AssetCategory partialUpdatedAssetCategory = new AssetCategory();
        partialUpdatedAssetCategory.setId(assetCategory.getId());

        partialUpdatedAssetCategory.assetCategoryId(UPDATED_ASSET_CATEGORY_ID).assetCategoryName(UPDATED_ASSET_CATEGORY_NAME);

        restAssetCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssetCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssetCategory))
            )
            .andExpect(status().isOk());

        // Validate the AssetCategory in the database
        List<AssetCategory> assetCategoryList = assetCategoryRepository.findAll();
        assertThat(assetCategoryList).hasSize(databaseSizeBeforeUpdate);
        AssetCategory testAssetCategory = assetCategoryList.get(assetCategoryList.size() - 1);
        assertThat(testAssetCategory.getAssetCategoryId()).isEqualTo(UPDATED_ASSET_CATEGORY_ID);
        assertThat(testAssetCategory.getAssetCategoryName()).isEqualTo(UPDATED_ASSET_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingAssetCategory() throws Exception {
        int databaseSizeBeforeUpdate = assetCategoryRepository.findAll().size();
        assetCategory.setId(count.incrementAndGet());

        // Create the AssetCategory
        AssetCategoryDTO assetCategoryDTO = assetCategoryMapper.toDto(assetCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, assetCategoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assetCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetCategory in the database
        List<AssetCategory> assetCategoryList = assetCategoryRepository.findAll();
        assertThat(assetCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAssetCategory() throws Exception {
        int databaseSizeBeforeUpdate = assetCategoryRepository.findAll().size();
        assetCategory.setId(count.incrementAndGet());

        // Create the AssetCategory
        AssetCategoryDTO assetCategoryDTO = assetCategoryMapper.toDto(assetCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assetCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetCategory in the database
        List<AssetCategory> assetCategoryList = assetCategoryRepository.findAll();
        assertThat(assetCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAssetCategory() throws Exception {
        int databaseSizeBeforeUpdate = assetCategoryRepository.findAll().size();
        assetCategory.setId(count.incrementAndGet());

        // Create the AssetCategory
        AssetCategoryDTO assetCategoryDTO = assetCategoryMapper.toDto(assetCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assetCategoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssetCategory in the database
        List<AssetCategory> assetCategoryList = assetCategoryRepository.findAll();
        assertThat(assetCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAssetCategory() throws Exception {
        // Initialize the database
        assetCategoryRepository.saveAndFlush(assetCategory);

        int databaseSizeBeforeDelete = assetCategoryRepository.findAll().size();

        // Delete the assetCategory
        restAssetCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, assetCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AssetCategory> assetCategoryList = assetCategoryRepository.findAll();
        assertThat(assetCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
