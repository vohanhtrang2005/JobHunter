package com.job.domain;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.job.util.SecurityUtil;

import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "companies")
@Setter
@Getter
public class Company {
        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @NotBlank(message="Company name is required")
        private String name;
        private String address;
        @Column(columnDefinition = "MEDIUMTEXT")
        private String description;
        private String logo;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
       @Column(name = "create_at")
    private Instant createAt;

    @Column(name = "update_at")
    private Instant updateAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

        @PrePersist
       public void handleBeforeCreate() {
             this.createdBy = SecurityUtil.getCurrentUserLogin().orElse("");




        this.createAt=Instant.now();
       }
    } 
    

