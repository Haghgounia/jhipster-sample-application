package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Language;
import com.mycompany.myapp.service.dto.LanguageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Language} and its DTO {@link LanguageDTO}.
 */
@Mapper(componentModel = "spring")
public interface LanguageMapper extends EntityMapper<LanguageDTO, Language> {}
