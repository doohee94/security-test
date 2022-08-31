package com.example.securitytest.security.model;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException, ServletException {
    commence(request, response, accessDeniedException);
  }

  public void commence(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException authException) throws IOException {
    log.error("[CustomAccessDeniedHandler] "+authException.getMessage());
    authException.printStackTrace();
    response.setStatus(403);
    response.setContentType("application/json;charset=UTF-8");
  }
}
