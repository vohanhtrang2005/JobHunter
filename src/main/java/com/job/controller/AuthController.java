package com.job.controller;


import org.springframework.http.HttpHeaders; 
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.job.domain.User;
import com.job.domain.dto.LoginDTO;
import com.job.domain.dto.RestLoginDTO;
import com.job.service.UserService;
import com.job.util.SecurityUtil;
import com.job.util.annotation.ApiMessage;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping("/api/v1")
public class AuthController {
        @Value("${hoidanit.jwt.access-token-validity-in-seconds}")
    private long accessTokenExpiration;
 
private final UserService userService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;        
    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil, UserService userService) {
        this.userService = userService;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
    }
    @PostMapping("/login")
    public ResponseEntity<RestLoginDTO> login(@Valid  @RequestBody LoginDTO loginDto) {
        //Nạp input gồm username/password vào Security
 UsernamePasswordAuthenticationToken authenticationToken 
= new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

//xác thực người dùng => cần viết hàm loadUserByUsername
Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
//create tooken 
String access_token = this.securityUtil.createToken(authentication);
SecurityContextHolder.getContext().setAuthentication(authentication);


RestLoginDTO res = new RestLoginDTO();
User currentUser = this.userService.handleGetUserByUsername(loginDto.getUsername());

if(currentUser != null) {
    RestLoginDTO.UserLogin userLogin = new RestLoginDTO.UserLogin(currentUser.getId(), currentUser.getEmail(), currentUser.getName());
    res.setUser(userLogin);
}
res.setAccessToken(access_token);

//create refressh token
String refresh_token = this.securityUtil.createFreshToken(loginDto.getUsername(), res);
this.userService.updateUserToken(refresh_token, loginDto.getUsername());

ResponseCookie resCookie = ResponseCookie
.from("refresh_token", refresh_token)
.httpOnly(true)
.path("/")
.maxAge(accessTokenExpiration) 
.secure(true)// Chỉ gửi cookie qua HTTPS

.build();

    
return ResponseEntity.ok()
.header(HttpHeaders.SET_COOKIE, resCookie.toString())
.body(res);
    }

@GetMapping("/auth/account")
@ApiMessage("fetch account mesage")
public String getAccount() {
    return "fetch account";
}



}
