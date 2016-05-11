package com.humane.admin.smps.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.humane.util.jackson.PercentSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatusDto implements Serializable {
    private String admissionNm;
    private String examNm;
    private String majorNm;
    private String deptNm;
    private String headNm;
    private String bldgNm;
    private String hallNm;

    private Long examineeCnt;
    private Long attendCnt;
    private Long absentCnt;

    @JsonSerialize(using = PercentSerializer.class)
    private BigDecimal attendPer;

    @JsonSerialize(using = PercentSerializer.class)
    private BigDecimal absentPer;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Date examDate;

    @DateTimeFormat(pattern = "HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss", timezone = "Asia/Seoul")
    private Date examTime;


    /* examinee */
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

    private String totalScore;
    private Boolean isAttend;
    private Boolean isCancel;
    private BufferedImage examineeImage;
}