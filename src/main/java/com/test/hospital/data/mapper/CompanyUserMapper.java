package com.test.hospital.data.mapper;

import com.test.hospital.data.entity.CompanyUser;
import com.test.hospital.data.dto.CompanyUserDto;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CompanyUserMapper {

    CompanyUser toEntity(CompanyUserDto companyUserDto);

    CompanyUserDto toDto(CompanyUser companyUser);

    default Page<CompanyUserDto> toDtoPage(Page<CompanyUser> paged){
        return paged.map(this::toDto);
    }
}