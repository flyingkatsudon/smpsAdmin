package com.humane.smps.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class physicalDto extends BasicDto{

    private String virtNo;

    private Long scorerCnt;

    private String examTime;
    private String examCd1;

    private String scorerNm11;
    private String scorerNm12;

    private String scorerNm21;
    private String scorerNm22;

    private String scorerNm31;
    private String scorerNm32;

    private String scorerNm41;
    private String scorerNm42;

    private String score11;
    private String score12;
    private String score21;
    private String score22;

    private String total01;
    private String total02;
    private String total03;
    private String total04;

    private String grade01;
    private String grade02;
    private String grade03;
    private String grade04;

    private Long itemCnt;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date scoreDttm1;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date scoreDttm2;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date scoreDttm3;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date scoreDttm4;

    private Boolean isVirtNo;
}
