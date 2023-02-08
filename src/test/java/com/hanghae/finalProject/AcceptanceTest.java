package com.hanghae.finalProject;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

//테스트를 실행하기 전에 새로운 애플리케이션 컨텍스트를 생성해서 사용할 수 있도록 해준다.
@DirtiesContext (classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {
     @LocalServerPort
     int port;
     
     @BeforeEach
     public void setUp() {
          RestAssured.port = port;
     }
}
