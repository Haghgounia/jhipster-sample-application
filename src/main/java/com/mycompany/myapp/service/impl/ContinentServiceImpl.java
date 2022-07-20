package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Continent;
import com.mycompany.myapp.repository.ContinentRepository;
import com.mycompany.myapp.service.ContinentService;
import com.mycompany.myapp.service.dto.ContinentDTO;
import com.mycompany.myapp.service.mapper.ContinentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Continent}.
 */
@Service
@Transactional
public class ContinentServiceImpl implements ContinentService {

    private final Logger log = LoggerFactory.getLogger(ContinentServiceImpl.class);

    private final ContinentRepository continentRepository;

    private final ContinentMapper continentMapper;

    public ContinentServiceImpl(ContinentRepository continentRepository, ContinentMapper continentMapper) {
        this.continentRepository = continentRepository;
        this.continentMapper = continentMapper;
    }

    @Override
    public ContinentDTO save(ContinentDTO continentDTO) {
        log.debug("Request to save Continent : {}", continentDTO);
        Continent continent = continentMapper.toEntity(continentDTO);
        continent = continentRepository.save(continent);
        return continentMapper.toDto(continent);
    }

    @Override
    public ContinentDTO update(ContinentDTO continentDTO) {
        log.debug("Request to save Continent : {}", continentDTO);
        Continent continent = continentMapper.toEntity(continentDTO);
        continent = continentRepository.save(continent);
        return continentMapper.toDto(continent);
    }

    @Override
    public Optional<ContinentDTO> partialUpdate(ContinentDTO continentDTO) {
        log.debug("Request to partially update Continent : {}", continentDTO);

        return continentRepository
            .findById(continentDTO.getId())
            .map(existingContinent -> {
                continentMapper.partialUpdate(existingContinent, continentDTO);

                return existingContinent;
            })
            .map(continentRepository::save)
            .map(continentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContinentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Continents");
        return continentRepository.findAll(pageable).map(continentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ContinentDTO> findOne(Long id) {
        log.debug("Request to get Continent : {}", id);
        return continentRepository.findById(id).map(continentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Continent : {}", id);
        continentRepository.deleteById(id);
    }
}
