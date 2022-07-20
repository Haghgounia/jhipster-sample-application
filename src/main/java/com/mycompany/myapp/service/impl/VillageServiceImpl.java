package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Village;
import com.mycompany.myapp.repository.VillageRepository;
import com.mycompany.myapp.service.VillageService;
import com.mycompany.myapp.service.dto.VillageDTO;
import com.mycompany.myapp.service.mapper.VillageMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Village}.
 */
@Service
@Transactional
public class VillageServiceImpl implements VillageService {

    private final Logger log = LoggerFactory.getLogger(VillageServiceImpl.class);

    private final VillageRepository villageRepository;

    private final VillageMapper villageMapper;

    public VillageServiceImpl(VillageRepository villageRepository, VillageMapper villageMapper) {
        this.villageRepository = villageRepository;
        this.villageMapper = villageMapper;
    }

    @Override
    public VillageDTO save(VillageDTO villageDTO) {
        log.debug("Request to save Village : {}", villageDTO);
        Village village = villageMapper.toEntity(villageDTO);
        village = villageRepository.save(village);
        return villageMapper.toDto(village);
    }

    @Override
    public VillageDTO update(VillageDTO villageDTO) {
        log.debug("Request to save Village : {}", villageDTO);
        Village village = villageMapper.toEntity(villageDTO);
        village = villageRepository.save(village);
        return villageMapper.toDto(village);
    }

    @Override
    public Optional<VillageDTO> partialUpdate(VillageDTO villageDTO) {
        log.debug("Request to partially update Village : {}", villageDTO);

        return villageRepository
            .findById(villageDTO.getId())
            .map(existingVillage -> {
                villageMapper.partialUpdate(existingVillage, villageDTO);

                return existingVillage;
            })
            .map(villageRepository::save)
            .map(villageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VillageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Villages");
        return villageRepository.findAll(pageable).map(villageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VillageDTO> findOne(Long id) {
        log.debug("Request to get Village : {}", id);
        return villageRepository.findById(id).map(villageMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Village : {}", id);
        villageRepository.deleteById(id);
    }
}
