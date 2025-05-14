package com.example.banking.dto;

import com.example.banking.entity.EmailData;
import com.example.banking.entity.PhoneData;
import com.example.banking.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class UserDto {

    private Long id;

    private String name;

    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate dateOfBirth;

    private List<String> emails;

    private List<String> phones;

    public UserDto() {
    }

    public UserDto(Long id, String name, LocalDate dateOfBirth,
                   List<String> emails, List<String> phones) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.emails = emails;
        this.phones = phones;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public List<String> getPhones() {
        return phones;
    }

    public void setPhones(List<String> phones) {
        this.phones = phones;
    }

    public static UserDto fromEntity(User user) {
        List<String> emailList = user.getEmails().stream()
                .map(EmailData::getEmail)
                .collect(Collectors.toList());

        List<String> phoneList = user.getPhones().stream()
                .map(PhoneData::getPhone)
                .collect(Collectors.toList());

        return new UserDto(
                user.getId(),
                user.getName(),
                user.getDateOfBirth(),
                emailList,
                phoneList
        );
    }
}

