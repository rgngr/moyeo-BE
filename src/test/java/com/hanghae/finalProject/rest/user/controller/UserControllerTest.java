package com.hanghae.finalProject.rest.user.controller;

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

class UserControllerTest extends AcceptanceTest {
     
     private static final String EMAIL = "jojtest123@nate.com";
     private static final String PASSWORD = "joung18@#$";
     
     @DisplayName ("회원가입")
     @Test
     void signup() {
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
               .isEqualTo("중복된 username 입니다.");
//          assertThat(response.header("Location")).isNotBlank();
     }
     
     @DisplayName ("로그인")
     @Test
     void login() {
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
     void getProfileUpdatePage() {
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
          assertThat(response.body().jsonPath().getString("data.username")).isBlank();
     }
     
     @DisplayName("프로필 이미지 삭제")
     @Test
     void deleteProfileUrl() {
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
}