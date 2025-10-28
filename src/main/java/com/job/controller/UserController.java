package com.job.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties.Http;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.job.domain.User;
import com.job.service.UserService;
import com.job.util.error.IdInvalidException;




@RestController
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    public ResponseEntity<User> createNewUser(@RequestBody User PostManUser) {
        String hashPassword= this.passwordEncoder.encode(PostManUser.getPassword());
        PostManUser.setPassword(hashPassword);
        User user = userService.createUser(PostManUser);
        return  ResponseEntity.status(HttpStatus.CREATED).body(user);
        
    }
 

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) throws IdInvalidException{
        if(id>=1500){
            throw new IdInvalidException("ID is invalid, must be less than 1500");              
        }
         userService.handleDeleteUser(id);
         return ResponseEntity.ok("User deleted successfully");
    }
       @PutMapping("/users")
    public ResponseEntity<User> putUser(@RequestBody User PostManUser) {
       
        User  user = this.userService.handleUpdateUser(PostManUser); 

        return ResponseEntity.ok(user);
    }
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
      User user = this.userService.handleFindUser(id);
        return ResponseEntity.ok(user);
    }
  @GetMapping("/users")
public ResponseEntity<List<User>> getAllUsers() {
    return ResponseEntity.ok(userService.handleFindAllUsers());
}

}
 