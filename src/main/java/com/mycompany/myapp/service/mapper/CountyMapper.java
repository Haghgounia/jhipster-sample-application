package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.County;
import com.mycompany.myapp.domain.Province;
import com.mycompany.myapp.service.dto.CountyDTO;
import com.mycompany.myapp.service.dto.ProvinceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link County} and its DTO {@link CountyDTO}.
 */
@Mapper(componentModel = "spring")
public interface CountyMapper extends EntityMapper<CountyDTO, County> {
    @Mapping(target = "province", source = "province", qualifiedByName = "provinceId")
    CountyDTO toDto(County s);

    @Named("provinceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProvinceDTO toDtoProvinceId(Province province);
}
