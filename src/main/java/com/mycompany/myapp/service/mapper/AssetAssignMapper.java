package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.AssetAssign;
import com.mycompany.myapp.service.dto.AssetAssignDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AssetAssign} and its DTO {@link AssetAssignDTO}.
 */
@Mapper(componentModel = "spring")
public interface AssetAssignMapper extends EntityMapper<AssetAssignDTO, AssetAssign> {}
