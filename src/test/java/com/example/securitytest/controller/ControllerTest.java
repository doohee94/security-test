package com.example.securitytest.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.securitytest.user.dto.LoginDto;
import com.example.securitytest.user.dto.TokenDto;
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
  void 유효하지_않는_토큰으로_API_호출(){
    //given
    String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzZWN1cml0eS10ZXN0IiwidXNlcl9uYW1lIjoiYWRtaW4iLCJCZWFyZXIiOiJST0xFX0FETUlOIiwiZXhwIjoxNjYxOTI0NDYzfQ.NJzHCEoZj2Qxgk27uf_7rG_T2RQRLXhwnQAaPNbz2obk4o8nLI-mSgupuStQ9OiNJ2PD2E367fpJJybbbQPkhQ";

    //when
    ExtractableResponse<Response> response = RestAssured
        .given().log().all()
        .header("Authorization", "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().get("/all")
        .then().log().all().extract();

    //then
    assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
  }

  @Test
  void ADMIN_권한으로_admin_호출(){
    //given
    TokenDto loginDto = getLoginDto();

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
  void ADMIN_권한으로_user_호출(){
    //given
    TokenDto loginDto = getLoginDto();

    //when
    ExtractableResponse<Response> response = RestAssured
        .given().log().all()
        .header("Authorization", "Bearer " + loginDto.getAccessToken())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().get("/user")
        .then().log().all().extract();

    //then
    assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
  }

  @Test
  void HEADER_없이_admin_호출(){
    //when
    ExtractableResponse<Response> response = RestAssured
        .given().log().all()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().get("/admin")
        .then().log().all().extract();

    //then
    assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
  }


  private TokenDto getLoginDto() {
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

    return response.as(TokenDto.class);
  }


}