package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.RuralDistrict;
import com.mycompany.myapp.repository.RuralDistrictRepository;
import com.mycompany.myapp.service.RuralDistrictService;
import com.mycompany.myapp.service.dto.RuralDistrictDTO;
import com.mycompany.myapp.service.mapper.RuralDistrictMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RuralDistrict}.
 */
@Service
@Transactional
public class RuralDistrictServiceImpl implements RuralDistrictService {

    private final Logger log = LoggerFactory.getLogger(RuralDistrictServiceImpl.class);

    private final RuralDistrictRepository ruralDistrictRepository;

    private final RuralDistrictMapper ruralDistrictMapper;

    public RuralDistrictServiceImpl(RuralDistrictRepository ruralDistrictRepository, RuralDistrictMapper ruralDistrictMapper) {
        this.ruralDistrictRepository = ruralDistrictRepository;
        this.ruralDistrictMapper = ruralDistrictMapper;
    }

    @Override
    public RuralDistrictDTO save(RuralDistrictDTO ruralDistrictDTO) {
        log.debug("Request to save RuralDistrict : {}", ruralDistrictDTO);
        RuralDistrict ruralDistrict = ruralDistrictMapper.toEntity(ruralDistrictDTO);
        ruralDistrict = ruralDistrictRepository.save(ruralDistrict);
        return ruralDistrictMapper.toDto(ruralDistrict);
    }

    @Override
    public RuralDistrictDTO update(RuralDistrictDTO ruralDistrictDTO) {
        log.debug("Request to save RuralDistrict : {}", ruralDistrictDTO);
        RuralDistrict ruralDistrict = ruralDistrictMapper.toEntity(ruralDistrictDTO);
        ruralDistrict = ruralDistrictRepository.save(ruralDistrict);
        return ruralDistrictMapper.toDto(ruralDistrict);
    }

    @Override
    public Optional<RuralDistrictDTO> partialUpdate(RuralDistrictDTO ruralDistrictDTO) {
        log.debug("Request to partially update RuralDistrict : {}", ruralDistrictDTO);

        return ruralDistrictRepository
            .findById(ruralDistrictDTO.getId())
            .map(existingRuralDistrict -> {
                ruralDistrictMapper.partialUpdate(existingRuralDistrict, ruralDistrictDTO);

                return existingRuralDistrict;
            })
            .map(ruralDistrictRepository::save)
            .map(ruralDistrictMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RuralDistrictDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RuralDistricts");
        return ruralDistrictRepository.findAll(pageable).map(ruralDistrictMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RuralDistrictDTO> findOne(Long id) {
        log.debug("Request to get RuralDistrict : {}", id);
        return ruralDistrictRepository.findById(id).map(ruralDistrictMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RuralDistrict : {}", id);
        ruralDistrictRepository.deleteById(id);
    }
}
