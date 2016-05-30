package com.humane.smps.form;

import com.blogspot.na5cent.exom.annotation.Column;
import lombok.Data;

@Data
public class FormDeviVo {
    @Column(name = "편차코드") private String deviCd;
    @Column(name = "상위편차코드") private String fkDeviCd;
    @Column(name = "편차명") private String deviNm;
    @Column(name = "최소점수") private String minScore;
    @Column(name = "최대점수") private String maxScore;
    @Column(name = "정렬순서") private String orderby;
}
