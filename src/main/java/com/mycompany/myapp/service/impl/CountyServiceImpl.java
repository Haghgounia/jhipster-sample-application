package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.County;
import com.mycompany.myapp.repository.CountyRepository;
import com.mycompany.myapp.service.CountyService;
import com.mycompany.myapp.service.dto.CountyDTO;
import com.mycompany.myapp.service.mapper.CountyMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link County}.
 */
@Service
@Transactional
public class CountyServiceImpl implements CountyService {

    private final Logger log = LoggerFactory.getLogger(CountyServiceImpl.class);

    private final CountyRepository countyRepository;

    private final CountyMapper countyMapper;

    public CountyServiceImpl(CountyRepository countyRepository, CountyMapper countyMapper) {
        this.countyRepository = countyRepository;
        this.countyMapper = countyMapper;
    }

    @Override
    public CountyDTO save(CountyDTO countyDTO) {
        log.debug("Request to save County : {}", countyDTO);
        County county = countyMapper.toEntity(countyDTO);
        county = countyRepository.save(county);
        return countyMapper.toDto(county);
    }

    @Override
    public CountyDTO update(CountyDTO countyDTO) {
        log.debug("Request to save County : {}", countyDTO);
        County county = countyMapper.toEntity(countyDTO);
        county = countyRepository.save(county);
        return countyMapper.toDto(county);
    }

    @Override
    public Optional<CountyDTO> partialUpdate(CountyDTO countyDTO) {
        log.debug("Request to partially update County : {}", countyDTO);

        return countyRepository
            .findById(countyDTO.getId())
            .map(existingCounty -> {
                countyMapper.partialUpdate(existingCounty, countyDTO);

                return existingCounty;
            })
            .map(countyRepository::save)
            .map(countyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CountyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Counties");
        return countyRepository.findAll(pageable).map(countyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CountyDTO> findOne(Long id) {
        log.debug("Request to get County : {}", id);
        return countyRepository.findById(id).map(countyMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete County : {}", id);
        countyRepository.deleteById(id);
    }
}
