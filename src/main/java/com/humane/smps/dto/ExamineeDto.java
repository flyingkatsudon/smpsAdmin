package com.humane.smps.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.humane.util.jackson.TimeSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.awt.image.BufferedImage;
import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ExamineeDto {
    private String admissionNm;
    private String typeNm;
    private String examNm;
    private String deptNm;
    private String majorNm;
    private String groupNm;
    private String headNm;
    private String bldgNm;
    private String hallNm;

    private String examineeCd;
    private String examineeNm;
    private String virtNo;
    private Long scorerCnt;

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

    private String avgScore01;
    private String avgScore02;
    private String avgScore03;
    private String avgScore04;
    private String avgScore05;
    private String avgScore06;
    private String avgScore07;
    private String avgScore08;
    private String avgScore09;
    private String avgScore10;

    private String totScore01;
    private String totScore02;
    private String totScore03;
    private String totScore04;
    private String totScore05;
    private String totScore06;
    private String totScore07;
    private String totScore08;
    private String totScore09;
    private String totScore10;

    private String totalScore;
    private Boolean isAttend;
    private Boolean isCancel;

    private Long itemCnt;
    private Long scoredCnt;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Date examDate;

    @DateTimeFormat(pattern = "HH:mm")
    @JsonSerialize(using = TimeSerializer.class)
    private Date examTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date regDttm;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date cancelDttm;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date scoreDttm;

    private BufferedImage examineeImage;
    private String fromExamineeCd;
    private String toExamineeCd;

    private String collegeNm;
    
    @DateTimeFormat(pattern = "yyyy년 MM월 dd일 HH시 mm분 ss초")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy년 MM월 dd일 HH시 mm분 ss초", timezone = "Asia/Seoul")
    private Date printDttm;

    private BufferedImage univLogo;
}
