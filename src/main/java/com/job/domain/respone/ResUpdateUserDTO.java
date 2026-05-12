package com.job.domain.respone;

import java.time.Instant;

import com.job.util.constant.GenderEnum;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ResUpdateUserDTO {
   
    private long id;
    private String name;
    private String address;
    private int age;
    private GenderEnum gender;
    private Instant updatedAt;
    private CompanyUser company;
        private RoleUser role;
    

@Getter
@Setter
public static class CompanyUser {
    private long id;
    private String name;
}

@Getter
@Setter
public static class RoleUser {
    private long id;
    private String name;
}
}
