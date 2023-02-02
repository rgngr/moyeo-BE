package com.hanghae.finalProject.rest.meeting.controller;

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

class MeetingControllerTest extends AcceptanceTest {
     private static final String EMAIL = "jojtest123@nate.com";
     private static final String PASSWORD = "joung18@#$";
     private static final Long MEETINGID = 10236L;
     
     @DisplayName ("모임 상세 조회")
     @Test
     void getMeeting() {
          // 로그인 토큰구하기
          String accessToken = getToken();
          // Given
          // When
          ExtractableResponse<Response> response =
               RestAssured
                    .given().log().all()
                    .header("Authorization", accessToken)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/api/meetings/"+MEETINGID)
                    .then().log().all()
                    .extract();
          // Then
          assertThat(response.body().jsonPath().getLong("data.id")).isEqualTo(MEETINGID);
     }
     @DisplayName ("GET 배너이미지")
     @Test
     void getBanners() {
          // 로그인 토큰구하기
          String accessToken = getToken();
          // Given
          // When
          ExtractableResponse<Response> response =
               RestAssured
                    .given().log().all()
                    .when()
                    .get("/api/meetings/banner")
                    .then().log().all()
                    .extract();
          // Then
          assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
          assertThat(response.body().jsonPath().getList("data")).isNotNull();
     }
     
     @DisplayName("모임 전체 조회")
     @Test
     void getMeetings() {
          // 로그인 토큰구하기
          String accessToken = getToken();
          // Given
          // When
          ExtractableResponse<Response> response =
               RestAssured
                    .given().log().all()
                    .header("Authorization", accessToken)
                    .param("sortyby", "popular")
                    .param("category","밥모여")
                    .param("meetingId",0)
                    .when()
                    .get("/api/meetings")
                    .then().log().all()
                    .extract();
          // Then
          assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
          assertThat(response.body().jsonPath().getList("data.meetingList")).isNotNull();
     }
     
     @DisplayName("모임명 검색")
     @Test
     void getMeetingsBySearch() {
          // 로그인 토큰구하기
          String accessToken = getToken();
          // Given
          // When
          ExtractableResponse<Response> response =
               RestAssured
                    .given().log().all()
                    .header("Authorization", accessToken)
                    .param("searchBy", "비번")
                    .param("category","밥모여")
                    .param("meetingId")
                    .when()
                    .get("/api/meetings/search")
                    .then().log().all()
                    .extract();
          // Then
          assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
          assertThat(response.body().jsonPath().getList("data.meetingList")).isNotNull();
     }
     @DisplayName("모임 생성")
     @Test
     void createMeeting() {
          // 로그인 토큰구하기
          String accessToken = getToken();
          // Given
          Map<String, Object> params = new HashMap<>();
          
          // When
          
          // Then
          // .multiPart("data",body,"application/json")
          // .multiPart("file[0]", new File(file1),"multipart/form-data")
          
     }
     
     @Test
     void createMeetingTemp() {
     }
     
     @Test
     void updateAllMeeting() {
     }
     
     @Test
     void updateMeetingImage() {
     }
     
     @Test
     void getUpdatePage() {
     }
     
     @Test
     void updateLink() {
     }
     
     @Test
     void deleteMeeting() {
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