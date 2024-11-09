package com.test.hospital.excpetionhandling.exception;

import com.test.hospital.excpetionhandling.ErrorStatus;
import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException {

    private final int status;

    public UserNotFoundException(ErrorStatus errorStatus) {
        super(errorStatus.getMessage());
        this.status = errorStatus.getStatusCode();
    }
}
