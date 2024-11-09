package com.test.hospital.controller;

import com.test.hospital.data.dto.CompanyUserDto;
import com.test.hospital.service.CompanyUserService;
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
@RequestMapping("/company-users")
@RequiredArgsConstructor
public class CompanyUserController {

    private final CompanyUserService companyUserService;

    @Operation(summary = "Get paged users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users received")
    })
    @GetMapping
    public ResponseEntity<PagedModel<CompanyUserDto>> getPageable(@ParameterObject Pageable pageable) {
        var companyUsers = companyUserService.getPageable(pageable);
        return ResponseEntity.ok(new PagedModel<>(companyUsers));
    }

    @Operation(summary = "Get user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User received"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CompanyUserDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(companyUserService.getById(id));
    }

    @Operation(summary = "Create user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created"),
    })
    @PostMapping
    public ResponseEntity<CompanyUserDto> create(@RequestBody CompanyUserDto companyUserDto) {
        var result = companyUserService.createUser(companyUserDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @Operation(summary = "Delete user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        companyUserService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
