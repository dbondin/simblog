package com.dbondin.simblog.service;

import java.util.List;
import java.util.Optional;

import com.dbondin.simblog.entity.User;

public interface UserService {
  long count();
  List<User> findAll();
  Optional<User> findById(Long id);
  Optional<User> findByUsername(String username);
  User save(User user);
  User add(String username, String password);
}
