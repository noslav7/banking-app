package com.example.banking.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 500)
    private String name;

    @NotNull
    private LocalDate dateOfBirth;

    @NotBlank
    @Size(min = 8, max = 500)
    private String password;

    @OneToOne(mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY,
            optional = false)
    private Account account;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmailData> emails = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PhoneData> phones = new ArrayList<>();

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
        account.setUser(this);
    }

    public List<EmailData> getEmails() {
        return emails;
    }

    public List<PhoneData> getPhones() {
        return phones;
    }

    public void addEmail(EmailData e) {
        emails.add(e);
        e.setUser(this);
    }

    public void removeEmail(EmailData e) {
        emails.remove(e);
        e.setUser(null);
    }

    public void addPhone(PhoneData p) {
        phones.add(p);
        p.setUser(this);
    }

    public void removePhone(PhoneData p) {
        phones.remove(p);
        p.setUser(null);
    }
}

