package com.dbondin.simblog.rest;

import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RestController;

import com.dbondin.simblog.service.UserService;
import com.dbondin.simblog.swagger.api.UserApi;
import com.dbondin.simblog.swagger.model.AddUserRequest;
import com.dbondin.simblog.swagger.model.AddUserResponse;
import com.dbondin.simblog.swagger.model.GetUserResponse;
import com.dbondin.simblog.swagger.model.GetUsersResponse;
import com.dbondin.simblog.swagger.model.User;

@RestController
public class UserRestController implements UserApi {

  @Autowired
  private UserService userService;
  
  @Override
  @Secured("ROLE_USER")
  public ResponseEntity<GetUsersResponse> getUsers() {
    return new ResponseEntity<>(
        new GetUsersResponse().status(true)
            .users(userService.findAll().stream().map(entityUser -> convert(entityUser)).collect(Collectors.toList())),
        HttpStatus.OK);
  }

  @Override
  @Secured("ROLE_USER")
  public ResponseEntity<GetUserResponse> getUser(final Long userId) {
    return userService.findById(userId).map(
        entityUser -> new ResponseEntity<>(new GetUserResponse().status(true).user(convert(entityUser)), HttpStatus.OK))
        .orElse(new ResponseEntity<GetUserResponse>(new GetUserResponse().status(false), HttpStatus.NOT_FOUND));
  }

  @Override
  @Secured("ROLE_ADMIN")
  public ResponseEntity<AddUserResponse> addUser(@Valid AddUserRequest addUserRequest) {
    return new ResponseEntity<>(new AddUserResponse()
        .user(convert(userService.add(addUserRequest.getUsername(), addUserRequest.getPassword()))), HttpStatus.OK);
  }

  private User convert(final com.dbondin.simblog.entity.User entityUser) {
    final User swaggerUser = new User();
    BeanUtils.copyProperties(entityUser, swaggerUser);
    return swaggerUser;
  }
}
