package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Asset;
import com.mycompany.myapp.service.dto.AssetDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Asset} and its DTO {@link AssetDTO}.
 */
@Mapper(componentModel = "spring")
public interface AssetMapper extends EntityMapper<AssetDTO, Asset> {}
