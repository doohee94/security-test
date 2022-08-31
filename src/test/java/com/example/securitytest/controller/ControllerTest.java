package com.example.securitytest.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.securitytest.user.dto.LoginDto;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = {"test"})
class ControllerTest {

  @Value("${local.server.port}")
  int port;

  @BeforeEach
  public void setUp() {
    if (RestAssured.port == RestAssured.UNDEFINED_PORT) {
      RestAssured.port = port;
    }
    RestAssured.defaultParser = Parser.JSON;
  }

  @Test
  void 로그인_테스트(){
    //given
    LoginDto loginDto = new LoginDto();
    loginDto.setName("admin");
    loginDto.setPassword("admin");

    //when
    ExtractableResponse<Response> response = RestAssured
        .given().log().all()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(loginDto)
        .when().post("/login")
        .then().log().all().extract();

    //then
    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

  }

  @Test
  void test1(){
    ExtractableResponse<Response> response = RestAssured
        .given().log().all()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().get("/admin")
        .then().log().all().extract();

    assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
  }


  @Test
  void test2(){
    ExtractableResponse<Response> response = RestAssured
        .given().log().all()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().get("/user")
        .then().log().all().extract();

    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
  }

}