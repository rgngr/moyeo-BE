package com.hanghae.finalProject.rest.user.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.function.Supplier;

@Entity (name = "users")
@RequiredArgsConstructor
@Getter
@Table(indexes = @Index(name = "idx__username", columnList = "username"))
public class User implements Supplier<User> {
     //- username은  `최소 4자 이상, 10자 이하이며 알파벳 소문자(a~z), 숫자(0~9)`로 구성되어야 한다.
     //- password는  `최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9)`로 구성되어야 한다.
     //- DB에 중복된 username이 없다면 회원을 저장하고 Client 로 성공했다는 메시지, 상태코드 반환하기
     @Id
     @GeneratedValue (strategy = GenerationType.IDENTITY)
     private Long id;
     
     @Column (nullable = false, unique = true,columnDefinition = "VARCHAR(10)")
     private String username;
     
     @Column(nullable = false)
     private String password;
     
     @Column(nullable = false)
     private String email; // 카카오 메일주소와 중복가능(다른유저로봄). unique False
     
     @Column
     private String profileUrl;
     
     @Column
     private Boolean deleted = false;
     
     @Column(unique = true)
     private Long kakaoId;

     public User(String username, String password, String email) {
          this.username = username;
          this.password = password;
          this.email = email;
     }


     // 추가 필요
     public User(String kakaoNickname, Long kakaoId, String password, String email, String profileUrl){
          this.username = kakaoNickname;
          this.password = password;
          this.email = email;
          this.profileUrl = profileUrl;
          this.kakaoId = kakaoId;
     
     }
     
     public User kakaoIdUpdate(Long kakaoId) {
          this.kakaoId = kakaoId;
          return this;
     }
     @Override
     public User get() {
          return null;
     }

     public void updateProfile(String profileUrl) {
          this.profileUrl = profileUrl;
     }
}
