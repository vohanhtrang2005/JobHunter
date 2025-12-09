package com.job.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.job.domain.Company;
import com.job.service.CompanyService;

import org.springframework.web.bind.annotation.RequestBody;   

import jakarta.validation.Valid;

@RestController
public class CompanyController {
   private final CompanyService companyService;
    public CompanyController(CompanyService companyService) {
         this.companyService = companyService;
    }
     @PostMapping("/companies")
     public ResponseEntity<?> createCompany(@Valid @RequestBody Company reqCompany) {
         return ResponseEntity.status(HttpStatus.CREATED).body(this.companyService.handleCreateService(reqCompany));
     }
}
