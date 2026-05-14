package com.job.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties.Http;
import org.springframework.data.domain.PageRequest;
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
import com.job.domain.request.ReqUpdateUserDTO;
import com.job.domain.respone.ResCreateUserDTO;
import com.job.domain.respone.ResUpdateUserDTO;
import com.job.domain.respone.ResUserDTO;
import com.job.domain.respone.ResultPaginationDTO;
import com.job.service.CompanyService;
import com.job.service.UserService;
import com.job.util.annotation.ApiMessage;
import com.job.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

@RestController
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder; 
      private final CompanyService companyService; 

    public UserController(UserService userService, PasswordEncoder passwordEncoder, CompanyService companyService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.companyService = companyService;
    }

      @PostMapping("/users")
       @PreAuthorize("hasAuthority('USER_CREATE')") 
    @ApiMessage("Create a new user")
    public ResponseEntity<ResCreateUserDTO> createNewUser(@Valid @RequestBody User PostManUser) throws IdInvalidException {
      
        

        String hashPassword = this.passwordEncoder.encode(PostManUser.getPassword());
        PostManUser.setPassword(hashPassword);
        User user = userService.createUser(PostManUser);
        ResCreateUserDTO userDTO = userService.convertToResCreateUserDTO(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);

    }

    // @ExceptionHandler(value = IdInvalidException.class)
    // public ResponseEntity<String> ex (IdInvalidException idInvalidException){
    //     return ResponseEntity.badRequest().body(idInvalidException.getMessage());
    // }
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) throws IdInvalidException {
     if (id >= 1500) {
            throw new IdInvalidException("ID is invalid, must be less than 1500");
        }
        userService.handleDeleteUser(id);

        return ResponseEntity.ok(null);
    }

  @PutMapping("/users")
public ResponseEntity<ResUpdateUserDTO> putUser(
        @RequestBody ReqUpdateUserDTO req)
        throws IdInvalidException {

    if(req.getId() >= 1500) {
        throw new IdInvalidException(
            "ID is invalid, must be less than 1500");
    }

    User user = this.userService.handleUpdateUser(req);

    if(user == null) {
        throw new IdInvalidException(
            "User not found");
    }

    return ResponseEntity.ok(
        this.userService.resUpdateUserDTO(user));
}

    @GetMapping("/users/{id}")
    public ResponseEntity<ResUserDTO> getUserById(@PathVariable("id") Long id) throws IdInvalidException {
        User user = this.userService.handleFindUser(id);
        if(user == null) {
           throw new IdInvalidException("User with ID " + id + " does not exist");
        }
        
        return ResponseEntity.ok(this.userService.convertToResUserDTO(user));
    }

    @GetMapping("/users")
    @ApiMessage("Fetch all users")
    public ResponseEntity<ResultPaginationDTO> getAllUsers(  @Filter Specification<User> spec , Pageable pageable) {
               
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.fetchAllUsers(spec,pageable));
    }

}
