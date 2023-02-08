package com.hanghae.finalProject.rest.follow.repository;

import com.hanghae.finalProject.rest.follow.model.Follow;
import com.hanghae.finalProject.rest.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

     // 내가 팔로우한 사람들 리스트 뽑기용
     @Query(value = "select f from Follow f join fetch f.following where f.user= :user")
     List<Follow> findByUser(@Param("user") User user);
     Optional<Follow> findByUserAndFollowing(User user, User following);

     @Query(value = "select f from Follow f join fetch f.user where f.following= :follow")
     List<Follow> findByFollow(@Param("follow") User follow);
}
