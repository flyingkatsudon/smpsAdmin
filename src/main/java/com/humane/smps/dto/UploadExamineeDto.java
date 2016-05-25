package com.humane.smps.dto;

import com.blogspot.na5cent.exom.annotation.Column;
import lombok.Data;

@Data
public class UploadExamineeDto {
    @Column(name = "수험번호") private String examineeCd;
    @Column(name = "일련번호") private String unknown1;
    @Column(name = "수험생명") private String examineeNm;
    @Column(name = "생년월일") private String birth;
    @Column(name = "전형") private String admissionCd;
    @Column(name = "단과대학") private String collegeCd;
    @Column(name = "모집단위") private String deptNm;
    @Column(name = "전공") private String majorNm;
    @Column(name = "시험일자") private String examDate;
    @Column(name = "시험시간") private String examTime;

    @Column(name = "고사본부") private String headNm;
    @Column(name = "고사건물") private String bldgNm;
    @Column(name = "고사실") private String hallNm;

    @Column(name = "출신고교코드") private String unknown2;
    @Column(name = "출신고교명") private String unknown3;
}
