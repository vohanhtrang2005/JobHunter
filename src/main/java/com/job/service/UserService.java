package com.job.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.apache.coyote.BadRequestException;
import org.hibernate.query.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.job.domain.Company;
import com.job.domain.User;
import com.job.domain.respone.ResCreateUserDTO;
import com.job.domain.respone.ResUpdateUserDTO;
import com.job.domain.respone.ResUserDTO;
import com.job.domain.respone.ResultPaginationDTO;
import com.job.reponsitory.UserRepository;
import com.job.util.constant.GenderEnum;
import com.job.util.error.IdInvalidException;


@Service
public class UserService {
    private final UserRepository userReponsitory;
        private final CompanyService companyService;
    public UserService(UserRepository userReponsitory, CompanyService companyService) {
         this.companyService = companyService;
        this.userReponsitory = userReponsitory;
    }
public User createUser(User user) throws IdInvalidException {
      boolean isEmailExist = this.isEmailExist(user.getEmail());
        if (isEmailExist) {
            throw new IdInvalidException("Email already exists");
        }
    
    //check company
  Company company = this.companyService
            .handleFindCompany(user.getCompany().getId())
            .orElseThrow(() -> new IdInvalidException("Company not found"));

        user.setCompany(company);
   
  return userReponsitory.save(user);
}

public ResCreateUserDTO convertToResCreateUserDTO(User user) {
    ResCreateUserDTO userDTO = new ResCreateUserDTO();
    userDTO.setAddress(user.getAddress());
    userDTO.setAge(user.getAge());
    userDTO.setCreatedAt(user.getCreatedAt());
    userDTO.setId(user.getId());
    userDTO.setGender(user.getGender());
    userDTO.setName(user.getName());
    userDTO.setEmail(user.getEmail());

  ResCreateUserDTO.CompanyUser companyUser = new ResCreateUserDTO.CompanyUser();

companyUser.setId(user.getCompany().getId());
companyUser.setName(user.getCompany().getName());

// gán vào DTO
userDTO.setCompany(companyUser);
   


    
    
    return userDTO;
}
public boolean isEmailExist(String email) {
    return userReponsitory.existsByEmail(email);
}

public void handleDeleteUser(Long id) {
    userReponsitory.deleteById(id);
}
public User handleFindUser(Long id) {
    Optional<User> user = userReponsitory.findById(id);
    if(user.isPresent()) {
        return user.get();
    } 
return null;

}
public User handleUpdateUser(User user) {
    User currentUser = handleFindUser(user.getId());
    if(currentUser != null) {
        currentUser.setName(user.getName());
        currentUser.setEmail(user.getEmail());
        currentUser.setPassword(user.getPassword());
        return userReponsitory.save(currentUser);
    }  
    return null;   
}
public List<User> handleFindAllUsers() {
    return userReponsitory.findAll();
}
public User handleGetUserByUsername(String username) {
    return userReponsitory.findByEmail(username);
}
public ResultPaginationDTO fetchAllUsers(Specification spec, Pageable pageable) {
  org.springframework.data.domain.Page<User> pageUser = userReponsitory.findAll(spec, pageable);
    ResultPaginationDTO rs = new ResultPaginationDTO();
    ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
    mt.setPage(pageable.getPageNumber()+1);   
    mt.setPageSize(pageable.getPageSize());

    mt.setTotal(pageUser.getTotalElements());
    mt.setPages(pageUser.getTotalPages());
    rs.setMeta(mt);


  List<ResUserDTO> listUserDTO = pageUser.getContent().stream().map(u -> convertToResUserDTO(u)).toList();


    rs.setResult(listUserDTO);
    

    return rs;
}
public User fetchUserById(Long id) {
    Optional<User> user = userReponsitory.findById(id);
    if(user.isPresent()) {
        return user.get();
    }
    return null;

}


public ResUpdateUserDTO resUpdateUserDTO (User user) {
    ResUpdateUserDTO userDTO = new ResUpdateUserDTO();
    userDTO.setAddress(user.getAddress());
    userDTO.setAge(user.getAge());
    
    userDTO.setId(user.getId());
    userDTO.setGender(user.getGender());
    userDTO.setName(user.getName());

    userDTO.setUpdatedAt(user.getUpdatedAt());
    return userDTO;
}
public ResUserDTO convertToResUserDTO(User user) {
    ResUserDTO userDTO = new ResUserDTO();
    userDTO.setAddress(user.getAddress());
    userDTO.setAge(user.getAge());
    userDTO.setCreatedAt(user.getCreatedAt());
    userDTO.setId(user.getId());
    userDTO.setGender(user.getGender());
    userDTO.setName(user.getName());
    userDTO.setEmail(user.getEmail());
    userDTO.setUpdatedAt(user.getUpdatedAt());

if(user.getCompany() == null) {
 userDTO.setCompany(null);
}
else{
    ResUserDTO.CompanyUser companyUser = new ResUserDTO.CompanyUser();
companyUser.setId(user.getCompany().getId());
companyUser.setName(user.getCompany().getName());
    userDTO.setCompany(companyUser); }
    return userDTO;
} 
public void updateUserToken(String token, String email){
    User currentUser = this.handleGetUserByUsername(email);
    if(currentUser != null){
        currentUser.setRefreshToken(token);
        this.userReponsitory.save(currentUser);
    }
}
public User findByRefreshTokenAndEmail(String refreshToken, String email) {
    return this.userReponsitory.findByRefreshTokenAndEmail(refreshToken, email);
}}