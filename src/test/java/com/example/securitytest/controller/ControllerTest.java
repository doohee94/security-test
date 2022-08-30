package com.example.securitytest.controller;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest
class ControllerTest {

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