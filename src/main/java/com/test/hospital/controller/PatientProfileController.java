package com.test.hospital.controller;

import com.test.hospital.data.dto.PatientProfileDto;
import com.test.hospital.data.dto.UpdatePatientProfile;
import com.test.hospital.service.PatientProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patient-profiles")
@RequiredArgsConstructor
public class PatientProfileController {

    private final PatientProfileService patientProfileService;

    @Operation(summary = "Get paged patients")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users received")
    })
    @GetMapping
    public ResponseEntity<PagedModel<PatientProfileDto>> getPageable(@ParameterObject Pageable pageable) {
        var patientProfiles = patientProfileService.getList(pageable);
        return ResponseEntity.ok(new PagedModel<>(patientProfiles));
    }

    @Operation(summary = "Get patient by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient received"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PatientProfileDto> getById(@PathVariable Long id) {
        var result = patientProfileService.getById(id);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Create patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient created"),
    })
    @PostMapping
    public ResponseEntity<PatientProfileDto> create(@RequestBody PatientProfileDto patientProfileDto) {
        var saved = patientProfileService.create(patientProfileDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Operation(summary = "Update patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient updated"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<PatientProfileDto> update(@PathVariable Long id, @RequestBody UpdatePatientProfile updPatientProfile) {
        var updPatient = patientProfileService.updatePatient(id, updPatientProfile);
        return ResponseEntity.ok(updPatient);
    }

    @Operation(summary = "Delete patient by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient deleted"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        patientProfileService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
