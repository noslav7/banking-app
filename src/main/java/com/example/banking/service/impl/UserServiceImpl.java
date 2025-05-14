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
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;

    private static final String CACHE_USERS = "users";

    private static final String CACHE_USER_SEARCH = "userSearch";

    public UserServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    @Cacheable(value = CACHE_USERS,
            key = "'findAll_'+#pageable.pageNumber+'_'+#pageable.pageSize")
    public Page<UserDto> findAll(Pageable pageable) {
        return userRepo.findAll(pageable)
                .map(UserDto::fromEntity);
    }

    @Override
    @Cacheable(value = CACHE_USERS,
            key = "'findByEmail_'+#email")
    public Optional<UserDto> findByEmail(String email) {
        return userRepo.findByEmail(email)
                .map(UserDto::fromEntity);
    }

    @Override
    @Cacheable(value = CACHE_USERS,
            key = "'findByPhone_'+#phone")
    public Optional<UserDto> findByPhone(String phone) {
        return userRepo.findByPhone(phone)
                .map(UserDto::fromEntity);
    }

    @Override
    @Cacheable(value = CACHE_USER_SEARCH,
            key = "'byName_'+#prefix+'_'+#pageable.pageNumber+'_'+#pageable.pageSize")
    public Page<UserDto> findByName(String prefix, Pageable pageable) {
        return userRepo.findByNameStartingWith(prefix, pageable)
                .map(UserDto::fromEntity);
    }

    @Override
    @Cacheable(value = CACHE_USER_SEARCH,
            key = "'byDob_'+#date.toString()+'_'+#pageable.pageNumber+'_'+#pageable.pageSize")
    public Page<UserDto> findByDateOfBirthAfter(LocalDate date, Pageable pageable) {
        return userRepo.findByDateOfBirthAfter(date, pageable)
                .map(UserDto::fromEntity);
    }

    @Override
    @Cacheable(value = CACHE_USERS,
            key = "'getById_'+#id")
    public UserDto getById(Long id) {
        return userRepo.findById(id)
                .map(UserDto::fromEntity)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Override
    @Transactional
    @CacheEvict(value = {CACHE_USERS, CACHE_USER_SEARCH}, allEntries = true)
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
    @CacheEvict(value = {CACHE_USERS, CACHE_USER_SEARCH}, allEntries = true)
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

