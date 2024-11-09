package com.test.hospital.service.impl;

import com.test.hospital.data.dto.PatientProfileDto;
import com.test.hospital.data.dto.UpdatePatientProfile;
import com.test.hospital.data.entity.PatientProfile;
import com.test.hospital.data.mapper.PatientProfileMapper;
import com.test.hospital.data.repository.PatientProfileRepository;
import com.test.hospital.excpetionhandling.exception.PatientNotFoundException;
import com.test.hospital.service.PatientProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.test.hospital.excpetionhandling.ErrorStatus.PATIENT_NOT_FOUND;
import static java.util.Objects.nonNull;

@RequiredArgsConstructor
@Service
public class PatientProfileServiceImpl implements PatientProfileService {

    private final PatientProfileRepository patientProfileRepository;
    private final PatientProfileMapper patientProfileMapper;

    @Override
    public Page<PatientProfileDto> getList(Pageable pageable) {
        var paged = patientProfileRepository.findAll(pageable);
        return patientProfileMapper.toDtoPage(paged);
    }

    @Override
    public PatientProfileDto getById(Long id) {
        var patientProfile = patientProfileRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException(PATIENT_NOT_FOUND));
        return patientProfileMapper.toDto(patientProfile);
    }

    @Override
    public List<PatientProfileDto> getManyByGuid(List<String> guids) {
        var allById = patientProfileRepository.findAllByOldClientGuidsFromLegacyIn(guids);
        if (allById.isEmpty()) {
            throw new PatientNotFoundException(PATIENT_NOT_FOUND);
        }
        return patientProfileMapper.toDtoList(allById);
    }

    @Override
    public PatientProfileDto create(PatientProfileDto patientProfileDto) {
        var entity = patientProfileMapper.toEntity(patientProfileDto);
        var saved = patientProfileRepository.save(entity);
        return patientProfileMapper.toDto(saved);
    }


    @Override
    public void delete(Long id) {
        var patientProfile = patientProfileRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException(PATIENT_NOT_FOUND));
        patientProfileRepository.delete(patientProfile);
    }

    @Override
    public PatientProfileDto updatePatient(Long id, UpdatePatientProfile updPatientProfile) {
        var patientProfile = patientProfileRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException(PATIENT_NOT_FOUND));

        updatePatientFields(updPatientProfile, patientProfile);
        var upd = patientProfileRepository.save(patientProfile);
        return patientProfileMapper.toDto(upd);
    }

    private void updatePatientFields(UpdatePatientProfile updPatientProfile, PatientProfile patientProfile) {
        if (nonNull(updPatientProfile.firstName()) && !updPatientProfile.firstName().equals(patientProfile.getFirstName())) {
            patientProfile.setFirstName(updPatientProfile.firstName());
        }
        if (nonNull(updPatientProfile.lastName()) && !updPatientProfile.lastName().equals(patientProfile.getLastName())) {
            patientProfile.setLastName(updPatientProfile.lastName());
        }
        if (nonNull(updPatientProfile.status()) && !updPatientProfile.status().equals(patientProfile.getStatusId())) {
            patientProfile.setStatusId(updPatientProfile.status());
        }
    }

}
