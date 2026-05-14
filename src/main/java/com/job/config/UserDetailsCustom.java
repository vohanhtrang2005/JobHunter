package com.job.config;

import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;


import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.job.domain.User;
import com.job.service.UserService;
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
    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
    if(user.getRole() !=null){
        //them role 
        authorities.add(new SimpleGrantedAuthority("ROLE_"+user.getRole().getName()));
    }

    //them danh sach permission
if(user.getRole().getPermissions() != null) {
    for(Permission p : user.getRole().getPermissions()) {
        authorities.add(new SimpleGrantedAuthority(p.getName()));
    }
}
    return new org.springframework.security.core.userdetails.User (
        user.getEmail(),
    user.getPassword(),
   authorities//tra ve danh sach authority


    );}
    
}
            