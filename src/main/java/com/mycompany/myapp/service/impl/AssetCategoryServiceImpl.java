package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.AssetCategory;
import com.mycompany.myapp.repository.AssetCategoryRepository;
import com.mycompany.myapp.service.AssetCategoryService;
import com.mycompany.myapp.service.dto.AssetCategoryDTO;
import com.mycompany.myapp.service.mapper.AssetCategoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AssetCategory}.
 */
@Service
@Transactional
public class AssetCategoryServiceImpl implements AssetCategoryService {

    private final Logger log = LoggerFactory.getLogger(AssetCategoryServiceImpl.class);

    private final AssetCategoryRepository assetCategoryRepository;

    private final AssetCategoryMapper assetCategoryMapper;

    public AssetCategoryServiceImpl(AssetCategoryRepository assetCategoryRepository, AssetCategoryMapper assetCategoryMapper) {
        this.assetCategoryRepository = assetCategoryRepository;
        this.assetCategoryMapper = assetCategoryMapper;
    }

    @Override
    public AssetCategoryDTO save(AssetCategoryDTO assetCategoryDTO) {
        log.debug("Request to save AssetCategory : {}", assetCategoryDTO);
        AssetCategory assetCategory = assetCategoryMapper.toEntity(assetCategoryDTO);
        assetCategory = assetCategoryRepository.save(assetCategory);
        return assetCategoryMapper.toDto(assetCategory);
    }

    @Override
    public AssetCategoryDTO update(AssetCategoryDTO assetCategoryDTO) {
        log.debug("Request to save AssetCategory : {}", assetCategoryDTO);
        AssetCategory assetCategory = assetCategoryMapper.toEntity(assetCategoryDTO);
        assetCategory = assetCategoryRepository.save(assetCategory);
        return assetCategoryMapper.toDto(assetCategory);
    }

    @Override
    public Optional<AssetCategoryDTO> partialUpdate(AssetCategoryDTO assetCategoryDTO) {
        log.debug("Request to partially update AssetCategory : {}", assetCategoryDTO);

        return assetCategoryRepository
            .findById(assetCategoryDTO.getId())
            .map(existingAssetCategory -> {
                assetCategoryMapper.partialUpdate(existingAssetCategory, assetCategoryDTO);

                return existingAssetCategory;
            })
            .map(assetCategoryRepository::save)
            .map(assetCategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetCategoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AssetCategories");
        return assetCategoryRepository.findAll(pageable).map(assetCategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AssetCategoryDTO> findOne(Long id) {
        log.debug("Request to get AssetCategory : {}", id);
        return assetCategoryRepository.findById(id).map(assetCategoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AssetCategory : {}", id);
        assetCategoryRepository.deleteById(id);
    }
}
