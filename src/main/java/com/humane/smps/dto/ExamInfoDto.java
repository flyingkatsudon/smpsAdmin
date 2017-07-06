package com.humane.smps.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ExamInfoDto {
    // exam
    public String examCd;
    public BigDecimal adjust;
    public String barcodeType;
    public String examNm;
    public Long examineeLen;

    public boolean isAbsence;
    public boolean isClosedView;
    public boolean isHorizontal;
    public boolean isMgrAuto;
    public boolean isMove;

    public Long itemCnt;
    public String keypadType;
    public String period;

    public String printContent1;
    public String printContent2;
    public String printSign;
    public String printTitle1;
    public String printTitle2;

    public Long scorerCnt;
    public Long totScore;
    public String typeNm;

    public Long virtNoDigits;
    public String virtNoType;

    public String fkExamCd;

    // admission
    public String admissionNm;

    // exam_hall_date
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    public String hallDate;
    public String virtNoEnd;
    public String virtNoStart;
    public String hallCd;

    // hall
    public String headNm;
    public String bldgNm;
    public String hallNm;
}