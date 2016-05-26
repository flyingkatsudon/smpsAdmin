package com.humane.smps.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Examinee {
    @Id private String examineeCd;
    private String examineeNm;
    private String deptNm;
    private String majorNm;
    private String birth;
}