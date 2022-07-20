package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.AssetAssign;
import com.mycompany.myapp.repository.AssetAssignRepository;
import com.mycompany.myapp.service.AssetAssignService;
import com.mycompany.myapp.service.dto.AssetAssignDTO;
import com.mycompany.myapp.service.mapper.AssetAssignMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AssetAssign}.
 */
@Service
@Transactional
public class AssetAssignServiceImpl implements AssetAssignService {

    private final Logger log = LoggerFactory.getLogger(AssetAssignServiceImpl.class);

    private final AssetAssignRepository assetAssignRepository;

    private final AssetAssignMapper assetAssignMapper;

    public AssetAssignServiceImpl(AssetAssignRepository assetAssignRepository, AssetAssignMapper assetAssignMapper) {
        this.assetAssignRepository = assetAssignRepository;
        this.assetAssignMapper = assetAssignMapper;
    }

    @Override
    public AssetAssignDTO save(AssetAssignDTO assetAssignDTO) {
        log.debug("Request to save AssetAssign : {}", assetAssignDTO);
        AssetAssign assetAssign = assetAssignMapper.toEntity(assetAssignDTO);
        assetAssign = assetAssignRepository.save(assetAssign);
        return assetAssignMapper.toDto(assetAssign);
    }

    @Override
    public AssetAssignDTO update(AssetAssignDTO assetAssignDTO) {
        log.debug("Request to save AssetAssign : {}", assetAssignDTO);
        AssetAssign assetAssign = assetAssignMapper.toEntity(assetAssignDTO);
        assetAssign = assetAssignRepository.save(assetAssign);
        return assetAssignMapper.toDto(assetAssign);
    }

    @Override
    public Optional<AssetAssignDTO> partialUpdate(AssetAssignDTO assetAssignDTO) {
        log.debug("Request to partially update AssetAssign : {}", assetAssignDTO);

        return assetAssignRepository
            .findById(assetAssignDTO.getId())
            .map(existingAssetAssign -> {
                assetAssignMapper.partialUpdate(existingAssetAssign, assetAssignDTO);

                return existingAssetAssign;
            })
            .map(assetAssignRepository::save)
            .map(assetAssignMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetAssignDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AssetAssigns");
        return assetAssignRepository.findAll(pageable).map(assetAssignMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AssetAssignDTO> findOne(Long id) {
        log.debug("Request to get AssetAssign : {}", id);
        return assetAssignRepository.findById(id).map(assetAssignMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AssetAssign : {}", id);
        assetAssignRepository.deleteById(id);
    }
}
