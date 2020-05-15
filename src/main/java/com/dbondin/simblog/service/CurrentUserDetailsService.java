package com.dbondin.simblog.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface CurrentUserDetailsService {
    UserDetails getCurrentUserDetails();
}
