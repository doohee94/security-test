package com.example.securitytest.security.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Slf4j
@Getter
public class UrlFilterInvocationSecurityMetadataSource implements
    FilterInvocationSecurityMetadataSource {

  private Map<RequestMatcher, List<ConfigAttribute>> requestMap;

  public UrlFilterInvocationSecurityMetadataSource() {
    this.requestMap = new HashMap<>();

    //GET, POST를 포함한 모든 HTTPMethod를 ROLE_ADMIN 권한 접근 허용
    requestMap.put(new AntPathRequestMatcher("/admin"),
        Collections.singletonList(new SecurityConfig("ROLE_ADMIN")));

    //ROLE_ADMIN, ROLE_COMPANY -> `/company` GET 허용
    requestMap.put(new AntPathRequestMatcher("/company", "GET"),
        Arrays.asList(new SecurityConfig("ROLE_ADMIN"), new SecurityConfig("ROLE_COMPANY")));
    //ROLE_ADMIN -> '/company' POST 허용
    requestMap.put(new AntPathRequestMatcher("/company", "POST"),
        Collections.singletonList(new SecurityConfig("ROLE_ADMIN")));

    //ROLE_ADMIN, ROLE_COMPANY, ROLE_USER -> `/user` GET 허용
    requestMap.put(new AntPathRequestMatcher("/user", "GET"),
        Arrays.asList(new SecurityConfig("ROLE_ADMIN"), new SecurityConfig("ROLE_COMPANY"),
            new SecurityConfig("ROLE_USER")));
    //ROLE_ADMIN, ROLE_COMPANY -> '/user' POST 허용
    requestMap.put(new AntPathRequestMatcher("/user", "POST"),
        Arrays.asList(new SecurityConfig("ROLE_ADMIN"), new SecurityConfig("ROLE_COMPANY")));


  }

  @Override
  public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
    final HttpServletRequest request = ((FilterInvocation) object).getRequest();

    if (requestMap != null) {
      Set<Map.Entry<RequestMatcher, List<ConfigAttribute>>> entries = requestMap.entrySet();
      for (Map.Entry<RequestMatcher, List<ConfigAttribute>> entry : entries) {
        if (entry.getKey().matches(request)) {
          return entry.getValue();
        }
      }
    }

    return Collections.emptyList();
  }

  @Override
  public Collection<ConfigAttribute> getAllConfigAttributes() {
    Set<ConfigAttribute> allAttributes = new HashSet<>();

    for (Map.Entry<RequestMatcher, List<ConfigAttribute>> entry : requestMap
        .entrySet()) {
      allAttributes.addAll(entry.getValue());
    }

    return allAttributes;
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return FilterInvocation.class.isAssignableFrom(clazz);
  }


  public void reset(Map<RequestMatcher, List<ConfigAttribute>> requestMap) {
    this.requestMap = requestMap;
  }
}
