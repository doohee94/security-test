package com.example.securitytest.security.config;

import com.example.securitytest.security.jwt.JWTFilter;
import com.example.securitytest.security.model.CustomAccessDeniedHandler;
import com.example.securitytest.security.model.CustomAuthenticationEntryPoint;

import com.example.securitytest.security.model.UrlFilterInvocationSecurityMetadataSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {

  private final AuthenticationProvider authenticationProvider;
  private final JWTFilter jwtFilter;
  private final CustomAccessDeniedHandler customAccessDeniedHandler;
  private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {

    security.csrf().disable()
        .headers().frameOptions().disable();

    security.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    security.authorizeHttpRequests()
        .antMatchers("/webjars/**", "/swagger/**",
            "/swagger*/**", "/swagger-resources", "/swagger-resources/**",
            "/api/v3/api-docs", "/v3/api-docs",
            "/configuration/security", "/configuration/ui",
            "/css/**", "/js/**", "/img/**"
        ).permitAll()
        .antMatchers("/**").permitAll()
        .anyRequest().authenticated()
    ;

    security
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(customFilterSecurityInterceptor(), FilterSecurityInterceptor.class);

    security
        .exceptionHandling()
        .accessDeniedHandler(customAccessDeniedHandler)
        .authenticationEntryPoint(customAuthenticationEntryPoint);

    security.formLogin().disable();

    security.httpBasic().disable();

    return security.build();
  }

  @Bean
  public AuthenticationManager authenticationManagerBean() {
    List<AuthenticationProvider> authenticationProviderList = new ArrayList<>();
    authenticationProviderList.add(authenticationProvider);
    ProviderManager authenticationManager = new ProviderManager(authenticationProviderList);
    authenticationManager.setAuthenticationEventPublisher(defaultAuthenticationEventPublisher());
    return authenticationManager;
  }

  @Bean
  DefaultAuthenticationEventPublisher defaultAuthenticationEventPublisher() {
    return new DefaultAuthenticationEventPublisher();
  }

  @Bean
  public FilterSecurityInterceptor customFilterSecurityInterceptor() {

    FilterSecurityInterceptor filterSecurityInterceptor = new FilterSecurityInterceptor();
    filterSecurityInterceptor
        .setSecurityMetadataSource(urlFilterInvocationSecurityMetadataSource());
    filterSecurityInterceptor.setAccessDecisionManager(affirmativeBased());

    return filterSecurityInterceptor;
  }

  @Bean
  public UrlFilterInvocationSecurityMetadataSource urlFilterInvocationSecurityMetadataSource() {
    return new UrlFilterInvocationSecurityMetadataSource();
  }

  private AccessDecisionManager affirmativeBased() {
    return new AffirmativeBased(Collections.singletonList(new RoleVoter()));
  }

}
