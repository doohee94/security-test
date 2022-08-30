package com.example.securitytest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

  @GetMapping("/admin")
  public String test1(){
    return "admin";
  }

  @GetMapping("/user")
  public String test2(){
    return "user";
  }
}
