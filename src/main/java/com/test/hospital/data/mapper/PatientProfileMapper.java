package com.test.hospital.data.mapper;

import com.test.hospital.data.dto.PatientProfileDto;
import com.test.hospital.data.entity.PatientProfile;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PatientProfileMapper {

    PatientProfile toEntity(PatientProfileDto patientProfileDto);

    PatientProfileDto toDto(PatientProfile patientProfile);

    default Page<PatientProfileDto> toDtoPage(Page<PatientProfile> paged) {
        return paged.map(this::toDto);
    }

    List<PatientProfileDto> toDtoList(List<PatientProfile> patientProfiles);

}