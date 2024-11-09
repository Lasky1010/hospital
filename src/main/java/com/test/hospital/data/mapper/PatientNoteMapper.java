package com.test.hospital.data.mapper;

import com.test.hospital.data.dto.PatientNoteDto;
import com.test.hospital.data.entity.PatientNote;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PatientNoteMapper {

    PatientNote toEntity(PatientNoteDto patientNoteDto);

    PatientNoteDto toDto(PatientNote patientNote);

    default Page<PatientNoteDto> toDtoPage(Page<PatientNote> paged) {
        return paged.map(this::toDto);
    }

    List<PatientNoteDto> toDtoList(List<PatientNote> patientNoteList);

}