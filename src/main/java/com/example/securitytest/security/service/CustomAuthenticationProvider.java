package com.example.securitytest.security.service;

import com.example.securitytest.user.domain.User;
import com.example.securitytest.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.Collections;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

  private final UserRepository userRepository;

  @Override
  public Authentication authenticate(Authentication authentication){
    String name = authentication.getName();
    String password = authentication.getCredentials().toString();

    User user = userRepository.findByName(name)
        .orElseThrow(NullPointerException::new);

    if (!user.isMatchPassword(password)) {
      //패스워드가 틀릴경우 exception 발생 로직
    }

    SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(
        user.getAuthorities().getRole());

    //임시 인증객체를 진짜 인증객체로 생성
    return new UsernamePasswordAuthenticationToken(name, password,
        new ArrayList<>(Collections.singletonList(simpleGrantedAuthority)));
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(
        UsernamePasswordAuthenticationToken.class);
  }
}
