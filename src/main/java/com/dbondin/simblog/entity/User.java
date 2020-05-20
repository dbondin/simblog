package com.dbondin.simblog.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "simblog_user")
@SequenceGenerator(sequenceName = "simblog_user_id_seq", name = AbstractEntity.GENERATOR_NAME, allocationSize = 1)
@Data
public class User extends AbstractEntity {

  @Column(name = "username", length = 255, nullable = false, unique = true)
  private String username;
  
  @Column(name = "password", length = 255, nullable = false, unique = false)
  private String password;
  
  @Column(name = "enabled", nullable = false, unique = false)
  private boolean enabled;
  
  @Enumerated(EnumType.STRING)
  @ElementCollection(targetClass = UserRole.class, fetch = FetchType.EAGER) 
  @CollectionTable(name = "simblog_user_role",
      joinColumns = @JoinColumn(name = "user_id"))
  @Column(name = "user_role")
  private Set<UserRole> roles = new HashSet<>();
}
