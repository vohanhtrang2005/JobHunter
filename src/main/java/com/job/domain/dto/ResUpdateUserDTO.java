package com.job.domain.dto;

import java.time.Instant;

import com.job.util.constant.GenderEnum;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ResUpdateUserDTO {
    ResUpdateUserDTO res = new ResUpdateUserDTO();
    private long id;
    private String name;
    private String address;
    private int age;
    private GenderEnum gender;
    private Instant updatedAt;
}
