package com.humane.smps.form;

import com.blogspot.na5cent.exom.annotation.Column;
import lombok.Data;

@Data
public class FormItemVo {
    @Column(name = "모집") private String recruitNm;
    @Column(name = "전형코드") private String admissionCd;
    @Column(name = "전형명") private String admissionNm;
    @Column(name = "계열") private String typeNm;
    @Column(name = "시험코드") private String examCd;
    @Column(name = "상위시험코드") private String fkExamCd; // 에리카 체육 등에 쓰임
    @Column(name = "시험명") private String examNm;
    @Column(name = "단계") private String period;
    @Column(name = "시험일자") private String examDate;

    @Column(name = "평가위원수") private String scorerCnt;
    @Column(name = "키패드") private String keypadType;
    @Column(name = "가번호 할당 방식") private String virtNoType;
    @Column(name = "총점") private String totScore;
    @Column(name = "결시버튼 사용여부") private Boolean isAbsence;
    @Column(name = "마감데이터 사용여부") private Boolean isClosedView;
    @Column(name = "가번호 자동여부") private Boolean isMgrAuto;
    @Column(name = "가번호 표시 자릿수") private String virtNoDigits;
    @Column(name = "수험번호 자릿수") private String examineeLen;

    @Column(name = "바코드타입") private String barcodeType;
    @Column(name = "조정점수") private String adjust;

    @Column(name = "평가표제목1") private String printTitle1;
    @Column(name = "평가표제목2") private String printTitle2;
    @Column(name = "평가표문구1") private String printContent1;
    @Column(name = "평가표문구2") private String printContent2;
    @Column(name = "평가표서명") private String printSign;

    @Column(name = "항목수") private String itemCnt;
    @Column(name = "항목1 코드") private String itemNo1;   @Column(name = "항목1 명") private String itemNm1;   @Column(name = "항목1 편차코드") private String deviCd1;     @Column(name = "항목1 키패드") private String keypadType1;
    @Column(name = "항목2 코드") private String itemNo2;   @Column(name = "항목2 명") private String itemNm2;   @Column(name = "항목2 편차코드") private String deviCd2;     @Column(name = "항목2 키패드") private String keypadType2;
    @Column(name = "항목3 코드") private String itemNo3;   @Column(name = "항목3 명") private String itemNm3;   @Column(name = "항목3 편차코드") private String deviCd3;     @Column(name = "항목3 키패드") private String keypadType3;
    @Column(name = "항목4 코드") private String itemNo4;   @Column(name = "항목4 명") private String itemNm4;   @Column(name = "항목4 편차코드") private String deviCd4;     @Column(name = "항목4 키패드") private String keypadType4;
    @Column(name = "항목5 코드") private String itemNo5;   @Column(name = "항목5 명") private String itemNm5;   @Column(name = "항목5 편차코드") private String deviCd5;     @Column(name = "항목5 키패드") private String keypadType5;
    @Column(name = "항목6 코드") private String itemNo6;   @Column(name = "항목6 명") private String itemNm6;   @Column(name = "항목6 편차코드") private String deviCd6;     @Column(name = "항목6 키패드") private String keypadType6;
    @Column(name = "항목7 코드") private String itemNo7;   @Column(name = "항목7 명") private String itemNm7;   @Column(name = "항목7 편차코드") private String deviCd7;     @Column(name = "항목7 키패드") private String keypadType7;
    @Column(name = "항목8 코드") private String itemNo8;   @Column(name = "항목8 명") private String itemNm8;   @Column(name = "항목8 편차코드") private String deviCd8;     @Column(name = "항목8 키패드") private String keypadType8;
    @Column(name = "항목9 코드") private String itemNo9;   @Column(name = "항목9 명") private String itemNm9;   @Column(name = "항목9 편차코드") private String deviCd9;     @Column(name = "항목9 키패드") private String keypadType9;
    @Column(name = "항목10 코드") private String itemNo10; @Column(name = "항목10 명") private String itemNm10; @Column(name = "항목10 편차코드") private String deviCd10;   @Column(name = "항목10 키패드") private String keypadType10;

    @Column(name = "항목1 최대점") private String maxScore1; @Column(name = "항목1 최소점") private String minScore1;
    @Column(name = "항목2 최대점") private String maxScore2; @Column(name = "항목2 최소점") private String minScore2;
    @Column(name = "항목3 최대점") private String maxScore3; @Column(name = "항목3 최소점") private String minScore3;
    @Column(name = "항목4 최대점") private String maxScore4; @Column(name = "항목4 최소점") private String minScore4;
    @Column(name = "항목5 최대점") private String maxScore5; @Column(name = "항목5 최소점") private String minScore5;
    @Column(name = "항목6 최대점") private String maxScore6; @Column(name = "항목6 최소점") private String minScore6;
    @Column(name = "항목7 최대점") private String maxScore7; @Column(name = "항목7 최소점") private String minScore7;
    @Column(name = "항목8 최대점") private String maxScore8; @Column(name = "항목8 최소점") private String minScore8;
    @Column(name = "항목9 최대점") private String maxScore9; @Column(name = "항목9 최소점") private String minScore9;
    @Column(name = "항목10 최대점") private String maxScore10; @Column(name = "항목10 최소점") private String minScore10;
}