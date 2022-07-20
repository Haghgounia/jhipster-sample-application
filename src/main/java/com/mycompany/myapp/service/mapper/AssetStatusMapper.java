package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.AssetStatus;
import com.mycompany.myapp.service.dto.AssetStatusDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AssetStatus} and its DTO {@link AssetStatusDTO}.
 */
@Mapper(componentModel = "spring")
public interface AssetStatusMapper extends EntityMapper<AssetStatusDTO, AssetStatus> {}
