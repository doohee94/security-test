package com.example.securitytest.security.jwt;

import io.jsonwebtoken.JwtException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

  public static final String AUTHORIZATION_HEADER = "Authorization";
  private final TokenProvider tokenProvider;

  @Override
  protected void doFilterInternal(HttpServletRequest servletRequest,
      HttpServletResponse servletResponse,
      FilterChain filterChain) throws ServletException, IOException {

    try {
      String jwt = resolveToken(servletRequest);
      //만약 토큰에 이상이 있다면 오류가 발생한다.
      if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
        //tokenProvider에서 jwt를 가져가 Authentication 객체생성
        Authentication authentication = this.tokenProvider.getAuthentication(jwt);
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
      //이상이 없다면 계속 진행
      filterChain.doFilter(servletRequest, servletResponse);
    } catch (JwtException e) {
      log.error("[JWTExceptionHandlerFilter] "+e.getMessage());
      servletResponse.setStatus(401);
      servletResponse.setContentType("application/json;charset=UTF-8");
    }
  }

  private String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
    if (StringUtils.hasText(bearerToken) && StringUtils.startsWithIgnoreCase(bearerToken,
        "Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }
}

