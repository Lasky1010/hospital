package com.test.hospital.data.dto.legacy;

import java.time.LocalDateTime;

public record NoteLegacy(
        String guid,
        String comments,
        LocalDateTime modifiedDateTime,
        LocalDateTime datetime,
        String loggedUser,
        LocalDateTime createdDateTime,
        String clientGuid) {
}