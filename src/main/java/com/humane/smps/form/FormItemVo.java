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
    @Column(name = "가번호 표시") private String virtNoType; // 가번호를 가번호 앱에서 뭐라고 나타낼지, 가번호? 실기번호?
    @Column(name = "가번호 관리") private String virtNoAssignType;
    @Column(name = "총점") private String totScore;
    @Column(name = "채점방향가로") private Boolean isHorizontal;
    @Column(name = "지정이동방식 사용") private Boolean isMove;
    @Column(name = "결시버튼 사용") private Boolean isAbsence;
    @Column(name = "결시값") private String absentValue;

    @Column(name = "마감데이터 사용") private Boolean isClosedView;
    @Column(name = "가번호 자동") private Boolean isMgrAuto;
    @Column(name = "가번호 표시 자릿수") private String virtNoDigits;
    @Column(name = "수험번호 자릿수") private String examineeLen;

    @Column(name = "바코드타입") private String barcodeType;
    @Column(name = "조정점수") private String adjust;
    @Column(name = "타이머 사용") private Boolean isTimer;

    @Column(name = "평가표제목1") private String printTitle1;
    @Column(name = "평가표제목2") private String printTitle2;
    @Column(name = "평가표문구1") private String printContent1;
    @Column(name = "평가표문구2") private String printContent2;
    @Column(name = "평가표서명") private String printSign;

    @Column(name = "항목1 점수코드") private String deviCd1;
    @Column(name = "항목2 점수코드") private String deviCd2;
    @Column(name = "항목3 점수코드") private String deviCd3;
    @Column(name = "항목4 점수코드") private String deviCd4;
    @Column(name = "항목5 점수코드") private String deviCd5;
    @Column(name = "항목6 점수코드") private String deviCd6;
    @Column(name = "항목7 점수코드") private String deviCd7;
    @Column(name = "항목8 점수코드") private String deviCd8;
    @Column(name = "항목9 점수코드") private String deviCd9;
    @Column(name = "항목10 점수코드") private String deviCd10;

    @Column(name = "항목수") private String itemCnt;
    @Column(name = "항목1 순서") private String itemNo1;        @Column(name = "항목1 명") private String itemNm1;          @Column(name = "항목1 키패드") private String keypadType1;
    @Column(name = "항목2 순서") private String itemNo2;        @Column(name = "항목2 명") private String itemNm2;          @Column(name = "항목2 키패드") private String keypadType2;
    @Column(name = "항목3 순서") private String itemNo3;        @Column(name = "항목3 명") private String itemNm3;          @Column(name = "항목3 키패드") private String keypadType3;
    @Column(name = "항목4 순서") private String itemNo4;        @Column(name = "항목4 명") private String itemNm4;          @Column(name = "항목4 키패드") private String keypadType4;
    @Column(name = "항목5 순서") private String itemNo5;        @Column(name = "항목5 명") private String itemNm5;          @Column(name = "항목5 키패드") private String keypadType5;
    @Column(name = "항목6 순서") private String itemNo6;        @Column(name = "항목6 명") private String itemNm6;          @Column(name = "항목6 키패드") private String keypadType6;
    @Column(name = "항목7 순서") private String itemNo7;        @Column(name = "항목7 명") private String itemNm7;          @Column(name = "항목7 키패드") private String keypadType7;
    @Column(name = "항목8 순서") private String itemNo8;        @Column(name = "항목8 명") private String itemNm8;          @Column(name = "항목8 키패드") private String keypadType8;
    @Column(name = "항목9 순서") private String itemNo9;        @Column(name = "항목9 명") private String itemNm9;          @Column(name = "항목9 키패드") private String keypadType9;
    @Column(name = "항목10 순서") private String itemNo10;      @Column(name = "항목10 명") private String itemNm10;        @Column(name = "항목10 키패드") private String keypadType10;

    @Column(name = "항목1 최댓점") private String maxScore1;     @Column(name = "항목1 최솟점") private String minScore1;     @Column(name = "항목1 매핑") private String scoreMap1;
    @Column(name = "항목2 최댓점") private String maxScore2;     @Column(name = "항목2 최솟점") private String minScore2;     @Column(name = "항목2 매핑") private String scoreMap2;
    @Column(name = "항목3 최댓점") private String maxScore3;     @Column(name = "항목3 최솟점") private String minScore3;     @Column(name = "항목3 매핑") private String scoreMap3;
    @Column(name = "항목4 최댓점") private String maxScore4;     @Column(name = "항목4 최솟점") private String minScore4;     @Column(name = "항목4 매핑") private String scoreMap4;
    @Column(name = "항목5 최댓점") private String maxScore5;     @Column(name = "항목5 최솟점") private String minScore5;     @Column(name = "항목5 매핑") private String scoreMap5;
    @Column(name = "항목6 최댓점") private String maxScore6;     @Column(name = "항목6 최솟점") private String minScore6;     @Column(name = "항목6 매핑") private String scoreMap6;
    @Column(name = "항목7 최댓점") private String maxScore7;     @Column(name = "항목7 최솟점") private String minScore7;     @Column(name = "항목7 매핑") private String scoreMap7;
    @Column(name = "항목8 최댓점") private String maxScore8;     @Column(name = "항목8 최솟점") private String minScore8;     @Column(name = "항목8 매핑") private String scoreMap8;
    @Column(name = "항목9 최댓점") private String maxScore9;     @Column(name = "항목9 최솟점") private String minScore9;     @Column(name = "항목9 매핑") private String scoreMap9;
    @Column(name = "항목10 최댓점") private String maxScore10;   @Column(name = "항목10 최솟점") private String minScore10;   @Column(name = "항목10 매핑") private String scoreMap10;
}