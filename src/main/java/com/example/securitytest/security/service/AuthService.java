package com.example.securitytest.security.service;

import com.example.securitytest.security.jwt.TokenProvider;
import com.example.securitytest.user.dto.LoginDto;
import com.example.securitytest.user.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

  private final AuthenticationManager authenticationManager;
  private final TokenProvider tokenProvider;

  public TokenDto login(LoginDto loginDto) {

    //임시 인증 객체 생성
    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(loginDto.getName(), loginDto.getPassword());

    //인증 객체 -> AuthenticationProvider에서 인증 작업 후, SecurityContext에 해당 인증 객체 등록
    Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    //토큰으로 변환 시켜 토큰 관련 객체 리턴
    return tokenProvider.createToken(authentication);
  }
}
