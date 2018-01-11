package com.humane.smps.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ScoreUploadDto {
    private String userAdmissions;
    private String admissionCd;
    private String admissionNm;
    private String year;
    private String examineeCd;
    private String isAttend;
    private String scorerCd;
    private String itemNo;
    private String score;
}
