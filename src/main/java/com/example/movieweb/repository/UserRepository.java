package com.example.movieweb.repository;

import com.example.movieweb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findBySocialIdAndProvider(String socialId, String provider);
}