package com.test.hospital.data.dto.legacy;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ClientLegacy(String agency, String guid,
                           String firstName, String lastName,
                           String status, LocalDate dob,
                           LocalDateTime createdDateTime) {
}
