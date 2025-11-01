package com.job.service;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.job.domain.User;
@Component("userDetailService")
public class UserDetailsCustom implements UserDetailsService {

    private final UserService userService;
    public UserDetailsCustom(UserService userService) {
        this.userService = userService;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userService.handleGetUserByUsername(username);
    if(user == null) {
        throw new UsernameNotFoundException("Username/password invalid");
    }
    return new org.springframework.security.core.userdetails.User (
        user.getEmail(),
    user.getPassword(),
    Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))


    );}
    
}
            