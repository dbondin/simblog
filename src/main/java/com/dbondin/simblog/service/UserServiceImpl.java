package com.dbondin.simblog.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbondin.simblog.entity.User;
import com.dbondin.simblog.entity.UserRole;
import com.dbondin.simblog.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;
  
  @Autowired
  private PasswordEncoder passwordEncoder;
  
  @Override
  @Transactional(readOnly = true)
  public List<User> findAll() {
    return userRepository.findAll();
  }
  
  @Override
  @Transactional(readOnly = true)
  public long count() {
    return userRepository.count();
  }
  
  @Override
  @Transactional(readOnly = false)
  public Optional<User> findById(final Long id) {
    return userRepository.findById(id);
  }
  
  @Override
  @Transactional(readOnly = false)
  public User save(final User user) {
    return userRepository.save(user);
  }
  
  @Override
  @Transactional(readOnly = true)
  public Optional<User> findByUsername(final String username) {
    return userRepository.findByUsername(username);
  }
  
  @Override
  public User add(String username, String password) {
    final User user = new User();
    user.setUsername(username);
    user.setPassword(passwordEncoder.encode(password));
    user.setEnabled(true);
    user.getRoles().add(UserRole.USER);
    return userRepository.save(user);
  }
}
