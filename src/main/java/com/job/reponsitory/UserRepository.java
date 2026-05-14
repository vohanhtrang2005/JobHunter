package com.job.reponsitory;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;

import com.job.domain.User;
import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

   @EntityGraph(attributePaths = {"role", "role.permissions"}) 
    User findByEmail(String email);
    
    Optional<User>  findById(Long id);
    
    boolean existsByEmail(String email);
    User findByRefreshTokenAndEmail(String refreshToken, String email);
    
}
