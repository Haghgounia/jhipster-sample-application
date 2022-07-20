package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.District;
import com.mycompany.myapp.domain.RuralDistrict;
import com.mycompany.myapp.service.dto.DistrictDTO;
import com.mycompany.myapp.service.dto.RuralDistrictDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RuralDistrict} and its DTO {@link RuralDistrictDTO}.
 */
@Mapper(componentModel = "spring")
public interface RuralDistrictMapper extends EntityMapper<RuralDistrictDTO, RuralDistrict> {
    @Mapping(target = "district", source = "district", qualifiedByName = "districtId")
    RuralDistrictDTO toDto(RuralDistrict s);

    @Named("districtId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DistrictDTO toDtoDistrictId(District district);
}
