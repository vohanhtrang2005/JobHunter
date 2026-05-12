package com.job.domain.request;

import com.job.util.constant.GenderEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqUpdateUserDTO {

    private Long id;

    private String name;

    private Integer age;

    private String address;

    private GenderEnum gender;

    private Long companyId;

    private Long roleId;
}