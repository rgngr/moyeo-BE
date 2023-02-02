package com.hanghae.finalProject.rest.user.controller;

import com.hanghae.finalProject.AcceptanceTest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class UserControllerTest extends AcceptanceTest {
     
     private static final String EMAIL = "jojtest123@nate.com";
     private static final String PASSWORD = "joung18@#$";
     
     @DisplayName ("회원가입")
     @Test
     void signupTest() {
          // Given
          Map<String, String> params = new HashMap<>();
          params.put("username", "장영주test");
          params.put("password", "joung18@#$");
          params.put("email", "jojtest123@nate.com");
          
          // When
          ExtractableResponse<Response> response =
               RestAssured
               .given().log().all()
               .body(params)
               .contentType(MediaType.APPLICATION_JSON_VALUE)
               .when()
               .post("/api/users/signup")
               .then().log().all()
               .extract();
          
          // Then
//          assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
          assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
          assertThat(response.body().jsonPath().getString("statusMsg"))
               .isEqualTo("중복된 email 입니다");
//          assertThat(response.header("Location")).isNotBlank();
     }
     
     @DisplayName ("로그인")
     @Test
     void loginTest() {
          // Given
          Map<String, String> params = new HashMap<>();
          params.put("password", PASSWORD);
          params.put("email", EMAIL);
     
          // When
          ExtractableResponse<Response> response =
               RestAssured
                    .given().log().all()
                    .body(params)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .post("/api/users/login")
                    .then().log().all()
                    .extract();
     
          // Then
          assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
          assertThat(response.body().jsonPath().getString("statusMsg"))
               .isEqualTo("로그인 성공");
          assertThat(response.header("Authorization")).isNotBlank();
     }
     
     @DisplayName ("프로필 수정 페이지 불러오기")
     @Test
     void getProfileUpdatePageTest() {
          // 로그인 토큰구하기
          String accessToken = getToken();
          // Given
          // When
          ExtractableResponse<Response> response =
               RestAssured
                    .given().log().all()
                    .header("Authorization", accessToken)
                    .when()
                    .get("/api/users/profile/update-page")
                    .then().log().all()
                    .extract();
          // Then
          assertThat(response.body().jsonPath().getString("data.username")).isNotBlank();
     }
     
     @DisplayName ("프로필 이미지 변경")
     @Test
     void updateProfileUrlTest(){
     
     }
     
     @DisplayName("프로필 이미지 삭제")
     @Test
     void deleteProfileUrlTest() {
          // 로그인 토큰구하기
          String accessToken = getToken();
          // Given
          // When
          ExtractableResponse<Response> response =
               RestAssured
                    .given().log().all()
                    .header("Authorization", accessToken)
                    .when()
                    .delete("/api/users/profile-url")
                    .then().log().all()
                    .extract();
          // Then
          assertThat(response.body().jsonPath().getString("statusMsg")).isEqualTo("프로필 이미지 삭제 성공");
     }
     
     @DisplayName("프로필 username/자기소개 수정")
     @Test
     void updateProfileTest() {
          String accessToken = getToken();
          // Given
          Map<String, String> params = new HashMap<>();
          String newUsername = "testUser"+(int)(Math.random()*100+1);
          params.put("username", newUsername);
          params.put("profileMsg", "changed profile msg");
          // When
          ExtractableResponse<Response> response =
               RestAssured
                    .given().log().all()
                    .header("Authorization", accessToken)
                    .body(params)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .patch("/api/users/profile")
                    .then().log().all()
                    .extract();
          // Then
          assertThat(response.body().jsonPath().getString("data.username")).isEqualTo(newUsername);
          assertThat(response.body().jsonPath().getString("data.profileMsg")).isEqualTo("changed profile msg");
     }
     
     @DisplayName("마이페이지 불러오기")
     @Test
     void getMypageTest() {
          String accessToken = getToken();
          // Given
          // When
          ExtractableResponse<Response> response =
               RestAssured
                    .given().log().all()
                    .header("Authorization", accessToken)
                    .when()
                    .get("/api/users/mypage")
                    .then().log().all()
                    .extract();
          // Then
          assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
          assertThat(response.body().jsonPath().getString("data.username")).isNotEmpty();
          assertThat(response.body().jsonPath().getInt("data.attendantsNum")).isGreaterThanOrEqualTo(0);
     }
     
     @DisplayName("비밀번호 변경")
     @Test
     void passwordChangeTest() {
          String accessToken = getToken();
          // Given
          Map<String, String> params = new HashMap<>();
          params.put("email", EMAIL);
          params.put("password", PASSWORD);
          params.put("passwordCheck", PASSWORD);
          
          // When
          ExtractableResponse<Response> response =
               RestAssured
                    .given().log().all()
                    .header("Authorization", accessToken)
                    .body(params)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .patch("/api/users/passwordChange")
                    .then().log().all()
                    .extract();
          // Then
          assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
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