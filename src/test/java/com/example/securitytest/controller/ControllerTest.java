package com.example.securitytest.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.securitytest.user.dto.LoginDto;
import com.example.securitytest.user.dto.TokenDto;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = {"test"})
@TestMethodOrder(MethodOrderer.MethodName.class)
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
  void ADMIN_권한으로_admin_호출_200(){
    //given
    TokenDto loginDto = getLoginDto("admin");

    //when
    ExtractableResponse<Response> response = RestAssured
        .given().log().all()
        .header("Authorization", "Bearer " + loginDto.getAccessToken())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().get("/admin")
        .then().log().all().extract();

    //then
    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
  }

  @Test
  void ADMIN_권한으로_user_호출_200(){
    //given
    TokenDto loginDto = getLoginDto("admin");

    //when
    ExtractableResponse<Response> response = RestAssured
        .given().log().all()
        .header("Authorization", "Bearer " + loginDto.getAccessToken())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().get("/user")
        .then().log().all().extract();

    //then
    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
  }

  @Test
  void ADMIN_권한으로_company_호출_200(){
    //given
    TokenDto loginDto = getLoginDto("admin");

    //when
    ExtractableResponse<Response> response = RestAssured
        .given().log().all()
        .header("Authorization", "Bearer " + loginDto.getAccessToken())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().get("/company")
        .then().log().all().extract();

    //then
    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
  }

  @Test
  void USER_권한으로_user_호출_200(){
    //given
    TokenDto loginDto = getLoginDto("user");

    //when
    ExtractableResponse<Response> response = RestAssured
        .given().log().all()
        .header("Authorization", "Bearer " + loginDto.getAccessToken())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().get("/user")
        .then().log().all().extract();

    //then
    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
  }

  @Test
  void USER_권한으로_company_호출_403(){
    //given
    TokenDto loginDto = getLoginDto("user");

    //when
    ExtractableResponse<Response> response = RestAssured
        .given().log().all()
        .header("Authorization", "Bearer " + loginDto.getAccessToken())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().get("/company")
        .then().log().all().extract();

    //then
    assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
  }

  @Test
  void USER_권한으로_admin_호출_403(){
    //given
    TokenDto loginDto = getLoginDto("user");

    //when
    ExtractableResponse<Response> response = RestAssured
        .given().log().all()
        .header("Authorization", "Bearer " + loginDto.getAccessToken())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().get("/admin")
        .then().log().all().extract();

    //then
    assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
  }

  @Test
  void COMPANY_권한으로_company_호출_200(){
    //given
    TokenDto loginDto = getLoginDto("company");

    //when
    ExtractableResponse<Response> response = RestAssured
        .given().log().all()
        .header("Authorization", "Bearer " + loginDto.getAccessToken())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().get("/company")
        .then().log().all().extract();

    //then
    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
  }

  @Test
  void COMPANY_권한으로_user_호출_200(){
    //given
    TokenDto loginDto = getLoginDto("company");

    //when
    ExtractableResponse<Response> response = RestAssured
        .given().log().all()
        .header("Authorization", "Bearer " + loginDto.getAccessToken())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().get("/user")
        .then().log().all().extract();

    //then
    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
  }

  @Test
  void COMPANY_권한으로_admin_호출_403(){
    //given
    TokenDto loginDto = getLoginDto("company");

    //when
    ExtractableResponse<Response> response = RestAssured
        .given().log().all()
        .header("Authorization", "Bearer " + loginDto.getAccessToken())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().get("/admin")
        .then().log().all().extract();

    //then
    assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
  }



  private TokenDto getLoginDto(String role) {
    LoginDto loginDto = new LoginDto();
    loginDto.setName(role);
    loginDto.setPassword(role);

    //when
    ExtractableResponse<Response> response = RestAssured
        .given().log().all()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(loginDto)
        .when().post("/login")
        .then().log().all().extract();

    return response.as(TokenDto.class);
  }


}