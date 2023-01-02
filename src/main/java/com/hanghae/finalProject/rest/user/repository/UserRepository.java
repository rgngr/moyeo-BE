package com.hanghae.finalProject.rest.user.repository;

import com.hanghae.finalProject.rest.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
     Boolean existsByUsername(String username);
     Boolean existsByEmail(String email);

     Optional<User> findByKakaoIdIsNullAndEmail(String email);
}
