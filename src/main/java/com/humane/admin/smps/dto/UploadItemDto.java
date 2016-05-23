package com.humane.admin.smps.dto;

import com.blogspot.na5cent.exom.annotation.Column;
import lombok.Data;

@Data
public class UploadItemDto {
    @Column(name = "학교명") private String univNm;
    @Column(name = "학교코드") private String univCd;
    @Column(name = "전형2") private String unknown1;
    @Column(name = "전형") private String admissionCd;

    @Column(name = "단과대학") private String collegeNm;
    @Column(name = "모집단위") private String deptNm;
    @Column(name = "전공") private String majorNm;
    @Column(name = "세부전공") private String unknown2;

    @Column(name = "시험코드") private String examCd;
    @Column(name = "상위시험코드") private String unknown3;
    @Column(name = "시험명") private String examNm;
    @Column(name = "단계") private String unknown4;
    @Column(name = "가로세로") private String unknown5;
    @Column(name = "시험일자") private String examDate;
    @Column(name = "시험시간") private String examTime;
    @Column(name = "시험종료일") private String unknown6;
    @Column(name = "항목수") private Integer itemCnt;
    @Column(name = "총점") private Long totalScore;
    @Column(name = "평가위원수") private Integer scorerCnt;
    @Column(name = "키패드") private String keypadType;
    @Column(name = "가번호 시작") private String virtNoStart;
    @Column(name = "가번호 종료") private String virtNoEnd;
    @Column(name = "헤더") private String virtHeader;
    @Column(name = "번호부여방식") private String unknown7;
    @Column(name = "마감데이터 사용여부") private String unknown8;
    @Column(name = "지정이동방식") private String unknown9;
    @Column(name = "결시버튼 사용여부") private String unknown10;

    @Column(name = "항목코드1") private String itemNo1; @Column(name = "항목명1") private String itemNm1; @Column(name = "항목명1 최대점수") private String totScore1;
    @Column(name = "항목코드2") private String itemNo2; @Column(name = "항목명2") private String itemNm2; @Column(name = "항목명2 최대점수") private String totScore2;
    @Column(name = "항목코드3") private String itemNo3; @Column(name = "항목명3") private String itemNm3; @Column(name = "항목명3 최대점수") private String totScore3;
    @Column(name = "항목코드4") private String itemNo4; @Column(name = "항목명4") private String itemNm4; @Column(name = "항목명4 최대점수") private String totScore4;
    @Column(name = "항목코드5") private String itemNo5; @Column(name = "항목명5") private String itemNm5; @Column(name = "항목명5 최대점수") private String totScore5;
    @Column(name = "항목코드6") private String itemNo6; @Column(name = "항목명6") private String itemNm6; @Column(name = "항목명6 최대점수") private String totScore6;
    @Column(name = "항목코드7") private String itemNo7; @Column(name = "항목명7") private String itemNm7; @Column(name = "항목명7 최대점수") private String totScore7;
    @Column(name = "항목코드8") private String itemNo8; @Column(name = "항목명8") private String itemNm8; @Column(name = "항목명8 최대점수") private String totScore8;
    @Column(name = "항목코드9") private String itemNo9; @Column(name = "항목명9") private String itemNm9; @Column(name = "항목명9 최대점수") private String totScore9;
    @Column(name = "항목코드10") private String itemNo10; @Column(name = "항목명10") private String itemNm10; @Column(name = "항목명10 최대점수") private String totScore10;

}