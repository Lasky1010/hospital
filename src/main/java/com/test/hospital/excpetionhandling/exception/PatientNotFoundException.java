package com.test.hospital.excpetionhandling.exception;

import com.test.hospital.excpetionhandling.ErrorStatus;
import lombok.Getter;

@Getter
public class PatientNotFoundException extends RuntimeException {

    private final int status;

    public PatientNotFoundException(ErrorStatus errorStatus) {
        super(errorStatus.getMessage());
        this.status = errorStatus.getStatusCode();
    }
}
