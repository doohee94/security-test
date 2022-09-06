package com.example.securitytest.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.securitytest.user.dto.LoginDto;
import com.example.securitytest.user.dto.TokenDto;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

    RestAssured
        .given().log().all()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().post("/reset")
        .then().log().all().extract();
  }


  @Nested
  class POST_테스트{

    @ParameterizedTest
    @CsvSource(value = {"admin,admin","admin,company","admin,user","company,user"})
    void POST_200_테스트(String loginUser, String url){
      //given
      TokenDto loginDto = getLoginDto(loginUser);

      //when
      ExtractableResponse<Response> response = RestAssured
          .given().log().all()
          .header("Authorization", "Bearer " + loginDto.getAccessToken())
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .when().post("/"+url)
          .then().log().all().extract();

      //then
      assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @ParameterizedTest
    @CsvSource(value = {"company,company","user,user"})
    void POST_403_테스트(String loginUser, String url){
      //given
      TokenDto loginDto = getLoginDto(loginUser);

      //when
      ExtractableResponse<Response> response = RestAssured
          .given().log().all()
          .header("Authorization", "Bearer " + loginDto.getAccessToken())
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .when().post("/"+url)
          .then().log().all().extract();

      //then
      assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

  }

  @Nested
  class GET_테스트{

    @ParameterizedTest
    @CsvSource(value = {"admin,admin","admin,company","admin,user","company,company","company,user","user,user"})
    void GET_200_테스트(String loginUser, String url){
      //given
      TokenDto loginDto = getLoginDto(loginUser);

      //when
      ExtractableResponse<Response> response = RestAssured
          .given().log().all()
          .header("Authorization", "Bearer " + loginDto.getAccessToken())
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .when().get("/"+url)
          .then().log().all().extract();

      //then
      assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @ParameterizedTest
    @CsvSource(value = {"company,admin","user,company"})
    void GET_403_테스트(String loginUser, String url){
      //given
      TokenDto loginDto = getLoginDto(loginUser);

      //when
      ExtractableResponse<Response> response = RestAssured
          .given().log().all()
          .header("Authorization", "Bearer " + loginDto.getAccessToken())
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .when().get("/"+url)
          .then().log().all().extract();

      //then
      assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

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