package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.JobOpening;
import com.mycompany.myapp.repository.JobOpeningRepository;
import com.mycompany.myapp.service.JobOpeningService;
import com.mycompany.myapp.service.dto.JobOpeningDTO;
import com.mycompany.myapp.service.mapper.JobOpeningMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link JobOpening}.
 */
@Service
@Transactional
public class JobOpeningServiceImpl implements JobOpeningService {

    private final Logger log = LoggerFactory.getLogger(JobOpeningServiceImpl.class);

    private final JobOpeningRepository jobOpeningRepository;

    private final JobOpeningMapper jobOpeningMapper;

    public JobOpeningServiceImpl(JobOpeningRepository jobOpeningRepository, JobOpeningMapper jobOpeningMapper) {
        this.jobOpeningRepository = jobOpeningRepository;
        this.jobOpeningMapper = jobOpeningMapper;
    }

    @Override
    public JobOpeningDTO save(JobOpeningDTO jobOpeningDTO) {
        log.debug("Request to save JobOpening : {}", jobOpeningDTO);
        JobOpening jobOpening = jobOpeningMapper.toEntity(jobOpeningDTO);
        jobOpening = jobOpeningRepository.save(jobOpening);
        return jobOpeningMapper.toDto(jobOpening);
    }

    @Override
    public JobOpeningDTO update(JobOpeningDTO jobOpeningDTO) {
        log.debug("Request to save JobOpening : {}", jobOpeningDTO);
        JobOpening jobOpening = jobOpeningMapper.toEntity(jobOpeningDTO);
        jobOpening = jobOpeningRepository.save(jobOpening);
        return jobOpeningMapper.toDto(jobOpening);
    }

    @Override
    public Optional<JobOpeningDTO> partialUpdate(JobOpeningDTO jobOpeningDTO) {
        log.debug("Request to partially update JobOpening : {}", jobOpeningDTO);

        return jobOpeningRepository
            .findById(jobOpeningDTO.getId())
            .map(existingJobOpening -> {
                jobOpeningMapper.partialUpdate(existingJobOpening, jobOpeningDTO);

                return existingJobOpening;
            })
            .map(jobOpeningRepository::save)
            .map(jobOpeningMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JobOpeningDTO> findAll(Pageable pageable) {
        log.debug("Request to get all JobOpenings");
        return jobOpeningRepository.findAll(pageable).map(jobOpeningMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<JobOpeningDTO> findOne(Long id) {
        log.debug("Request to get JobOpening : {}", id);
        return jobOpeningRepository.findById(id).map(jobOpeningMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete JobOpening : {}", id);
        jobOpeningRepository.deleteById(id);
    }
}
