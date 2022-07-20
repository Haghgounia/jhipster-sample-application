package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.AssetStatus;
import com.mycompany.myapp.repository.AssetStatusRepository;
import com.mycompany.myapp.service.criteria.AssetStatusCriteria;
import com.mycompany.myapp.service.dto.AssetStatusDTO;
import com.mycompany.myapp.service.mapper.AssetStatusMapper;
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
 * Integration tests for the {@link AssetStatusResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AssetStatusResourceIT {

    private static final Integer DEFAULT_ASSET_STATUS_ID = 1;
    private static final Integer UPDATED_ASSET_STATUS_ID = 2;
    private static final Integer SMALLER_ASSET_STATUS_ID = 1 - 1;

    private static final String DEFAULT_ASSET_STATUS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_STATUS_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/asset-statuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AssetStatusRepository assetStatusRepository;

    @Autowired
    private AssetStatusMapper assetStatusMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAssetStatusMockMvc;

    private AssetStatus assetStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssetStatus createEntity(EntityManager em) {
        AssetStatus assetStatus = new AssetStatus().assetStatusId(DEFAULT_ASSET_STATUS_ID).assetStatusName(DEFAULT_ASSET_STATUS_NAME);
        return assetStatus;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssetStatus createUpdatedEntity(EntityManager em) {
        AssetStatus assetStatus = new AssetStatus().assetStatusId(UPDATED_ASSET_STATUS_ID).assetStatusName(UPDATED_ASSET_STATUS_NAME);
        return assetStatus;
    }

    @BeforeEach
    public void initTest() {
        assetStatus = createEntity(em);
    }

    @Test
    @Transactional
    void createAssetStatus() throws Exception {
        int databaseSizeBeforeCreate = assetStatusRepository.findAll().size();
        // Create the AssetStatus
        AssetStatusDTO assetStatusDTO = assetStatusMapper.toDto(assetStatus);
        restAssetStatusMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetStatusDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AssetStatus in the database
        List<AssetStatus> assetStatusList = assetStatusRepository.findAll();
        assertThat(assetStatusList).hasSize(databaseSizeBeforeCreate + 1);
        AssetStatus testAssetStatus = assetStatusList.get(assetStatusList.size() - 1);
        assertThat(testAssetStatus.getAssetStatusId()).isEqualTo(DEFAULT_ASSET_STATUS_ID);
        assertThat(testAssetStatus.getAssetStatusName()).isEqualTo(DEFAULT_ASSET_STATUS_NAME);
    }

    @Test
    @Transactional
    void createAssetStatusWithExistingId() throws Exception {
        // Create the AssetStatus with an existing ID
        assetStatus.setId(1L);
        AssetStatusDTO assetStatusDTO = assetStatusMapper.toDto(assetStatus);

        int databaseSizeBeforeCreate = assetStatusRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssetStatusMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetStatus in the database
        List<AssetStatus> assetStatusList = assetStatusRepository.findAll();
        assertThat(assetStatusList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAssetStatuses() throws Exception {
        // Initialize the database
        assetStatusRepository.saveAndFlush(assetStatus);

        // Get all the assetStatusList
        restAssetStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].assetStatusId").value(hasItem(DEFAULT_ASSET_STATUS_ID)))
            .andExpect(jsonPath("$.[*].assetStatusName").value(hasItem(DEFAULT_ASSET_STATUS_NAME)));
    }

    @Test
    @Transactional
    void getAssetStatus() throws Exception {
        // Initialize the database
        assetStatusRepository.saveAndFlush(assetStatus);

        // Get the assetStatus
        restAssetStatusMockMvc
            .perform(get(ENTITY_API_URL_ID, assetStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(assetStatus.getId().intValue()))
            .andExpect(jsonPath("$.assetStatusId").value(DEFAULT_ASSET_STATUS_ID))
            .andExpect(jsonPath("$.assetStatusName").value(DEFAULT_ASSET_STATUS_NAME));
    }

    @Test
    @Transactional
    void getAssetStatusesByIdFiltering() throws Exception {
        // Initialize the database
        assetStatusRepository.saveAndFlush(assetStatus);

        Long id = assetStatus.getId();

        defaultAssetStatusShouldBeFound("id.equals=" + id);
        defaultAssetStatusShouldNotBeFound("id.notEquals=" + id);

        defaultAssetStatusShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAssetStatusShouldNotBeFound("id.greaterThan=" + id);

        defaultAssetStatusShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAssetStatusShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAssetStatusesByAssetStatusIdIsEqualToSomething() throws Exception {
        // Initialize the database
        assetStatusRepository.saveAndFlush(assetStatus);

        // Get all the assetStatusList where assetStatusId equals to DEFAULT_ASSET_STATUS_ID
        defaultAssetStatusShouldBeFound("assetStatusId.equals=" + DEFAULT_ASSET_STATUS_ID);

        // Get all the assetStatusList where assetStatusId equals to UPDATED_ASSET_STATUS_ID
        defaultAssetStatusShouldNotBeFound("assetStatusId.equals=" + UPDATED_ASSET_STATUS_ID);
    }

    @Test
    @Transactional
    void getAllAssetStatusesByAssetStatusIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetStatusRepository.saveAndFlush(assetStatus);

        // Get all the assetStatusList where assetStatusId not equals to DEFAULT_ASSET_STATUS_ID
        defaultAssetStatusShouldNotBeFound("assetStatusId.notEquals=" + DEFAULT_ASSET_STATUS_ID);

        // Get all the assetStatusList where assetStatusId not equals to UPDATED_ASSET_STATUS_ID
        defaultAssetStatusShouldBeFound("assetStatusId.notEquals=" + UPDATED_ASSET_STATUS_ID);
    }

    @Test
    @Transactional
    void getAllAssetStatusesByAssetStatusIdIsInShouldWork() throws Exception {
        // Initialize the database
        assetStatusRepository.saveAndFlush(assetStatus);

        // Get all the assetStatusList where assetStatusId in DEFAULT_ASSET_STATUS_ID or UPDATED_ASSET_STATUS_ID
        defaultAssetStatusShouldBeFound("assetStatusId.in=" + DEFAULT_ASSET_STATUS_ID + "," + UPDATED_ASSET_STATUS_ID);

        // Get all the assetStatusList where assetStatusId equals to UPDATED_ASSET_STATUS_ID
        defaultAssetStatusShouldNotBeFound("assetStatusId.in=" + UPDATED_ASSET_STATUS_ID);
    }

    @Test
    @Transactional
    void getAllAssetStatusesByAssetStatusIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetStatusRepository.saveAndFlush(assetStatus);

        // Get all the assetStatusList where assetStatusId is not null
        defaultAssetStatusShouldBeFound("assetStatusId.specified=true");

        // Get all the assetStatusList where assetStatusId is null
        defaultAssetStatusShouldNotBeFound("assetStatusId.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetStatusesByAssetStatusIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetStatusRepository.saveAndFlush(assetStatus);

        // Get all the assetStatusList where assetStatusId is greater than or equal to DEFAULT_ASSET_STATUS_ID
        defaultAssetStatusShouldBeFound("assetStatusId.greaterThanOrEqual=" + DEFAULT_ASSET_STATUS_ID);

        // Get all the assetStatusList where assetStatusId is greater than or equal to UPDATED_ASSET_STATUS_ID
        defaultAssetStatusShouldNotBeFound("assetStatusId.greaterThanOrEqual=" + UPDATED_ASSET_STATUS_ID);
    }

    @Test
    @Transactional
    void getAllAssetStatusesByAssetStatusIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetStatusRepository.saveAndFlush(assetStatus);

        // Get all the assetStatusList where assetStatusId is less than or equal to DEFAULT_ASSET_STATUS_ID
        defaultAssetStatusShouldBeFound("assetStatusId.lessThanOrEqual=" + DEFAULT_ASSET_STATUS_ID);

        // Get all the assetStatusList where assetStatusId is less than or equal to SMALLER_ASSET_STATUS_ID
        defaultAssetStatusShouldNotBeFound("assetStatusId.lessThanOrEqual=" + SMALLER_ASSET_STATUS_ID);
    }

    @Test
    @Transactional
    void getAllAssetStatusesByAssetStatusIdIsLessThanSomething() throws Exception {
        // Initialize the database
        assetStatusRepository.saveAndFlush(assetStatus);

        // Get all the assetStatusList where assetStatusId is less than DEFAULT_ASSET_STATUS_ID
        defaultAssetStatusShouldNotBeFound("assetStatusId.lessThan=" + DEFAULT_ASSET_STATUS_ID);

        // Get all the assetStatusList where assetStatusId is less than UPDATED_ASSET_STATUS_ID
        defaultAssetStatusShouldBeFound("assetStatusId.lessThan=" + UPDATED_ASSET_STATUS_ID);
    }

    @Test
    @Transactional
    void getAllAssetStatusesByAssetStatusIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetStatusRepository.saveAndFlush(assetStatus);

        // Get all the assetStatusList where assetStatusId is greater than DEFAULT_ASSET_STATUS_ID
        defaultAssetStatusShouldNotBeFound("assetStatusId.greaterThan=" + DEFAULT_ASSET_STATUS_ID);

        // Get all the assetStatusList where assetStatusId is greater than SMALLER_ASSET_STATUS_ID
        defaultAssetStatusShouldBeFound("assetStatusId.greaterThan=" + SMALLER_ASSET_STATUS_ID);
    }

    @Test
    @Transactional
    void getAllAssetStatusesByAssetStatusNameIsEqualToSomething() throws Exception {
        // Initialize the database
        assetStatusRepository.saveAndFlush(assetStatus);

        // Get all the assetStatusList where assetStatusName equals to DEFAULT_ASSET_STATUS_NAME
        defaultAssetStatusShouldBeFound("assetStatusName.equals=" + DEFAULT_ASSET_STATUS_NAME);

        // Get all the assetStatusList where assetStatusName equals to UPDATED_ASSET_STATUS_NAME
        defaultAssetStatusShouldNotBeFound("assetStatusName.equals=" + UPDATED_ASSET_STATUS_NAME);
    }

    @Test
    @Transactional
    void getAllAssetStatusesByAssetStatusNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetStatusRepository.saveAndFlush(assetStatus);

        // Get all the assetStatusList where assetStatusName not equals to DEFAULT_ASSET_STATUS_NAME
        defaultAssetStatusShouldNotBeFound("assetStatusName.notEquals=" + DEFAULT_ASSET_STATUS_NAME);

        // Get all the assetStatusList where assetStatusName not equals to UPDATED_ASSET_STATUS_NAME
        defaultAssetStatusShouldBeFound("assetStatusName.notEquals=" + UPDATED_ASSET_STATUS_NAME);
    }

    @Test
    @Transactional
    void getAllAssetStatusesByAssetStatusNameIsInShouldWork() throws Exception {
        // Initialize the database
        assetStatusRepository.saveAndFlush(assetStatus);

        // Get all the assetStatusList where assetStatusName in DEFAULT_ASSET_STATUS_NAME or UPDATED_ASSET_STATUS_NAME
        defaultAssetStatusShouldBeFound("assetStatusName.in=" + DEFAULT_ASSET_STATUS_NAME + "," + UPDATED_ASSET_STATUS_NAME);

        // Get all the assetStatusList where assetStatusName equals to UPDATED_ASSET_STATUS_NAME
        defaultAssetStatusShouldNotBeFound("assetStatusName.in=" + UPDATED_ASSET_STATUS_NAME);
    }

    @Test
    @Transactional
    void getAllAssetStatusesByAssetStatusNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetStatusRepository.saveAndFlush(assetStatus);

        // Get all the assetStatusList where assetStatusName is not null
        defaultAssetStatusShouldBeFound("assetStatusName.specified=true");

        // Get all the assetStatusList where assetStatusName is null
        defaultAssetStatusShouldNotBeFound("assetStatusName.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetStatusesByAssetStatusNameContainsSomething() throws Exception {
        // Initialize the database
        assetStatusRepository.saveAndFlush(assetStatus);

        // Get all the assetStatusList where assetStatusName contains DEFAULT_ASSET_STATUS_NAME
        defaultAssetStatusShouldBeFound("assetStatusName.contains=" + DEFAULT_ASSET_STATUS_NAME);

        // Get all the assetStatusList where assetStatusName contains UPDATED_ASSET_STATUS_NAME
        defaultAssetStatusShouldNotBeFound("assetStatusName.contains=" + UPDATED_ASSET_STATUS_NAME);
    }

    @Test
    @Transactional
    void getAllAssetStatusesByAssetStatusNameNotContainsSomething() throws Exception {
        // Initialize the database
        assetStatusRepository.saveAndFlush(assetStatus);

        // Get all the assetStatusList where assetStatusName does not contain DEFAULT_ASSET_STATUS_NAME
        defaultAssetStatusShouldNotBeFound("assetStatusName.doesNotContain=" + DEFAULT_ASSET_STATUS_NAME);

        // Get all the assetStatusList where assetStatusName does not contain UPDATED_ASSET_STATUS_NAME
        defaultAssetStatusShouldBeFound("assetStatusName.doesNotContain=" + UPDATED_ASSET_STATUS_NAME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAssetStatusShouldBeFound(String filter) throws Exception {
        restAssetStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].assetStatusId").value(hasItem(DEFAULT_ASSET_STATUS_ID)))
            .andExpect(jsonPath("$.[*].assetStatusName").value(hasItem(DEFAULT_ASSET_STATUS_NAME)));

        // Check, that the count call also returns 1
        restAssetStatusMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAssetStatusShouldNotBeFound(String filter) throws Exception {
        restAssetStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAssetStatusMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAssetStatus() throws Exception {
        // Get the assetStatus
        restAssetStatusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAssetStatus() throws Exception {
        // Initialize the database
        assetStatusRepository.saveAndFlush(assetStatus);

        int databaseSizeBeforeUpdate = assetStatusRepository.findAll().size();

        // Update the assetStatus
        AssetStatus updatedAssetStatus = assetStatusRepository.findById(assetStatus.getId()).get();
        // Disconnect from session so that the updates on updatedAssetStatus are not directly saved in db
        em.detach(updatedAssetStatus);
        updatedAssetStatus.assetStatusId(UPDATED_ASSET_STATUS_ID).assetStatusName(UPDATED_ASSET_STATUS_NAME);
        AssetStatusDTO assetStatusDTO = assetStatusMapper.toDto(updatedAssetStatus);

        restAssetStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assetStatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetStatusDTO))
            )
            .andExpect(status().isOk());

        // Validate the AssetStatus in the database
        List<AssetStatus> assetStatusList = assetStatusRepository.findAll();
        assertThat(assetStatusList).hasSize(databaseSizeBeforeUpdate);
        AssetStatus testAssetStatus = assetStatusList.get(assetStatusList.size() - 1);
        assertThat(testAssetStatus.getAssetStatusId()).isEqualTo(UPDATED_ASSET_STATUS_ID);
        assertThat(testAssetStatus.getAssetStatusName()).isEqualTo(UPDATED_ASSET_STATUS_NAME);
    }

    @Test
    @Transactional
    void putNonExistingAssetStatus() throws Exception {
        int databaseSizeBeforeUpdate = assetStatusRepository.findAll().size();
        assetStatus.setId(count.incrementAndGet());

        // Create the AssetStatus
        AssetStatusDTO assetStatusDTO = assetStatusMapper.toDto(assetStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assetStatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetStatus in the database
        List<AssetStatus> assetStatusList = assetStatusRepository.findAll();
        assertThat(assetStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAssetStatus() throws Exception {
        int databaseSizeBeforeUpdate = assetStatusRepository.findAll().size();
        assetStatus.setId(count.incrementAndGet());

        // Create the AssetStatus
        AssetStatusDTO assetStatusDTO = assetStatusMapper.toDto(assetStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetStatus in the database
        List<AssetStatus> assetStatusList = assetStatusRepository.findAll();
        assertThat(assetStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAssetStatus() throws Exception {
        int databaseSizeBeforeUpdate = assetStatusRepository.findAll().size();
        assetStatus.setId(count.incrementAndGet());

        // Create the AssetStatus
        AssetStatusDTO assetStatusDTO = assetStatusMapper.toDto(assetStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetStatusMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetStatusDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssetStatus in the database
        List<AssetStatus> assetStatusList = assetStatusRepository.findAll();
        assertThat(assetStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAssetStatusWithPatch() throws Exception {
        // Initialize the database
        assetStatusRepository.saveAndFlush(assetStatus);

        int databaseSizeBeforeUpdate = assetStatusRepository.findAll().size();

        // Update the assetStatus using partial update
        AssetStatus partialUpdatedAssetStatus = new AssetStatus();
        partialUpdatedAssetStatus.setId(assetStatus.getId());

        partialUpdatedAssetStatus.assetStatusId(UPDATED_ASSET_STATUS_ID);

        restAssetStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssetStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssetStatus))
            )
            .andExpect(status().isOk());

        // Validate the AssetStatus in the database
        List<AssetStatus> assetStatusList = assetStatusRepository.findAll();
        assertThat(assetStatusList).hasSize(databaseSizeBeforeUpdate);
        AssetStatus testAssetStatus = assetStatusList.get(assetStatusList.size() - 1);
        assertThat(testAssetStatus.getAssetStatusId()).isEqualTo(UPDATED_ASSET_STATUS_ID);
        assertThat(testAssetStatus.getAssetStatusName()).isEqualTo(DEFAULT_ASSET_STATUS_NAME);
    }

    @Test
    @Transactional
    void fullUpdateAssetStatusWithPatch() throws Exception {
        // Initialize the database
        assetStatusRepository.saveAndFlush(assetStatus);

        int databaseSizeBeforeUpdate = assetStatusRepository.findAll().size();

        // Update the assetStatus using partial update
        AssetStatus partialUpdatedAssetStatus = new AssetStatus();
        partialUpdatedAssetStatus.setId(assetStatus.getId());

        partialUpdatedAssetStatus.assetStatusId(UPDATED_ASSET_STATUS_ID).assetStatusName(UPDATED_ASSET_STATUS_NAME);

        restAssetStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssetStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssetStatus))
            )
            .andExpect(status().isOk());

        // Validate the AssetStatus in the database
        List<AssetStatus> assetStatusList = assetStatusRepository.findAll();
        assertThat(assetStatusList).hasSize(databaseSizeBeforeUpdate);
        AssetStatus testAssetStatus = assetStatusList.get(assetStatusList.size() - 1);
        assertThat(testAssetStatus.getAssetStatusId()).isEqualTo(UPDATED_ASSET_STATUS_ID);
        assertThat(testAssetStatus.getAssetStatusName()).isEqualTo(UPDATED_ASSET_STATUS_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingAssetStatus() throws Exception {
        int databaseSizeBeforeUpdate = assetStatusRepository.findAll().size();
        assetStatus.setId(count.incrementAndGet());

        // Create the AssetStatus
        AssetStatusDTO assetStatusDTO = assetStatusMapper.toDto(assetStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, assetStatusDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assetStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetStatus in the database
        List<AssetStatus> assetStatusList = assetStatusRepository.findAll();
        assertThat(assetStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAssetStatus() throws Exception {
        int databaseSizeBeforeUpdate = assetStatusRepository.findAll().size();
        assetStatus.setId(count.incrementAndGet());

        // Create the AssetStatus
        AssetStatusDTO assetStatusDTO = assetStatusMapper.toDto(assetStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assetStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetStatus in the database
        List<AssetStatus> assetStatusList = assetStatusRepository.findAll();
        assertThat(assetStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAssetStatus() throws Exception {
        int databaseSizeBeforeUpdate = assetStatusRepository.findAll().size();
        assetStatus.setId(count.incrementAndGet());

        // Create the AssetStatus
        AssetStatusDTO assetStatusDTO = assetStatusMapper.toDto(assetStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetStatusMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(assetStatusDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssetStatus in the database
        List<AssetStatus> assetStatusList = assetStatusRepository.findAll();
        assertThat(assetStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAssetStatus() throws Exception {
        // Initialize the database
        assetStatusRepository.saveAndFlush(assetStatus);

        int databaseSizeBeforeDelete = assetStatusRepository.findAll().size();

        // Delete the assetStatus
        restAssetStatusMockMvc
            .perform(delete(ENTITY_API_URL_ID, assetStatus.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AssetStatus> assetStatusList = assetStatusRepository.findAll();
        assertThat(assetStatusList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
