package com.humane.smps.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.humane.util.jackson.TimeSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ScoreDto {
    private String userAdmissions;
    private String admissionNm;
    private String typeNm;
    private String examNm;
    private String examineeCd;
    private String examineeNm;
    private String examCd;
    private String groupNm;
    private String headNm;
    private String bldgNm;
    private String hallNm;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Date examDate;

    @DateTimeFormat(pattern = "HH:mm")
    @JsonSerialize(using = TimeSerializer.class)
    private Date examTime;

    private String deptNm;
    private String majorNm;

    private String virtNo;
    private String sheetNo;
    private String scorerNm;

    private String score01;
    private String score02;
    private String score03;
    private String score04;
    private String score05;
    private String score06;
    private String score07;
    private String score08;
    private String score09;
    private String score10;

    private String avgScore;
    private String totalScore;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date scoreDttm;

    private String rescoreDttm;

    private String memo;
    private Boolean isPhoto;

    private String evalCd;
}
