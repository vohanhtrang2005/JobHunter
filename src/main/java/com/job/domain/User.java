package com.job.domain;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.job.util.SecurityUtil;
import com.job.util.constant.GenderEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên không được để trống")
    @Size(min = 2, max = 50, message = "Tên phải từ 2 đến 50 ký tự")
    private String name;

    @NotBlank(message = "Email không được để trống")    
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String password;

    private int age;

    @Enumerated(EnumType.STRING)
    private GenderEnum gender;
private String address;

@Column(columnDefinition = "MEDIUMTEXT")
private String refreshToken;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
       @Column(name = "create_at")
private Instant createdAt;
private Instant updatedAt;
private String createBy;
private String updateBy;

@ManyToOne
@JoinColumn(name = "company_id")
private Company company;

      @PrePersist
       public void handleBeforeCreate() {
         this.createBy = SecurityUtil.getCurrentUserLogin().orElse("");
       this.createdAt=Instant.now();
       }
}
