package com.dbondin.simblog.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CurrentUserDetailsServiceImpl implements CurrentUserDetailsService {
  @Override
  public UserDetails getCurrentUserDetails() {
    UserDetails userDetails = null;
    try {
      final Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      if(principal instanceof UserDetails) {
        userDetails = (UserDetails) principal;
      }
      else {
        log.error("Principal is not of UserDetails type {}", principal);
      }
    }
    catch(Throwable t) {
      log.error("Error getting user details", t);
    }
    return userDetails;
  }
}
