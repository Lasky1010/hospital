package com.test.hospital.service.impl;

import com.test.hospital.data.dto.CompanyUserDto;
import com.test.hospital.data.mapper.CompanyUserMapper;
import com.test.hospital.data.repository.CompanyUserRepository;
import com.test.hospital.excpetionhandling.exception.UserNotFoundException;
import com.test.hospital.service.CompanyUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.test.hospital.excpetionhandling.ErrorStatus.USER_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class CompanyUserServiceImpl implements CompanyUserService {

    private final CompanyUserRepository companyUserRepository;
    private final CompanyUserMapper companyUserMapper;

    @Override
    public Page<CompanyUserDto> getPageable(Pageable pageable) {
        var paged = companyUserRepository.findAll(pageable);
        return companyUserMapper.toDtoPage(paged);
    }

    @Override
    public CompanyUserDto getById(Long id) {
        var companyUser = companyUserRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
        return companyUserMapper.toDto(companyUser);
    }


    @Override
    public CompanyUserDto createUser(CompanyUserDto companyUser) {
        var entity = companyUserMapper.toEntity(companyUser);
        var savedUser = companyUserRepository.save(entity);
        return companyUserMapper.toDto(savedUser);
    }


    @Override
    public void deleteUser(Long id) {
        var companyUser = companyUserRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
        companyUserRepository.delete(companyUser);
    }

}
