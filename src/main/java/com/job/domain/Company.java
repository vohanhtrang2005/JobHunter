package com.job.domain;

import java.time.Instant;

import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
        @Generated(value = "GenerationType.IDENTITY")
        private Long id;
        @NotBlank(message="Company name is required")
        private String name;
        private String address;
        @Column(columnDefinition = "MEDIUMTEXT")
        private String description;
        private String logo;
        private Instant createAt;
        private Instant updateAt;
        private String updatedBy;
        private String createdBy;
       
    }
    

