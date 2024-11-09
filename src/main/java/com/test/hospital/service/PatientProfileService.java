package com.test.hospital.service;

import com.test.hospital.data.dto.PatientProfileDto;
import com.test.hospital.data.dto.UpdatePatientProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PatientProfileService {

    Page<PatientProfileDto> getAllPageable(Pageable pageable);

    PatientProfileDto getById(Long id);

    List<PatientProfileDto> getAllByGuids(List<String> ids);

    PatientProfileDto create(PatientProfileDto patientProfile);

    void delete(Long id);

    PatientProfileDto updatePatient(Long id, UpdatePatientProfile updPatientProfile);
}
