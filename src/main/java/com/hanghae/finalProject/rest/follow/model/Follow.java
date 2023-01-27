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
     @ManyToOne (fetch = FetchType.LAZY)
     @JoinColumn(name ="following_id")
     private User following;
     
     public Follow(User user, User following) {
          this.user = user;
          this.following = following;
     }
     
     @Override
     public Follow get() {
          return null;
     }
}
