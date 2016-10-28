package com.humane.smps.form;

import com.blogspot.na5cent.exom.annotation.Column;
import lombok.Data;

@Data
public class FormExamineeVo {
    @Column(name = "수험번호") private String examineeCd;
    @Column(name = "수험생명") private String examineeNm;
    @Column(name = "생년월일") private String birth;
    @Column(name = "성별") private String gender;

    @Column(name = "전형코드") private String admissionCd;
    @Column(name = "학생지원전형") private String exmAdmNm;
    @Column(name = "시험코드") private String examCd;

    @Column(name = "단과대학") private String collegeNm;
    @Column(name = "계열") private String typeNm;
    @Column(name = "모집단위(학과)") private String deptNm;
    @Column(name = "전공") private String majorNm;
    @Column(name = "시험일자") private String examDate;
    @Column(name = "실제시험일") private String hallDate;
    @Column(name = "시험시간") private String examTime;
    @Column(name = "조") private String groupNm;

    @Column(name = "고사본부") private String headNm;
    @Column(name = "고사건물") private String bldgNm;
    @Column(name = "고사실") private String hallNm;
}
