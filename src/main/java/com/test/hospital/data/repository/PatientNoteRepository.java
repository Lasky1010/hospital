package com.test.hospital.data.repository;

import com.test.hospital.data.entity.PatientNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientNoteRepository extends JpaRepository<PatientNote, Long> {

    List<PatientNote> findAllByPatientProfile_Id(Long id);
}