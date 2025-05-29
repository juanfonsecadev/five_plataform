package com.plataformamarcenaria.repository;

import com.plataformamarcenaria.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByUserType(User.UserType userType);
    boolean existsByEmail(String email);
    List<User> findByActiveTrue();
} 