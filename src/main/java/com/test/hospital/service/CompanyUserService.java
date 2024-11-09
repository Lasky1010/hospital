package com.test.hospital.service;

import com.test.hospital.data.dto.CompanyUserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompanyUserService {

    Page<CompanyUserDto> getPageable(Pageable pageable);

    CompanyUserDto getById(Long id);

    CompanyUserDto createUser(CompanyUserDto companyUser);

    void deleteUser(Long id);

}
