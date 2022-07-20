package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.AssetCategory;
import com.mycompany.myapp.service.dto.AssetCategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AssetCategory} and its DTO {@link AssetCategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface AssetCategoryMapper extends EntityMapper<AssetCategoryDTO, AssetCategory> {}
