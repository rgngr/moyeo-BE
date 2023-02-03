package com.hanghae.finalProject.rest.follow.controller;

import com.hanghae.finalProject.AcceptanceTest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FollowControllerTest extends AcceptanceTest {
     private static final String EMAIL = "jojtest123@nate.com";
     private static final String PASSWORD = "joung18@#$";
     private static final Long MEETINGID = 10259L;
     private static final Long FOLLOWID = 10L;
     
     @DisplayName("팔로우/언팔로우")
     @Test
     void followTest() {
          // 로그인 토큰구하기
          String accessToken = getToken();
          // Given
          // When
          ExtractableResponse<Response> response =
               RestAssured
                    .given().log().all()
                    .header("Authorization", accessToken)
                    .when()
                    .get("/api/follow/"+FOLLOWID)
                    .then().log().all()
                    .extract();
          // Then
          assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
//          assertThat(response.body().jsonPath().getString("statusMsg")).isEqualTo("유저 팔로우 성공");
     }
     
     @Test
     void followingListTest() {
     }
     
     @Test
     void followerListTest() {
     }
     
     private static String getToken() {
          // 로그인 토큰구하기
          Map<String, String> loginParams = new HashMap<>();
          loginParams.put("email", EMAIL);
          loginParams.put("password", PASSWORD);
          
          ExtractableResponse<Response> response1 = RestAssured.given().log().all()
               .contentType(MediaType.APPLICATION_JSON_VALUE)
               .body(loginParams)
               .when().post("/api/users/login")
               .then().extract();
          
          String accessToken = response1.header("Authorization");
          return accessToken;
     }
}