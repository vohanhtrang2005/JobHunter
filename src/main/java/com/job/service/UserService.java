package com.job.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.job.domain.User;
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
}