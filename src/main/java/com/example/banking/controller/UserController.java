package com.example.banking.controller;

import com.example.banking.dto.UserDto;
import com.example.banking.service.UserService;
import com.example.banking.util.JwtUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    private final UserService userService;

    private final JwtUtil jwtUtil;

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public Page<UserDto> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate dateOfBirth,
            Pageable pageable
    ) {
        if (name != null) {
            return userService.findByName(name, pageable);
        }
        if (dateOfBirth != null) {
            return userService.findByDateOfBirthAfter(dateOfBirth, pageable);
        }
        return userService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public UserDto get(@PathVariable Long id) {
        return userService.getById(id);
    }

    @PatchMapping("/{id}/emails")
    public UserDto updateEmails(
            @PathVariable Long id,
            @RequestBody
            @Valid
            @NotEmpty(message = "Список email не может быть пустым")
            List<@Email(message = "Неверный формат email") String> emails,
            @RequestHeader("Authorization") String authHeader
    ) {
        Long me = jwtUtil.extractUserId(authHeader);
        if (!me.equals(id)) throw new RuntimeException("Forbidden");
        UserDto dto = new UserDto();
        dto.setEmails(emails);
        return userService.updateEmails(id, dto);
    }

    @PatchMapping("/{id}/phones")
    public UserDto updatePhones(
            @PathVariable Long id,
            @RequestBody
            @Valid
            @NotEmpty(message = "Список телефонов не может быть пустым")
            List<@Pattern(
                    regexp = "^\\d{11}$",
                    message = "Телефон должен состоять из 11 цифр, например 79207865432"
            ) String> phones,
            @RequestHeader("Authorization") String authHeader
    ) {
        Long me = jwtUtil.extractUserId(authHeader);
        if (!me.equals(id)) throw new RuntimeException("Forbidden");
        UserDto dto = new UserDto();
        dto.setPhones(phones);
        return userService.updatePhones(id, dto);
    }
}

