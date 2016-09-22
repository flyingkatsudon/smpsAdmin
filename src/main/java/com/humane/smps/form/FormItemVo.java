package com.humane.smps.form;

import com.blogspot.na5cent.exom.annotation.Column;
import lombok.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class FormItemVo {
    @Column(name = "모집") private String recruitNm;
    @Column(name = "전형코드") private String admissionCd;
    @Column(name = "전형명") private String admissionNm;
    @Column(name = "계열") private String typeNm;
    @Column(name = "시험코드") private String examCd;
    @Column(name = "시험명") private String examNm;
    @Column(name = "시험일자") private String examDate;
    @Column(name = "시험시간") private String examTime;

    public String getExamTime() {
        try {
            Date date = new SimpleDateFormat("YYYY-mm-dd HH:mm:ss").parse(examTime);
            return new SimpleDateFormat("HH:mm:ss").format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return examTime;
    }

    @Column(name = "평가위원수") private String scorerCnt;
    @Column(name = "키패드") private String keypadType;
    @Column(name = "가번호 시작") private String virtNoStart;
    @Column(name = "가번호 종료") private String virtNoEnd;
    @Column(name = "가번호 헤더") private String virtNoHead;
    @Column(name = "가번호 자릿수") private String virtNoLen;
    @Column(name = "가번호 할당 방식") private String virtNoType;
    @Column(name = "총점") private String totScore;
    @Column(name = "답안지 자릿수") private String paperLen;
    //@Column(name = "가번호 자동여부") private String isMgrAuto;
    @Column(name = "수험번호 자릿수") private String examineeLen;

    @Column(name = "항목수") private String itemCnt;
    @Column(name = "항목1 코드") private String itemNo1;   @Column(name = "항목1 명") private String itemNm1;   @Column(name = "항목1 편차코드") private String deviCd1;
    @Column(name = "항목2 코드") private String itemNo2;   @Column(name = "항목2 명") private String itemNm2;   @Column(name = "항목2 편차코드") private String deviCd2;
    @Column(name = "항목3 코드") private String itemNo3;   @Column(name = "항목3 명") private String itemNm3;   @Column(name = "항목3 편차코드") private String deviCd3;
    @Column(name = "항목4 코드") private String itemNo4;   @Column(name = "항목4 명") private String itemNm4;   @Column(name = "항목4 편차코드") private String deviCd4;
    @Column(name = "항목5 코드") private String itemNo5;   @Column(name = "항목5 명") private String itemNm5;   @Column(name = "항목5 편차코드") private String deviCd5;
    @Column(name = "항목6 코드") private String itemNo6;   @Column(name = "항목6 명") private String itemNm6;   @Column(name = "항목6 편차코드") private String deviCd6;
    @Column(name = "항목7 코드") private String itemNo7;   @Column(name = "항목7 명") private String itemNm7;   @Column(name = "항목7 편차코드") private String deviCd7;
    @Column(name = "항목8 코드") private String itemNo8;   @Column(name = "항목8 명") private String itemNm8;   @Column(name = "항목8 편차코드") private String deviCd8;
    @Column(name = "항목9 코드") private String itemNo9;   @Column(name = "항목9 명") private String itemNm9;   @Column(name = "항목9 편차코드") private String deviCd9;
    @Column(name = "항목10 코드") private String itemNo10; @Column(name = "항목10 명") private String itemNm10; @Column(name = "항목10 편차코드") private String deviCd10;

}