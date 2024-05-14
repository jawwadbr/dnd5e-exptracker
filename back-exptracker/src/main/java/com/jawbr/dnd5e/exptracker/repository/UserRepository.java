package com.jawbr.dnd5e.exptracker.repository;

import com.jawbr.dnd5e.exptracker.entity.User;
import com.jawbr.dnd5e.exptracker.util.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.email = :email")
    User findByEmail(String email);

    User findByUuid(UUID userId);

    Page<User> findByRole(UserRole role, Pageable pageable);

    Page<User> findByUsernameContainingAndRole(String keyword, Pageable pageable, UserRole role);
}
