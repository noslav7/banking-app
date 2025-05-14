package com.example.banking.service;

import com.example.banking.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;

public interface UserService {

    Page<UserDto> findAll(Pageable pageable);

    Page<UserDto> findByName(String prefix, Pageable pageable);

    Optional<UserDto> findByEmail(String email);

    Optional<UserDto> findByPhone(String phone);

    Page<UserDto> findByDateOfBirthAfter(LocalDate date, Pageable pageable);

    UserDto getById(Long id);

    UserDto updateEmails(Long userId, UserDto dto);

    UserDto updatePhones(Long userId, UserDto dto);
}

