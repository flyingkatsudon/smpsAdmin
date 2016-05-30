package com.humane.smps.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.humane.util.jackson.PercentSerializer;
import com.humane.util.jackson.TimeSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class StatusGroupDto {
    private String admissionNm;
    private String examNm;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Date examDate;

    @DateTimeFormat(pattern = "HH:mm:ss")
    @JsonSerialize(using = TimeSerializer.class)
    private Date examTime;

    private String majorNm;
    private String headNm;
    private String bldgNm;
    private String hallNm;
    //private String groupNm;

    private Long examineeCnt;
    private Long attendCnt;
    private Long absentCnt;

    @JsonSerialize(using = PercentSerializer.class)
    private BigDecimal attendPer;

    @JsonSerialize(using = PercentSerializer.class)
    private BigDecimal absentPer;
}
