package com.job.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.job.domain.Company;
import com.job.reponsitory.CompanyReponsitory;

import jakarta.persistence.PreUpdate;

@Service
public class CompanyService {
    private final CompanyReponsitory companyRepository;
    public CompanyService(CompanyReponsitory companyRepository) {
        this.companyRepository = companyRepository;
    }
    public Company handleCreateService(Company reqCompany) {
        return companyRepository.save(reqCompany);
    }
    public List<Company> handleGetCompany() {
        return companyRepository.findAll();
    }
    @PreUpdate
    public Company handleUpdateCompany(Company c){
        Optional<Company> companyOptional = this.companyRepository.findById(c.getId());
        if(companyOptional.isPresent()){
            Company existingCompany = companyOptional.get();
            existingCompany.setName(c.getName());
            existingCompany.setAddress(c.getAddress());
            existingCompany.setDescription(c.getDescription());
            existingCompany.setLogo(c.getLogo());
            return this.companyRepository.save(existingCompany);
        } else {
            return null;
          }
    }
    public void handleDeleteCompany(Long id) {
        this.companyRepository.deleteById(id);
    }
}
