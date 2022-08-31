package com.example.securitytest.user.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "authority")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Authority {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "authority_id")
  private Long id;

  @Column(name = "role", length = 50)
  private String role;

}
