package com.backend.proprental.repository;

import com.backend.proprental.entity.User;
import com.backend.proprental.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmailIgnoreCase(String email);

    Optional<User> findByRole(Role role);

    boolean existsByEmailIgnoreCase(String email);
}
