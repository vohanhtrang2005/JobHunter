package com.job.service;

import org.springframework.stereotype.Service;

import com.job.domain.Company;
import com.job.reponsitory.CompanyReponsitory;

@Service
public class CompanyService {
    private final CompanyReponsitory companyRepository;
    public CompanyService(CompanyReponsitory companyRepository) {
        this.companyRepository = companyRepository;
    }
    public Company handleCreateService(Company reqCompany) {
        return companyRepository.save(reqCompany);
    }
}
