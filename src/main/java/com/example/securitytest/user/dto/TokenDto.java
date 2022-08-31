package com.example.securitytest.user.dto;

import lombok.Data;

@Data
public class TokenDto {

  private String accessToken;
  private String tokenType;
  private long expiresIn;

  public TokenDto(String accessToken, long validity) {
    this.accessToken = accessToken;
    this.tokenType = "Bearer";
    this.expiresIn = validity;
  }
}
