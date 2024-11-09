package com.test.hospital.service.impl;

import com.test.hospital.data.dto.PatientNoteDto;
import com.test.hospital.data.dto.UpdatePatientNote;
import com.test.hospital.data.mapper.CompanyUserMapper;
import com.test.hospital.data.mapper.PatientNoteMapper;
import com.test.hospital.data.repository.PatientNoteRepository;
import com.test.hospital.excpetionhandling.exception.PatientNoteNotFoundException;
import com.test.hospital.service.CompanyUserService;
import com.test.hospital.service.PatientNoteService;
import com.test.hospital.service.PatientProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.test.hospital.excpetionhandling.ErrorStatus.NOTE_NOT_FOUND;
import static java.util.Objects.nonNull;

@RequiredArgsConstructor
@Service
public class PatientNoteServiceImpl implements PatientNoteService {

    private final PatientNoteRepository patientNoteRepository;
    private final PatientNoteMapper patientNoteMapper;
    private final PatientProfileService patientProfileService;
    private final CompanyUserService companyUserService;
    private final CompanyUserMapper companyUserMapper;

    @Override
    public Page<PatientNoteDto> getAllPageable(Pageable pageable) {
        var paged = patientNoteRepository.findAll(pageable);
        return patientNoteMapper.toDtoPage(paged);
    }

    @Override
    public PatientNoteDto getById(Long id) {
        var patientNote = patientNoteRepository.findById(id)
                .orElseThrow(() -> new PatientNoteNotFoundException(NOTE_NOT_FOUND));
        return patientNoteMapper.toDto(patientNote);
    }

    @Override
    public List<PatientNoteDto> getAllByIds(List<Long> ids) {
        var allById = patientNoteRepository.findAllById(ids);
        if (allById.isEmpty()) {
            throw new PatientNoteNotFoundException(NOTE_NOT_FOUND);
        }
        return patientNoteMapper.toDtoList(allById);
    }

    @Override
    public PatientNoteDto create(PatientNoteDto patientNoteDto) {
        var userId = patientNoteDto.createdById();
        var patientId = patientNoteDto.patientProfileId();

        companyUserService.getById(userId);
        patientProfileService.getById(patientId);

        var entity = patientNoteMapper.toEntity(patientNoteDto);
        entity.setCreatedDateTime(LocalDateTime.now());
        var saved = patientNoteRepository.save(entity);
        return patientNoteMapper.toDto(saved);
    }

    @Override
    public void delete(Long id) {
        var patientNote = patientNoteRepository.findById(id)
                .orElseThrow(() -> new PatientNoteNotFoundException(NOTE_NOT_FOUND));
        patientNoteRepository.delete(patientNote);
    }

    @Override
    public PatientNoteDto updateNote(UpdatePatientNote patientNoteDto) {
        var patientNote = patientNoteRepository.findById(patientNoteDto.noteId())
                .orElseThrow(() -> new PatientNoteNotFoundException(NOTE_NOT_FOUND));
        var user = companyUserMapper.toEntity(companyUserService.getById(patientNoteDto.userId()));
        if (nonNull(patientNoteDto.note())) {
            patientNote.setNote(patientNoteDto.note());
            patientNote.setLastModifiedBy(user);
            patientNote.setLastModifiedDateTime(LocalDateTime.now());
        }
        var upd = patientNoteRepository.save(patientNote);
        return patientNoteMapper.toDto(upd);
    }
}
