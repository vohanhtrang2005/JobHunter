package com.job.controller;

import java.util.List;
import java.util.Optional;

import org.hibernate.query.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.job.domain.Company;
import com.job.domain.dto.ResultPaginationDTO;
import com.job.service.CompanyService;
import com.turkraft.springfilter.boot.Filter;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;

@RestController
public class CompanyController {
   private final CompanyService companyService;
    public CompanyController(CompanyService companyService) {
         this.companyService = companyService;
    }
     @PostMapping("/companies")
     public ResponseEntity<Company> createCompany(@Valid @RequestBody Company reqCompany) {
         return ResponseEntity.status(HttpStatus.CREATED).body(this.companyService.handleCreateService(reqCompany));
     }
     @GetMapping("companies")
     public ResponseEntity<ResultPaginationDTO> getCompany(@Filter Specification spec, Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(this.companyService.handleGetCompany(spec,pageable));
    }
               
    
     
     @PutMapping("/companies")
     public ResponseEntity<Company> updateCompany(@Valid @RequestBody Company reqCompany) {
          Company updatedCompany = this.companyService.handleUpdateCompany(reqCompany);
          return ResponseEntity.ok(updatedCompany);
     }
     @DeleteMapping("/companies/{id}")
     public ResponseEntity<Void> deleteCompany(@PathVariable("id") Long id) {
          this.companyService.handleDeleteCompany(id);
          return ResponseEntity.ok(null);
     }
}
