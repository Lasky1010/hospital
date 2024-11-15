package com.test.hospital.data.repository;

import com.test.hospital.data.entity.PatientProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientProfileRepository extends JpaRepository<PatientProfile, Long> {

    List<PatientProfile> findAllByOldClientGuidsFromLegacyIn(List<String> guids);
}