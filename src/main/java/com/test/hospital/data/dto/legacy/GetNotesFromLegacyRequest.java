package com.test.hospital.data.dto.legacy;

import java.time.LocalDate;

public record GetNotesFromLegacyRequest(String agency, LocalDate dateFrom, LocalDate dateTo, String clientGuid) {
}
