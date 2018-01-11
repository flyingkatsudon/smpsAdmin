package com.humane.smps.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ScoreDto extends BasicDto{

    private BasicDto basicDto;

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
   // private BigDecimal totalScore; // 법대용
    private String totalScore;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date scoreDttm;

    private String rescoreDttm;

    private String memo;
    private Boolean isPhoto;

    private String evalCd;

    private Boolean isVirtNo;

    private String grade01;
    private String grade02;
    private String grade03;
    private String grade04;
    private String grade05;

    private Long updateCnt;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Date birth;
}
