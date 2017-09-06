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
public class ExamInfoDto{
    // exam
    private String id;
    private String userAdmissions;
    private String admissionCd;
    private String admissionNm;

    private String typeNm;

    private String examCd;
    private String examNm;

    private BigDecimal adjust;
    private String barcodeType;
    private Long examineeLen;

    private boolean isAbsence;
    private boolean isClosedView;
    private boolean isHorizontal;
    private boolean isMgrAuto;
    private boolean isMove;

    private Long itemCnt;
    private String period;

    private String printContent1;
    private String printContent2;
    private String printSign;
    private String printTitle1;
    private String printTitle2;

    private Long scorerCnt;
    private Long totScore;

    private Long virtNoDigits;
    private String virtNoType;
    private String virtNoAssignType;

    private String fkExamCd;

    // exam_hall_date
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private String hallDate;

    private String virtNoEnd;
    private String virtNoStart;

    private String hallCd;
    private String headNm;
    private String bldgNm;
    private String hallNm;

    // 수정 시 비교용 (기존 데이터)
    private String _virtNoStart;
    private String _virtNoEnd;
    private String _hallCd;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private String _hallDate;

    private String itemNm;
    private String itemNo;
    private String maxScore;
    private String minScore;
    private String maxWarning;
    private String minWarning;
    private String keypadType;
    private String scoreMap;

    private String _itemNm;
    private String _itemNo;
}