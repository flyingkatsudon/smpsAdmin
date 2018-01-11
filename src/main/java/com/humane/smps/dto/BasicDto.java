package com.humane.smps.dto;

/**
 * Created by Jeremy on 2017. 10. 16..
 */

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.humane.util.jackson.PercentSerializer;
import com.humane.util.jackson.TimeSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BasicDto implements Serializable {
    public String userAdmissions;

    public String univCd;
    public String admissionCd;
    public String admissionNm;
    private String exmAdmNm;

    public String typeNm;

    public String examCd;
    public String examNm;

    public String period;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    public Date examDate;

    public String headNm;
    public String bldgNm;
    public String hallCd;
    public String hallNm;

    public String deptNm;
    public String majorNm;

    public String groupNm;
    private String year;

    private String examineeCd;
    private String examineeNm;

    /* 수험표 출력 */
    private BufferedImage univLogo;
    private BufferedImage examineeImage;

    private String fromExamineeCd;
    private String toExamineeCd;
}