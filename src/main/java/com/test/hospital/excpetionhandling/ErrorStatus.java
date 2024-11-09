package com.test.hospital.excpetionhandling;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorStatus {
    USER_NOT_FOUND(404, "Person with given id not found"),
    PATIENT_NOT_FOUND(404, "Patient with given id not found"),
    NOTE_NOT_FOUND(404, "Note with given id not found"),
    WRONG_USER(400, "Wrong user");

    private final int statusCode;
    private final String message;
}
