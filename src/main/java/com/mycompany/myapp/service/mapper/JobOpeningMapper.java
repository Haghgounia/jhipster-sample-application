package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.JobOpening;
import com.mycompany.myapp.service.dto.JobOpeningDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link JobOpening} and its DTO {@link JobOpeningDTO}.
 */
@Mapper(componentModel = "spring")
public interface JobOpeningMapper extends EntityMapper<JobOpeningDTO, JobOpening> {}
