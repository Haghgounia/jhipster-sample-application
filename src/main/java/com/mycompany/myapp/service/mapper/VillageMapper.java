package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.RuralDistrict;
import com.mycompany.myapp.domain.Village;
import com.mycompany.myapp.service.dto.RuralDistrictDTO;
import com.mycompany.myapp.service.dto.VillageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Village} and its DTO {@link VillageDTO}.
 */
@Mapper(componentModel = "spring")
public interface VillageMapper extends EntityMapper<VillageDTO, Village> {
    @Mapping(target = "ruralDistrict", source = "ruralDistrict", qualifiedByName = "ruralDistrictId")
    VillageDTO toDto(Village s);

    @Named("ruralDistrictId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RuralDistrictDTO toDtoRuralDistrictId(RuralDistrict ruralDistrict);
}
