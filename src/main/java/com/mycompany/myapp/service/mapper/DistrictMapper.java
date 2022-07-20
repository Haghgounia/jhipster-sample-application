package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.County;
import com.mycompany.myapp.domain.District;
import com.mycompany.myapp.service.dto.CountyDTO;
import com.mycompany.myapp.service.dto.DistrictDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link District} and its DTO {@link DistrictDTO}.
 */
@Mapper(componentModel = "spring")
public interface DistrictMapper extends EntityMapper<DistrictDTO, District> {
    @Mapping(target = "county", source = "county", qualifiedByName = "countyId")
    DistrictDTO toDto(District s);

    @Named("countyId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CountyDTO toDtoCountyId(County county);
}
