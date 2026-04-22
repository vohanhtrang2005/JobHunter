package com.job.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.job.domain.Company;
import com.job.domain.respone.ResultPaginationDTO;
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
    public ResultPaginationDTO handleGetCompany(Specification<Company> spec, Pageable pageable) {
        Page<Company> pageCompany = companyRepository.findAll(spec,pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber()+1);
        mt.setPageSize(pageable.getPageSize());
        mt.setTotal(pageCompany.getTotalElements());
        mt.setPages(pageCompany.getTotalPages());
        rs.setMeta(mt);
        rs.setResult(pageCompany.getContent());
        return rs;
     
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
    public Optional<Company> handleFindCompany(Long id) {
        return companyRepository.findById(id);
    }

    }


