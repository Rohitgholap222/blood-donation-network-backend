package com.bloodnetwork.repository;

import com.bloodnetwork.entity.Role;
import com.bloodnetwork.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailIgnoreCase(String email);
    long countByRole(Role role);
}
