package com.humane.smps.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.humane.util.jackson.TimeSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ScoreUploadDto {
    private String userAdmissions;
    private String year;
    private String examineeCd;
    private String isAbsent;
    private String scorerCd;
    private String itemNo;
    private String score;
}
