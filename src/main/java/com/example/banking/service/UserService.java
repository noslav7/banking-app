package com.example.banking.service;

import com.example.banking.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface UserService {
    Page<UserDto> findByName(String prefix, Pageable pageable);
    Page<UserDto> findByDateOfBirthAfter(LocalDate date, Pageable pageable);
    UserDto getById(Long id);
    UserDto updateEmails(Long userId, UserDto dto);
    UserDto updatePhones(Long userId, UserDto dto);
}

