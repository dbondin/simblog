package com.dbondin.simblog.config;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.dbondin.simblog.service.UserService;

@Component
public class SimblogUserDetailsService implements UserDetailsService {

  @Autowired
  private UserService userService;

  @Override
  public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {

    final com.dbondin.simblog.entity.User simblogUser = userService.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

    return new User(simblogUser.getUsername(), simblogUser.getPassword(), simblogUser.isEnabled(), true, true, true,
        new ArrayList<>());
  };

}
