package com.hanghae.finalProject.rest.follow.model;

import com.hanghae.finalProject.rest.user.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.function.Supplier;

@Getter
@Entity
@NoArgsConstructor
public class Follow implements Supplier<Follow> {
     @Id
     @GeneratedValue (strategy = GenerationType.IDENTITY)
     private Long id;
     
     @ManyToOne (fetch = FetchType.LAZY)
     @JoinColumn(name ="user_id")
     private User user; // 모임생성자
     
     //팔로우아이디
     @Column(nullable = false)
     private Long followId;
     public Follow(User user, Long followId) {
          this.user = user;
          this.followId = followId;
     }
     
     @Override
     public Follow get() {
          return null;
     }
}
