package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Candidate;
import com.mycompany.myapp.service.dto.CandidateDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Candidate} and its DTO {@link CandidateDTO}.
 */
@Mapper(componentModel = "spring")
public interface CandidateMapper extends EntityMapper<CandidateDTO, Candidate> {}
