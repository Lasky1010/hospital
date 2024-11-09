package com.test.hospital.service;

import com.test.hospital.data.dto.PatientNoteDto;
import com.test.hospital.data.dto.UpdatePatientNote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PatientNoteService {

    Page<PatientNoteDto> getAllPageable(Pageable pageable);

    PatientNoteDto getById(Long id);

    List<PatientNoteDto> getAllByIds(List<Long> ids);

    PatientNoteDto create(PatientNoteDto patientNote);

    void delete(Long id);

    PatientNoteDto updateNote(UpdatePatientNote patientNoteDto);
}
