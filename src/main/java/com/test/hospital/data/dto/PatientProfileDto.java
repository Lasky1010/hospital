package com.test.hospital.data.dto;

import java.util.List;


public record PatientProfileDto(Long id, String firstName, String lastName,
                                List<String> oldClientGuidsFromLegacy,
                                PatientStatus statusId) {
}