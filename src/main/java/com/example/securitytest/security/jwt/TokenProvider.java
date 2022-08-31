package com.example.securitytest.security.jwt;


import com.example.securitytest.user.dto.TokenDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider implements InitializingBean {

  private String secretKey = "secretkeysecretkeysecretkeysecretkeysecretkeysecretkeysecretkeysecretkeyeysecretkeysecretkeysecretkeyseysecretkeysecretkeysecretkeys";
  //토큰 만료 시간
  private long tokenValidityInSeconds = 86400000;

  private Key key;

  @Override
  public void afterPropertiesSet() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    this.key = Keys.hmacShaKeyFor(keyBytes);
  }

  public TokenDto createToken(Authentication authentication) {
    String authorities = authentication.getAuthorities()
        .stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(","));

    long now = (new Date()).getTime();
    Date validity = new Date(now + tokenValidityInSeconds);

    Map<String, Object> headerMap = new HashMap<>();
    headerMap.put("typ", "JWT");

    return new TokenDto(Jwts.builder()
        .signWith(key, SignatureAlgorithm.HS512)
        .setHeader(headerMap)
        .setSubject("security-test")
        .claim("user_name", authentication.getName())
        .claim("Bearer", authorities)
        .setExpiration(validity)
        .compact(), validity.toInstant().toEpochMilli());
  }

}
