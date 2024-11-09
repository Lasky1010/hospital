package com.test.hospital.controller;

import com.test.hospital.data.dto.PatientNoteDto;
import com.test.hospital.data.dto.UpdatePatientNote;
import com.test.hospital.service.PatientNoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patient-notes")
@RequiredArgsConstructor
public class PatientNoteController {

    private final PatientNoteService patientNoteService;

    @Operation(summary = "Get paged patient notes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient notes received")
    })
    @GetMapping
    public ResponseEntity<PagedModel<PatientNoteDto>> getPageable(@ParameterObject Pageable pageable) {
        Page<PatientNoteDto> patientNotes = patientNoteService.getAllPageable(pageable);
        return ResponseEntity.ok(new PagedModel<>(patientNotes));
    }

    @Operation(summary = "Get patient notes by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient notes received"),
            @ApiResponse(responseCode = "404", description = "Patient notes not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PatientNoteDto> getById(@PathVariable Long id) {
        var result = patientNoteService.getById(id);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Get patient notes by IDs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient notes received"),
            @ApiResponse(responseCode = "404", description = "Patient notes not found")
    })
    @GetMapping("/by-ids")
    public ResponseEntity<List<PatientNoteDto>> getAllByIds(@RequestParam List<Long> ids) {
        var result = patientNoteService.getAllByIds(ids);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Create patient note")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient note created"),
    })
    @PostMapping
    public ResponseEntity<PatientNoteDto> create(@RequestBody PatientNoteDto patientNoteDto) {
        var saved = patientNoteService.create(patientNoteDto);
        return ResponseEntity.ok(saved);
    }

    @PatchMapping
    public ResponseEntity<PatientNoteDto> update(@RequestBody UpdatePatientNote updPatientNoteDto) {
        var upd = patientNoteService.updateNote(updPatientNoteDto);
        return ResponseEntity.ok(upd);
    }

    @Operation(summary = "Delete patient note")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Patient note deleted"),
            @ApiResponse(responseCode = "404", description = "Patient notes not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        patientNoteService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
