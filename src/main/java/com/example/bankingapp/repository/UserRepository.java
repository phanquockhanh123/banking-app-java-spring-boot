package com.example.bankingapp.repository;

import com.example.bankingapp.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Boolean existsByEmail(String email);

    Boolean existsByAccountNumber(String accountNumber);

    UserEntity findByAccountNumber(String accountNumber);
}
