package com.job.reponsitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.job.domain.Company;

@Repository
public interface CompanyReponsitory extends JpaRepository<Company, Long> {
    
    
}
