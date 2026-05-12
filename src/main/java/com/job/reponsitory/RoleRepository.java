package com.job.reponsitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<com.job.domain.Role, Long>,
                JpaSpecificationExecutor<com.job.domain.Role> {
        boolean existsByName(String name);
}
