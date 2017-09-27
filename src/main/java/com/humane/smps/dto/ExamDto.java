package com.humane.smps.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ExamDto {
    public String admissionCd;
    public String admissionNm;

    public String lastVirtNo;
    public Long virtNoCnt;
    public Long examineeCnt;
    public Long attendCnt;

    public String examCd;
    public String examNm;
    public String hallCd;
}