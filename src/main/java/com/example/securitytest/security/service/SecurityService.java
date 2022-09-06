package com.example.securitytest.security.service;

import com.example.securitytest.security.model.UrlFilterInvocationSecurityMetadataSource;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityService {

  private final FilterSecurityInterceptor filterSecurityInterceptor;
  private final UrlFilterInvocationSecurityMetadataSource urlFilterInvocationSecurityMetadataSource;

  public void resetAuthorities() {
    urlFilterInvocationSecurityMetadataSource.reset(newRequestMap());
    filterSecurityInterceptor.setSecurityMetadataSource(urlFilterInvocationSecurityMetadataSource);
  }

  private Map<RequestMatcher, List<ConfigAttribute>> newRequestMap() {
    Map<RequestMatcher, List<ConfigAttribute>> requestMap = new HashMap<>();

    //GET, POST를 포함한 모든 HTTPMethod를 ROLE_ADMIN 권한 접근 허용
    requestMap.put(new AntPathRequestMatcher("/admin"),
        Collections.singletonList(new SecurityConfig("ROLE_ADMIN")));

    requestMap.put(new AntPathRequestMatcher("/company"),
        Arrays.asList(new SecurityConfig("ROLE_ADMIN"), new SecurityConfig("ROLE_COMPANY")));

    requestMap.put(new AntPathRequestMatcher("/user"),
        Arrays.asList(new SecurityConfig("ROLE_ADMIN"), new SecurityConfig("ROLE_COMPANY"),
            new SecurityConfig("ROLE_USER")));

    return requestMap;
  }

}
