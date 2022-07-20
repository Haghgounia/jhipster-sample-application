package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.EmployeeContact;
import com.mycompany.myapp.service.dto.EmployeeContactDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EmployeeContact} and its DTO {@link EmployeeContactDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmployeeContactMapper extends EntityMapper<EmployeeContactDTO, EmployeeContact> {}
