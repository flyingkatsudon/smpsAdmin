package com.humane.smps.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Examinee {
    @Id private String examineeCd;
    private String examineeNm;
    private String collegeNm;
    private String deptNm;
    private String majorNm;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @Temporal(TemporalType.DATE)
    private Date birth;
    private String neisCd;
    private String neisNm;
    private String exmAdmNm; // 학생부전형
}