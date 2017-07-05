package com.humane.smps.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ExamInfoDto {
    // exam
    public String examCd;
    public String adjust;
    public String barcodeType;
    public String examNm;
    public String examineeLen;

    public String isAbsence;
    public String isClosedView;
    public String isHirizontal;
    public String isMgrAuto;
    public String isMove;

    public String itemCnt;
    public String keypadType;
    public String period;

    public String printContent1;
    public String printContent2;
    public String printSign;
    public String printTitle1;
    public String printTitle2;

    public String scorerCnt;
    public String totScore;
    public String typeNm;

    public String virtNoDigits;
    public String virtNoType;

    public String fkExamCd;

    // admission
    public String admissionNm;

    // exam_hall_date
    public String hallDate;
    public String virtNoEnd;
    public String virtNoStart;
    public String hallCd;

    // hall
    public String headNm;
    public String bldgNm;
    public String hallNm;
}