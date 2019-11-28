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
public class ExamineeDto extends BasicDto{

    private BasicDto basicDto;

    private String virtNo;

    private Boolean isVirtNo;
    private Boolean isAttend;

    private String collegeNm;
    
    @DateTimeFormat(pattern = "yyyy년 MM월 dd일 HH시 mm분 ss초")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy년 MM월 dd일 HH시 mm분 ss초", timezone = "Asia/Seoul")
    private Date printDttm;

    @DateTimeFormat(pattern = "HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date scanDttm;

    private String attendance;
    private String exmAdmNm;

    private String mainTitle;
    private String subTitle;

    private String examNm;

    private int rowNum;

}
