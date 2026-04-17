package com.job.controller;


import org.springframework.http.HttpHeaders; 
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
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
import com.job.util.error.IdInvalidException;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.CookieValue;
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
    @PostMapping("/auth/login")
    public ResponseEntity<RestLoginDTO> login(@Valid  @RequestBody LoginDTO loginDto) {
        //Nạp input gồm username/password vào Security
 UsernamePasswordAuthenticationToken authenticationToken 
= new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

//xác thực người dùng => cần viết hàm loadUserByUsername
Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
//create tooken 

SecurityContextHolder.getContext().setAuthentication(authentication);


RestLoginDTO res = new RestLoginDTO();
User currentUser = this.userService.handleGetUserByUsername(loginDto.getUsername());

if(currentUser != null) {
    RestLoginDTO.UserLogin userLogin = new RestLoginDTO.UserLogin(currentUser.getId(), currentUser.getEmail(), currentUser.getName());
    res.setUser(userLogin);
}
String access_token = this.securityUtil.createAccessToken(authentication.getName(), res.getUser());
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
public ResponseEntity<RestLoginDTO.UserLogin> getAccount() {
    String email = SecurityUtil.getCurrentUserJWT().isPresent() 
    ? SecurityUtil.getCurrentUserJWT().get() : "";
User currentUser = this.userService.handleGetUserByUsername(email);
RestLoginDTO.UserLogin userLogin = new RestLoginDTO.UserLogin();
if(currentUser != null) {
   userLogin.setId(currentUser.getId());
   userLogin.setEmail(currentUser.getEmail());
    userLogin.setName(currentUser.getName());
    
}
    return ResponseEntity.ok(userLogin);
}

@GetMapping("auth/refresh")
@ApiMessage("get User by refresh token")
public ResponseEntity<RestLoginDTO> getRefreshToken (@CookieValue(name="refresh_token", defaultValue = "abc") String refresh_token) throws IdInvalidException{
    if(refresh_token.equals("abc")) {
        throw new IdInvalidException("Ban chưa có refresh token o cookie");
    }
    //check valid token
      Jwt decode =   this.securityUtil.checkValidToken(refresh_token);
      String email = decode.getSubject();
      //check user có tồn tại và token có hợp lệ không
      User currentUser =this.userService.findByRefreshTokenAndEmail(refresh_token, email);
      if(currentUser == null) {
        throw new IdInvalidException("Refresh token không hợp lệ");
      } 

    //issue new token/set refresh token as cookie
RestLoginDTO res = new RestLoginDTO();
User currentUserDB = this.userService.handleGetUserByUsername(email);

if(currentUserDB != null) {
    RestLoginDTO.UserLogin userLogin = new RestLoginDTO.UserLogin(currentUser.getId(), currentUser.getEmail(), currentUser.getName());
    res.setUser(userLogin);
}
String access_token = this.securityUtil.createAccessToken(email, res.getUser());
res.setAccessToken(access_token);


//create refressh token
String new_refresh_token = this.securityUtil.createFreshToken(email, res);
this.userService.updateUserToken(new_refresh_token, email);

ResponseCookie resCookie = ResponseCookie
.from("refresh_token", new_refresh_token)
.httpOnly(true)
.path("/")
.maxAge(accessTokenExpiration) 
.secure(true)// Chỉ gửi cookie qua HTTPS

.build();

    
return ResponseEntity.ok()
.header(HttpHeaders.SET_COOKIE, resCookie.toString())
.body(res);

}
@PostMapping("/auth/logout")
@ApiMessage("logout user")
public ResponseEntity<Void> logout() throws IdInvalidException {
    String email = SecurityUtil.getCurrentUserLogin().isPresent() 
    ? SecurityUtil.getCurrentUserLogin().get() : "";
    if(email.equals("")){
        throw new IdInvalidException("Access token không hợp lệ");
    }
    //update refresh token = null
    this.userService.updateUserToken("null", email);
   
    ResponseCookie deletResponseCookie = ResponseCookie
.from("refresh_token", "null")
.httpOnly(true)
.path("/")
.maxAge(0)
.build();
    return ResponseEntity.ok()
    .header(HttpHeaders.SET_COOKIE, deletResponseCookie.toString())
    .body(null);
}



}
