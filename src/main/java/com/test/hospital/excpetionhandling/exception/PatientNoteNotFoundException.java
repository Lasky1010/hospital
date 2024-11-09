package com.test.hospital.excpetionhandling.exception;

import com.test.hospital.excpetionhandling.ErrorStatus;
import lombok.Getter;

@Getter
public class PatientNoteNotFoundException extends RuntimeException {

    private final int status;

    public PatientNoteNotFoundException(ErrorStatus errorStatus) {
        super(errorStatus.getMessage());
        this.status = errorStatus.getStatusCode();
    }
}
