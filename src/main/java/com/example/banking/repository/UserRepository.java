package com.example.banking.repository;

import com.example.banking.entity.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Override
    @CacheEvict(value = "users", allEntries = true)
    <S extends User> S save(S entity);

    @Override
    @CacheEvict(value = "users", allEntries = true)
    void deleteById(Long id);

    Optional<User> findByName(String name);

    @Cacheable(value = "users",
            key = "'findByDateOfBirthAfter_'+#date.toString()+'_'+#pageable.pageNumber+'_'+#pageable.pageSize")
    Page<User> findByDateOfBirthAfter(LocalDate date, Pageable pageable);

    @Cacheable(value = "users",
            key = "'findByNameStartingWith_'+#prefix+'_'+#pageable.pageNumber+'_'+#pageable.pageSize")
    Page<User> findByNameStartingWith(String prefix, Pageable pageable);

    @Query("SELECT u FROM User u JOIN u.emails e WHERE e.email = :email")
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u JOIN u.phones p WHERE p.phone = :phone")
    Optional<User> findByPhone(String phone);
}
