package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Candidate;
import com.mycompany.myapp.repository.CandidateRepository;
import com.mycompany.myapp.service.CandidateService;
import com.mycompany.myapp.service.dto.CandidateDTO;
import com.mycompany.myapp.service.mapper.CandidateMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Candidate}.
 */
@Service
@Transactional
public class CandidateServiceImpl implements CandidateService {

    private final Logger log = LoggerFactory.getLogger(CandidateServiceImpl.class);

    private final CandidateRepository candidateRepository;

    private final CandidateMapper candidateMapper;

    public CandidateServiceImpl(CandidateRepository candidateRepository, CandidateMapper candidateMapper) {
        this.candidateRepository = candidateRepository;
        this.candidateMapper = candidateMapper;
    }

    @Override
    public CandidateDTO save(CandidateDTO candidateDTO) {
        log.debug("Request to save Candidate : {}", candidateDTO);
        Candidate candidate = candidateMapper.toEntity(candidateDTO);
        candidate = candidateRepository.save(candidate);
        return candidateMapper.toDto(candidate);
    }

    @Override
    public CandidateDTO update(CandidateDTO candidateDTO) {
        log.debug("Request to save Candidate : {}", candidateDTO);
        Candidate candidate = candidateMapper.toEntity(candidateDTO);
        candidate = candidateRepository.save(candidate);
        return candidateMapper.toDto(candidate);
    }

    @Override
    public Optional<CandidateDTO> partialUpdate(CandidateDTO candidateDTO) {
        log.debug("Request to partially update Candidate : {}", candidateDTO);

        return candidateRepository
            .findById(candidateDTO.getId())
            .map(existingCandidate -> {
                candidateMapper.partialUpdate(existingCandidate, candidateDTO);

                return existingCandidate;
            })
            .map(candidateRepository::save)
            .map(candidateMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CandidateDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Candidates");
        return candidateRepository.findAll(pageable).map(candidateMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CandidateDTO> findOne(Long id) {
        log.debug("Request to get Candidate : {}", id);
        return candidateRepository.findById(id).map(candidateMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Candidate : {}", id);
        candidateRepository.deleteById(id);
    }
}
