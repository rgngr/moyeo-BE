package com.hanghae.finalProject.rest.user.repository;

import com.hanghae.finalProject.rest.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserCustomRepository {
     Boolean existsByUsername(String username);
     Boolean existsByEmail(String email);

     Optional<User> findByUsername(String username);



     Optional<User> findByKakaoIdIsNullAndEmail(String email);
     
     Optional<User> findByKakaoId(Long kakaoId);


//    List<User> findAllById(Long id);
}
