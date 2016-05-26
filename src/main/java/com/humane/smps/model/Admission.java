package com.humane.smps.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Admission {
    @Id private String admissionCd;
    private String admissionNm;
    private String recruitNm;
}
