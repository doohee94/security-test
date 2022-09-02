package com.example.securitytest.controller;

import com.example.securitytest.security.service.AuthService;
import com.example.securitytest.user.dto.LoginDto;
import com.example.securitytest.user.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class Controller {

  private final AuthService authService;

  @PostMapping("/login")
  public TokenDto login(@RequestBody LoginDto loginDto) {
    return authService.login(loginDto);
  }

  @GetMapping("/company")
  @PreAuthorize("hasRole('COMPANY')")
  public String company() {
    return "company";
  }

  @GetMapping("/admin")
  @PreAuthorize("hasRole('ADMIN')")
  public String admin() {
    return "admin";
  }

  @GetMapping("/user")
  @PreAuthorize("hasRole('USER')")
  public String user() {
    return "user";
  }

}
