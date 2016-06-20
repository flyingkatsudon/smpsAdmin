package com.humane.smps.form;

import com.blogspot.na5cent.exom.annotation.Column;
import lombok.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class FormHallVo {
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

    @Column(name = "고사실코드") private String hallCd;
    @Column(name = "고사본부") private String headNm;
    @Column(name = "고사건물") private String bldgNm;
    @Column(name = "고사실") private String hallNm;
    @Column(name = "예비고사실여부") private Boolean isEtc;
}
