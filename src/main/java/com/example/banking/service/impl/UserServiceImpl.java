package com.example.banking.service.impl;

import com.example.banking.dto.UserDto;
import com.example.banking.entity.EmailData;
import com.example.banking.entity.PhoneData;
import com.example.banking.entity.User;
import com.example.banking.repository.UserRepository;
import com.example.banking.service.UserService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;

    public UserServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    @Cacheable(value = "userSearch",
            key = "'byName_'+#prefix+'_'+#pageable.pageNumber+'_'+#pageable.pageSize")
    public Page<UserDto> findByName(String prefix, Pageable pageable) {
        return userRepo.findByNameStartingWith(prefix, pageable)
                .map(UserDto::fromEntity);
    }

    @Override
    @Cacheable(value = "userSearch",
            key = "'byDob_'+#date.toString()+'_'+#pageable.pageNumber+'_'+#pageable.pageSize")
    public Page<UserDto> findByDateOfBirthAfter(LocalDate date, Pageable pageable) {
        return userRepo.findByDateOfBirthAfter(date, pageable)
                .map(UserDto::fromEntity);
    }

    @Override
    public UserDto getById(Long id) {
        return userRepo.findById(id)
                .map(UserDto::fromEntity)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Override
    @Transactional
    @CacheEvict(value = {"users", "userSearch"}, allEntries = true)
    public UserDto updateEmails(Long userId, UserDto dto) {
        final User userEntity = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        userEntity.getEmails().clear();

        dto.getEmails().forEach(email -> {
            EmailData e = new EmailData();
            e.setEmail(email);
            userEntity.addEmail(e);
        });

        User saved = userRepo.save(userEntity);

        return UserDto.fromEntity(saved);
    }


    @Override
    @Transactional
    @CacheEvict(value = {"users", "userSearch"}, allEntries = true)
    public UserDto updatePhones(Long userId, UserDto dto) {
        final User userEntity = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        userEntity.getPhones().clear();

        dto.getPhones().forEach(phone -> {
            PhoneData p = new PhoneData();
            p.setPhone(phone);
            userEntity.addPhone(p);
        });

        User saved = userRepo.save(userEntity);
        return UserDto.fromEntity(saved);
    }
}

