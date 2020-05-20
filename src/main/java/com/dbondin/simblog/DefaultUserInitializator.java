package com.dbondin.simblog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.dbondin.simblog.entity.User;
import com.dbondin.simblog.entity.UserRole;
import com.dbondin.simblog.service.UserService;
import com.dbondin.simblog.settings.SimblogSettings;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DefaultUserInitializator implements ApplicationListener<ContextRefreshedEvent> {
  
  @Autowired
  private SimblogSettings simblogConfiguration;
  
  @Autowired
  private UserService userService;
  
  @Autowired
  private PasswordEncoder passwordEncoder;
  
  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    log.info("Checking if any users exists in database");
    if(userService.count() == 0) {
      log.warn("No users found. Creating default one");
      final User user = new User();
      user.setUsername(simblogConfiguration.getDefaultUsername());
      user.setPassword(passwordEncoder.encode(simblogConfiguration.getDefaultPassword()));
      user.setEnabled(true);
      user.getRoles().add(UserRole.ADMIN);
      user.getRoles().add(UserRole.MODERATOR);
      user.getRoles().add(UserRole.USER);
      userService.save(user);
    }
  }

}
