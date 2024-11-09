package com.test.hospital.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public enum PatientStatus {
    ACTIVE("Active", List.of(200,210,230)),
    INACTIVE("Inactive", List.of(0));

    private final String value;
    private final List<Integer> codes;
}
