package com.humane.admin.smps.dto;

import com.blogspot.na5cent.exom.annotation.Column;
import lombok.Data;

@Data
public class UploadHallDto {
    @Column(name = "학교명") private String univNm;
    @Column(name = "학교코드") private String univCd;

    @Column(name = "시험코드") private String examCd;
    @Column(name = "시험일자") private String examDate;
    @Column(name = "시험시간") private String examTime;

    @Column(name = "고사본부") private String headNm;
    @Column(name = "고사건물") private String bldgNm;
    @Column(name = "고사실") private String hallNm;
}
