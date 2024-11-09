package com.test.hospital.data.dto;

import java.time.LocalDateTime;


public record PatientNoteDto(Long id, LocalDateTime lastModifiedDateTime,
                             LocalDateTime createdDateTime, String note,
                             String oldNoteGuid, Long createdById,
                             Long lastModifiedById, Long patientProfileId,
                             String patientProfileFirstName, String patientProfileLastName,
                             PatientStatus patientProfileStatusId) {
}