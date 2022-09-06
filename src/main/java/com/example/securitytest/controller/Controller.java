package com.example.securitytest.controller;

import com.example.securitytest.security.service.AuthService;
import com.example.securitytest.security.service.SecurityService;
import com.example.securitytest.user.dto.LoginDto;
import com.example.securitytest.user.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class Controller {

  private final AuthService authService;
  private final SecurityService securityService;

  @PostMapping("/login")
  public TokenDto login(@RequestBody LoginDto loginDto) {
    return authService.login(loginDto);
  }

  @GetMapping("/company")
  public String company() {
    return "company";
  }

  @GetMapping("/admin")
  public String admin() {
    return "admin";
  }

  @GetMapping("/user")
  public String user() {
    return "user";
  }

  @PostMapping("/company")
  public String companyPost() {
    return "company";
  }

  @PostMapping("/admin")
  public String adminPost() {
    return "admin";
  }

  @PostMapping("/user")
  public String userPost() {
    return "user";
  }

  @PostMapping("/reset")
  public String reset() {
    securityService.resetAuthorities();
    return "reset";
  }
}
