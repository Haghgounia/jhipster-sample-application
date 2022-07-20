package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.AssetStatus;
import com.mycompany.myapp.repository.AssetStatusRepository;
import com.mycompany.myapp.service.AssetStatusService;
import com.mycompany.myapp.service.dto.AssetStatusDTO;
import com.mycompany.myapp.service.mapper.AssetStatusMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AssetStatus}.
 */
@Service
@Transactional
public class AssetStatusServiceImpl implements AssetStatusService {

    private final Logger log = LoggerFactory.getLogger(AssetStatusServiceImpl.class);

    private final AssetStatusRepository assetStatusRepository;

    private final AssetStatusMapper assetStatusMapper;

    public AssetStatusServiceImpl(AssetStatusRepository assetStatusRepository, AssetStatusMapper assetStatusMapper) {
        this.assetStatusRepository = assetStatusRepository;
        this.assetStatusMapper = assetStatusMapper;
    }

    @Override
    public AssetStatusDTO save(AssetStatusDTO assetStatusDTO) {
        log.debug("Request to save AssetStatus : {}", assetStatusDTO);
        AssetStatus assetStatus = assetStatusMapper.toEntity(assetStatusDTO);
        assetStatus = assetStatusRepository.save(assetStatus);
        return assetStatusMapper.toDto(assetStatus);
    }

    @Override
    public AssetStatusDTO update(AssetStatusDTO assetStatusDTO) {
        log.debug("Request to save AssetStatus : {}", assetStatusDTO);
        AssetStatus assetStatus = assetStatusMapper.toEntity(assetStatusDTO);
        assetStatus = assetStatusRepository.save(assetStatus);
        return assetStatusMapper.toDto(assetStatus);
    }

    @Override
    public Optional<AssetStatusDTO> partialUpdate(AssetStatusDTO assetStatusDTO) {
        log.debug("Request to partially update AssetStatus : {}", assetStatusDTO);

        return assetStatusRepository
            .findById(assetStatusDTO.getId())
            .map(existingAssetStatus -> {
                assetStatusMapper.partialUpdate(existingAssetStatus, assetStatusDTO);

                return existingAssetStatus;
            })
            .map(assetStatusRepository::save)
            .map(assetStatusMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetStatusDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AssetStatuses");
        return assetStatusRepository.findAll(pageable).map(assetStatusMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AssetStatusDTO> findOne(Long id) {
        log.debug("Request to get AssetStatus : {}", id);
        return assetStatusRepository.findById(id).map(assetStatusMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AssetStatus : {}", id);
        assetStatusRepository.deleteById(id);
    }
}
