package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Asset;
import com.mycompany.myapp.repository.AssetRepository;
import com.mycompany.myapp.service.criteria.AssetCriteria;
import com.mycompany.myapp.service.dto.AssetDTO;
import com.mycompany.myapp.service.mapper.AssetMapper;
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
 * Integration tests for the {@link AssetResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AssetResourceIT {

    private static final Integer DEFAULT_ASSET_ID = 1;
    private static final Integer UPDATED_ASSET_ID = 2;
    private static final Integer SMALLER_ASSET_ID = 1 - 1;

    private static final String DEFAULT_ASSET_SERIAL = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_SERIAL = "BBBBBBBBBB";

    private static final Integer DEFAULT_ASSET_STATUS = 1;
    private static final Integer UPDATED_ASSET_STATUS = 2;
    private static final Integer SMALLER_ASSET_STATUS = 1 - 1;

    private static final String ENTITY_API_URL = "/api/assets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private AssetMapper assetMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAssetMockMvc;

    private Asset asset;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Asset createEntity(EntityManager em) {
        Asset asset = new Asset().assetId(DEFAULT_ASSET_ID).assetSerial(DEFAULT_ASSET_SERIAL).assetStatus(DEFAULT_ASSET_STATUS);
        return asset;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Asset createUpdatedEntity(EntityManager em) {
        Asset asset = new Asset().assetId(UPDATED_ASSET_ID).assetSerial(UPDATED_ASSET_SERIAL).assetStatus(UPDATED_ASSET_STATUS);
        return asset;
    }

    @BeforeEach
    public void initTest() {
        asset = createEntity(em);
    }

    @Test
    @Transactional
    void createAsset() throws Exception {
        int databaseSizeBeforeCreate = assetRepository.findAll().size();
        // Create the Asset
        AssetDTO assetDTO = assetMapper.toDto(asset);
        restAssetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetDTO)))
            .andExpect(status().isCreated());

        // Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeCreate + 1);
        Asset testAsset = assetList.get(assetList.size() - 1);
        assertThat(testAsset.getAssetId()).isEqualTo(DEFAULT_ASSET_ID);
        assertThat(testAsset.getAssetSerial()).isEqualTo(DEFAULT_ASSET_SERIAL);
        assertThat(testAsset.getAssetStatus()).isEqualTo(DEFAULT_ASSET_STATUS);
    }

    @Test
    @Transactional
    void createAssetWithExistingId() throws Exception {
        // Create the Asset with an existing ID
        asset.setId(1L);
        AssetDTO assetDTO = assetMapper.toDto(asset);

        int databaseSizeBeforeCreate = assetRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAssets() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList
        restAssetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(asset.getId().intValue())))
            .andExpect(jsonPath("$.[*].assetId").value(hasItem(DEFAULT_ASSET_ID)))
            .andExpect(jsonPath("$.[*].assetSerial").value(hasItem(DEFAULT_ASSET_SERIAL)))
            .andExpect(jsonPath("$.[*].assetStatus").value(hasItem(DEFAULT_ASSET_STATUS)));
    }

    @Test
    @Transactional
    void getAsset() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get the asset
        restAssetMockMvc
            .perform(get(ENTITY_API_URL_ID, asset.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(asset.getId().intValue()))
            .andExpect(jsonPath("$.assetId").value(DEFAULT_ASSET_ID))
            .andExpect(jsonPath("$.assetSerial").value(DEFAULT_ASSET_SERIAL))
            .andExpect(jsonPath("$.assetStatus").value(DEFAULT_ASSET_STATUS));
    }

    @Test
    @Transactional
    void getAssetsByIdFiltering() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        Long id = asset.getId();

        defaultAssetShouldBeFound("id.equals=" + id);
        defaultAssetShouldNotBeFound("id.notEquals=" + id);

        defaultAssetShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAssetShouldNotBeFound("id.greaterThan=" + id);

        defaultAssetShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAssetShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAssetsByAssetIdIsEqualToSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where assetId equals to DEFAULT_ASSET_ID
        defaultAssetShouldBeFound("assetId.equals=" + DEFAULT_ASSET_ID);

        // Get all the assetList where assetId equals to UPDATED_ASSET_ID
        defaultAssetShouldNotBeFound("assetId.equals=" + UPDATED_ASSET_ID);
    }

    @Test
    @Transactional
    void getAllAssetsByAssetIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where assetId not equals to DEFAULT_ASSET_ID
        defaultAssetShouldNotBeFound("assetId.notEquals=" + DEFAULT_ASSET_ID);

        // Get all the assetList where assetId not equals to UPDATED_ASSET_ID
        defaultAssetShouldBeFound("assetId.notEquals=" + UPDATED_ASSET_ID);
    }

    @Test
    @Transactional
    void getAllAssetsByAssetIdIsInShouldWork() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where assetId in DEFAULT_ASSET_ID or UPDATED_ASSET_ID
        defaultAssetShouldBeFound("assetId.in=" + DEFAULT_ASSET_ID + "," + UPDATED_ASSET_ID);

        // Get all the assetList where assetId equals to UPDATED_ASSET_ID
        defaultAssetShouldNotBeFound("assetId.in=" + UPDATED_ASSET_ID);
    }

    @Test
    @Transactional
    void getAllAssetsByAssetIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where assetId is not null
        defaultAssetShouldBeFound("assetId.specified=true");

        // Get all the assetList where assetId is null
        defaultAssetShouldNotBeFound("assetId.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetsByAssetIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where assetId is greater than or equal to DEFAULT_ASSET_ID
        defaultAssetShouldBeFound("assetId.greaterThanOrEqual=" + DEFAULT_ASSET_ID);

        // Get all the assetList where assetId is greater than or equal to UPDATED_ASSET_ID
        defaultAssetShouldNotBeFound("assetId.greaterThanOrEqual=" + UPDATED_ASSET_ID);
    }

    @Test
    @Transactional
    void getAllAssetsByAssetIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where assetId is less than or equal to DEFAULT_ASSET_ID
        defaultAssetShouldBeFound("assetId.lessThanOrEqual=" + DEFAULT_ASSET_ID);

        // Get all the assetList where assetId is less than or equal to SMALLER_ASSET_ID
        defaultAssetShouldNotBeFound("assetId.lessThanOrEqual=" + SMALLER_ASSET_ID);
    }

    @Test
    @Transactional
    void getAllAssetsByAssetIdIsLessThanSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where assetId is less than DEFAULT_ASSET_ID
        defaultAssetShouldNotBeFound("assetId.lessThan=" + DEFAULT_ASSET_ID);

        // Get all the assetList where assetId is less than UPDATED_ASSET_ID
        defaultAssetShouldBeFound("assetId.lessThan=" + UPDATED_ASSET_ID);
    }

    @Test
    @Transactional
    void getAllAssetsByAssetIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where assetId is greater than DEFAULT_ASSET_ID
        defaultAssetShouldNotBeFound("assetId.greaterThan=" + DEFAULT_ASSET_ID);

        // Get all the assetList where assetId is greater than SMALLER_ASSET_ID
        defaultAssetShouldBeFound("assetId.greaterThan=" + SMALLER_ASSET_ID);
    }

    @Test
    @Transactional
    void getAllAssetsByAssetSerialIsEqualToSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where assetSerial equals to DEFAULT_ASSET_SERIAL
        defaultAssetShouldBeFound("assetSerial.equals=" + DEFAULT_ASSET_SERIAL);

        // Get all the assetList where assetSerial equals to UPDATED_ASSET_SERIAL
        defaultAssetShouldNotBeFound("assetSerial.equals=" + UPDATED_ASSET_SERIAL);
    }

    @Test
    @Transactional
    void getAllAssetsByAssetSerialIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where assetSerial not equals to DEFAULT_ASSET_SERIAL
        defaultAssetShouldNotBeFound("assetSerial.notEquals=" + DEFAULT_ASSET_SERIAL);

        // Get all the assetList where assetSerial not equals to UPDATED_ASSET_SERIAL
        defaultAssetShouldBeFound("assetSerial.notEquals=" + UPDATED_ASSET_SERIAL);
    }

    @Test
    @Transactional
    void getAllAssetsByAssetSerialIsInShouldWork() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where assetSerial in DEFAULT_ASSET_SERIAL or UPDATED_ASSET_SERIAL
        defaultAssetShouldBeFound("assetSerial.in=" + DEFAULT_ASSET_SERIAL + "," + UPDATED_ASSET_SERIAL);

        // Get all the assetList where assetSerial equals to UPDATED_ASSET_SERIAL
        defaultAssetShouldNotBeFound("assetSerial.in=" + UPDATED_ASSET_SERIAL);
    }

    @Test
    @Transactional
    void getAllAssetsByAssetSerialIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where assetSerial is not null
        defaultAssetShouldBeFound("assetSerial.specified=true");

        // Get all the assetList where assetSerial is null
        defaultAssetShouldNotBeFound("assetSerial.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetsByAssetSerialContainsSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where assetSerial contains DEFAULT_ASSET_SERIAL
        defaultAssetShouldBeFound("assetSerial.contains=" + DEFAULT_ASSET_SERIAL);

        // Get all the assetList where assetSerial contains UPDATED_ASSET_SERIAL
        defaultAssetShouldNotBeFound("assetSerial.contains=" + UPDATED_ASSET_SERIAL);
    }

    @Test
    @Transactional
    void getAllAssetsByAssetSerialNotContainsSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where assetSerial does not contain DEFAULT_ASSET_SERIAL
        defaultAssetShouldNotBeFound("assetSerial.doesNotContain=" + DEFAULT_ASSET_SERIAL);

        // Get all the assetList where assetSerial does not contain UPDATED_ASSET_SERIAL
        defaultAssetShouldBeFound("assetSerial.doesNotContain=" + UPDATED_ASSET_SERIAL);
    }

    @Test
    @Transactional
    void getAllAssetsByAssetStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where assetStatus equals to DEFAULT_ASSET_STATUS
        defaultAssetShouldBeFound("assetStatus.equals=" + DEFAULT_ASSET_STATUS);

        // Get all the assetList where assetStatus equals to UPDATED_ASSET_STATUS
        defaultAssetShouldNotBeFound("assetStatus.equals=" + UPDATED_ASSET_STATUS);
    }

    @Test
    @Transactional
    void getAllAssetsByAssetStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where assetStatus not equals to DEFAULT_ASSET_STATUS
        defaultAssetShouldNotBeFound("assetStatus.notEquals=" + DEFAULT_ASSET_STATUS);

        // Get all the assetList where assetStatus not equals to UPDATED_ASSET_STATUS
        defaultAssetShouldBeFound("assetStatus.notEquals=" + UPDATED_ASSET_STATUS);
    }

    @Test
    @Transactional
    void getAllAssetsByAssetStatusIsInShouldWork() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where assetStatus in DEFAULT_ASSET_STATUS or UPDATED_ASSET_STATUS
        defaultAssetShouldBeFound("assetStatus.in=" + DEFAULT_ASSET_STATUS + "," + UPDATED_ASSET_STATUS);

        // Get all the assetList where assetStatus equals to UPDATED_ASSET_STATUS
        defaultAssetShouldNotBeFound("assetStatus.in=" + UPDATED_ASSET_STATUS);
    }

    @Test
    @Transactional
    void getAllAssetsByAssetStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where assetStatus is not null
        defaultAssetShouldBeFound("assetStatus.specified=true");

        // Get all the assetList where assetStatus is null
        defaultAssetShouldNotBeFound("assetStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetsByAssetStatusIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where assetStatus is greater than or equal to DEFAULT_ASSET_STATUS
        defaultAssetShouldBeFound("assetStatus.greaterThanOrEqual=" + DEFAULT_ASSET_STATUS);

        // Get all the assetList where assetStatus is greater than or equal to UPDATED_ASSET_STATUS
        defaultAssetShouldNotBeFound("assetStatus.greaterThanOrEqual=" + UPDATED_ASSET_STATUS);
    }

    @Test
    @Transactional
    void getAllAssetsByAssetStatusIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where assetStatus is less than or equal to DEFAULT_ASSET_STATUS
        defaultAssetShouldBeFound("assetStatus.lessThanOrEqual=" + DEFAULT_ASSET_STATUS);

        // Get all the assetList where assetStatus is less than or equal to SMALLER_ASSET_STATUS
        defaultAssetShouldNotBeFound("assetStatus.lessThanOrEqual=" + SMALLER_ASSET_STATUS);
    }

    @Test
    @Transactional
    void getAllAssetsByAssetStatusIsLessThanSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where assetStatus is less than DEFAULT_ASSET_STATUS
        defaultAssetShouldNotBeFound("assetStatus.lessThan=" + DEFAULT_ASSET_STATUS);

        // Get all the assetList where assetStatus is less than UPDATED_ASSET_STATUS
        defaultAssetShouldBeFound("assetStatus.lessThan=" + UPDATED_ASSET_STATUS);
    }

    @Test
    @Transactional
    void getAllAssetsByAssetStatusIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where assetStatus is greater than DEFAULT_ASSET_STATUS
        defaultAssetShouldNotBeFound("assetStatus.greaterThan=" + DEFAULT_ASSET_STATUS);

        // Get all the assetList where assetStatus is greater than SMALLER_ASSET_STATUS
        defaultAssetShouldBeFound("assetStatus.greaterThan=" + SMALLER_ASSET_STATUS);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAssetShouldBeFound(String filter) throws Exception {
        restAssetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(asset.getId().intValue())))
            .andExpect(jsonPath("$.[*].assetId").value(hasItem(DEFAULT_ASSET_ID)))
            .andExpect(jsonPath("$.[*].assetSerial").value(hasItem(DEFAULT_ASSET_SERIAL)))
            .andExpect(jsonPath("$.[*].assetStatus").value(hasItem(DEFAULT_ASSET_STATUS)));

        // Check, that the count call also returns 1
        restAssetMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAssetShouldNotBeFound(String filter) throws Exception {
        restAssetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAssetMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAsset() throws Exception {
        // Get the asset
        restAssetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAsset() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        int databaseSizeBeforeUpdate = assetRepository.findAll().size();

        // Update the asset
        Asset updatedAsset = assetRepository.findById(asset.getId()).get();
        // Disconnect from session so that the updates on updatedAsset are not directly saved in db
        em.detach(updatedAsset);
        updatedAsset.assetId(UPDATED_ASSET_ID).assetSerial(UPDATED_ASSET_SERIAL).assetStatus(UPDATED_ASSET_STATUS);
        AssetDTO assetDTO = assetMapper.toDto(updatedAsset);

        restAssetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assetDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetDTO))
            )
            .andExpect(status().isOk());

        // Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeUpdate);
        Asset testAsset = assetList.get(assetList.size() - 1);
        assertThat(testAsset.getAssetId()).isEqualTo(UPDATED_ASSET_ID);
        assertThat(testAsset.getAssetSerial()).isEqualTo(UPDATED_ASSET_SERIAL);
        assertThat(testAsset.getAssetStatus()).isEqualTo(UPDATED_ASSET_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingAsset() throws Exception {
        int databaseSizeBeforeUpdate = assetRepository.findAll().size();
        asset.setId(count.incrementAndGet());

        // Create the Asset
        AssetDTO assetDTO = assetMapper.toDto(asset);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assetDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAsset() throws Exception {
        int databaseSizeBeforeUpdate = assetRepository.findAll().size();
        asset.setId(count.incrementAndGet());

        // Create the Asset
        AssetDTO assetDTO = assetMapper.toDto(asset);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAsset() throws Exception {
        int databaseSizeBeforeUpdate = assetRepository.findAll().size();
        asset.setId(count.incrementAndGet());

        // Create the Asset
        AssetDTO assetDTO = assetMapper.toDto(asset);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAssetWithPatch() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        int databaseSizeBeforeUpdate = assetRepository.findAll().size();

        // Update the asset using partial update
        Asset partialUpdatedAsset = new Asset();
        partialUpdatedAsset.setId(asset.getId());

        partialUpdatedAsset.assetId(UPDATED_ASSET_ID).assetStatus(UPDATED_ASSET_STATUS);

        restAssetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAsset.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAsset))
            )
            .andExpect(status().isOk());

        // Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeUpdate);
        Asset testAsset = assetList.get(assetList.size() - 1);
        assertThat(testAsset.getAssetId()).isEqualTo(UPDATED_ASSET_ID);
        assertThat(testAsset.getAssetSerial()).isEqualTo(DEFAULT_ASSET_SERIAL);
        assertThat(testAsset.getAssetStatus()).isEqualTo(UPDATED_ASSET_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateAssetWithPatch() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        int databaseSizeBeforeUpdate = assetRepository.findAll().size();

        // Update the asset using partial update
        Asset partialUpdatedAsset = new Asset();
        partialUpdatedAsset.setId(asset.getId());

        partialUpdatedAsset.assetId(UPDATED_ASSET_ID).assetSerial(UPDATED_ASSET_SERIAL).assetStatus(UPDATED_ASSET_STATUS);

        restAssetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAsset.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAsset))
            )
            .andExpect(status().isOk());

        // Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeUpdate);
        Asset testAsset = assetList.get(assetList.size() - 1);
        assertThat(testAsset.getAssetId()).isEqualTo(UPDATED_ASSET_ID);
        assertThat(testAsset.getAssetSerial()).isEqualTo(UPDATED_ASSET_SERIAL);
        assertThat(testAsset.getAssetStatus()).isEqualTo(UPDATED_ASSET_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingAsset() throws Exception {
        int databaseSizeBeforeUpdate = assetRepository.findAll().size();
        asset.setId(count.incrementAndGet());

        // Create the Asset
        AssetDTO assetDTO = assetMapper.toDto(asset);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, assetDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAsset() throws Exception {
        int databaseSizeBeforeUpdate = assetRepository.findAll().size();
        asset.setId(count.incrementAndGet());

        // Create the Asset
        AssetDTO assetDTO = assetMapper.toDto(asset);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAsset() throws Exception {
        int databaseSizeBeforeUpdate = assetRepository.findAll().size();
        asset.setId(count.incrementAndGet());

        // Create the Asset
        AssetDTO assetDTO = assetMapper.toDto(asset);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(assetDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAsset() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        int databaseSizeBeforeDelete = assetRepository.findAll().size();

        // Delete the asset
        restAssetMockMvc
            .perform(delete(ENTITY_API_URL_ID, asset.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
