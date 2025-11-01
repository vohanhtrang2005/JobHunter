package com.job.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.job.domain.dto.LoginDTO;
import com.job.util.SecurityUtil;

import jakarta.validation.Valid;



@RestController
public class AuthController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;        
    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
    }
    @PostMapping("/login")
    public ResponseEntity<LoginDTO> login(@Valid  @RequestBody LoginDTO loginDto) {
        //Nạp input gồm username/password vào Security
 UsernamePasswordAuthenticationToken authenticationToken 
= new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

//xác thực người dùng => cần viết hàm loadUserByUsername
Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
this.securityUtil.createToken(authentication);
return ResponseEntity.ok().body(loginDto);
    }
}
