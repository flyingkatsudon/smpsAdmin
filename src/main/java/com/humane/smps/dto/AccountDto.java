package com.humane.smps.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AccountDto implements Serializable {
    private String userAdmissions;
    private String admissionCd;
    private String admissionNm;

    private String userId;
    private String password;

    private String role;
    private String roleName;
    private String admissions;
}
