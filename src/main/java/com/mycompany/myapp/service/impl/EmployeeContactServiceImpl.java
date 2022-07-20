package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.EmployeeContact;
import com.mycompany.myapp.repository.EmployeeContactRepository;
import com.mycompany.myapp.service.EmployeeContactService;
import com.mycompany.myapp.service.dto.EmployeeContactDTO;
import com.mycompany.myapp.service.mapper.EmployeeContactMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link EmployeeContact}.
 */
@Service
@Transactional
public class EmployeeContactServiceImpl implements EmployeeContactService {

    private final Logger log = LoggerFactory.getLogger(EmployeeContactServiceImpl.class);

    private final EmployeeContactRepository employeeContactRepository;

    private final EmployeeContactMapper employeeContactMapper;

    public EmployeeContactServiceImpl(EmployeeContactRepository employeeContactRepository, EmployeeContactMapper employeeContactMapper) {
        this.employeeContactRepository = employeeContactRepository;
        this.employeeContactMapper = employeeContactMapper;
    }

    @Override
    public EmployeeContactDTO save(EmployeeContactDTO employeeContactDTO) {
        log.debug("Request to save EmployeeContact : {}", employeeContactDTO);
        EmployeeContact employeeContact = employeeContactMapper.toEntity(employeeContactDTO);
        employeeContact = employeeContactRepository.save(employeeContact);
        return employeeContactMapper.toDto(employeeContact);
    }

    @Override
    public EmployeeContactDTO update(EmployeeContactDTO employeeContactDTO) {
        log.debug("Request to save EmployeeContact : {}", employeeContactDTO);
        EmployeeContact employeeContact = employeeContactMapper.toEntity(employeeContactDTO);
        employeeContact = employeeContactRepository.save(employeeContact);
        return employeeContactMapper.toDto(employeeContact);
    }

    @Override
    public Optional<EmployeeContactDTO> partialUpdate(EmployeeContactDTO employeeContactDTO) {
        log.debug("Request to partially update EmployeeContact : {}", employeeContactDTO);

        return employeeContactRepository
            .findById(employeeContactDTO.getId())
            .map(existingEmployeeContact -> {
                employeeContactMapper.partialUpdate(existingEmployeeContact, employeeContactDTO);

                return existingEmployeeContact;
            })
            .map(employeeContactRepository::save)
            .map(employeeContactMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeContactDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EmployeeContacts");
        return employeeContactRepository.findAll(pageable).map(employeeContactMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmployeeContactDTO> findOne(Long id) {
        log.debug("Request to get EmployeeContact : {}", id);
        return employeeContactRepository.findById(id).map(employeeContactMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EmployeeContact : {}", id);
        employeeContactRepository.deleteById(id);
    }
}
