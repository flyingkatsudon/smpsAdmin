package com.humane.admin.smps.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.awt.image.BufferedImage;
import java.util.Date;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExamineeDto extends StatusDto {
    private String examineeCd;
    private String examineeNm;
    private String paperCd;
    private Boolean isAttend;
    private Boolean isNoIdCard;
    private Boolean isChangePaper;
    private String attendHeadNm;
    private String attendBldgNm;
    private String attendHallNm;
    private Boolean isOtherHall;
    private String birth;
    private Boolean isRecheck;

    private String firstPaperCd;
    private String lastPaperCd;
    private Long paperCnt;
    private String paperList;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date lastDttm;

    // 검색용
    private String fromExamineeCd;
    private String toExamineeCd;
    private String memo;
    private Date checkTime;

    private BufferedImage examineeImage;
}
