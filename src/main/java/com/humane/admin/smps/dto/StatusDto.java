package com.humane.admin.smps.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.humane.util.jackson.PercentSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatusDto implements Serializable {
    private String admissionNm;
    private String attendNm;
    private String majorNm;
    private String deptNm;
    private String typeNm;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Date attendDate;

    @DateTimeFormat(pattern = "HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss", timezone = "Asia/Seoul")
    private Date attendTime;
    private String hallCd;
    private String headNm;
    private String bldgNm;
    private String hallNm;
    private Long examineeCnt;
    private Long otherHallCnt;
    private Long attendCnt;
    private Long absentCnt;
    @JsonSerialize(using = PercentSerializer.class)
    private BigDecimal attendPer;
    @JsonSerialize(using = PercentSerializer.class)
    private BigDecimal absentPer;
    private String groupNm;
    private Boolean isSend;
    private Boolean isEtc;
    private Boolean isSign;
    private Long deviceNo;
}