package com.job.service;

import java.util.List;
import java.util.Optional;

import org.hibernate.query.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.job.domain.User;
import com.job.domain.dto.Meta;
import com.job.domain.dto.ResultPaginationDTO;
import com.job.reponsitory.UserRepository;


@Service
public class UserService {
    private final UserRepository userReponsitory;
    public UserService(UserRepository userReponsitory) {
        this.userReponsitory = userReponsitory;
    }
public User createUser(User user) {

    return userReponsitory.save(user);
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
 Meta mt = new Meta();
    mt.setPage(pageable.getPageNumber()+1);   
    mt.setPageSize(pageable.getPageSize());

    mt.setTotal(pageUser.getTotalElements());
    mt.setPages(pageUser.getTotalPages());
    rs.setMeta(mt);
    rs.setResult(pageUser.getContent());
    

    return rs;
}}