package com.job.reponsitory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.job.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

    
    User findByEmail(String email);
    
}
