package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Continent;
import com.mycompany.myapp.service.dto.ContinentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Continent} and its DTO {@link ContinentDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContinentMapper extends EntityMapper<ContinentDTO, Continent> {}
