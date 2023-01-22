package com.hanghae.finalProject.rest.follow.repository;

import com.hanghae.finalProject.rest.follow.model.Follow;
import com.hanghae.finalProject.rest.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

     @Query(value = "select f from Follow f join fetch f.user where f.user= :user")
     List<Follow> findByUser(User user);
     Optional<Follow> findByUserAndFollowId(User user, Long followId);

     @Query(value = "select f from Follow f join fetch f.user where f.followId= :followId")
     List<Follow> findByFollowId(Long followId);
}
