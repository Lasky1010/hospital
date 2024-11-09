package com.test.hospital.data.repository;

import com.test.hospital.data.entity.PatientNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientNoteRepository extends JpaRepository<PatientNote, Long> {

    Optional<PatientNote> findByOldNoteGuid(String oldGuid);
}