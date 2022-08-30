package com.example.securitytest.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {

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
        .antMatchers("/admin").hasRole("ROLE_ADMIN")
        .antMatchers("/**").permitAll()
        .anyRequest().authenticated();

    security.formLogin().disable();

    security.httpBasic().disable();

    return security.build();
  }

}
