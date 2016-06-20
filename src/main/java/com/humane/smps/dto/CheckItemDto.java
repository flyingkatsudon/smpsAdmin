package com.humane.smps.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.humane.util.jackson.TimeSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * SELECT admission.admission_nm
 , exam.exam_nm
 , exam.exam_date
 , exam.exam_time
 , examinee.examinee_cd
 , examinee.major_nm
 , examinee.dept_nm
 , hall.head_nm
 , hall.bldg_nm
 , hall.hall_nm
 , exam_map.virt_no
 , score.scorer_nm
 , (SELECT COUNT(*) FROM item WHERE exam.exam_cd = item.exam_cd) AS item_cnt
 , (score.score01 IS NOT NULL)
 + (score.score02 IS NOT NULL)
 + (score.score03 IS NOT NULL)
 + (score.score04 IS NOT NULL)
 + (score.score05 IS NOT NULL)
 + (score.score06 IS NOT NULL)
 + (score.score07 IS NOT NULL)
 + (score.score08 IS NOT NULL)
 + (score.score09 IS NOT NULL)
 + (score.score10 IS NOT NULL) AS scored_cnt
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CheckItemDto {
    private String admissionNm;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Date examDate;

    @DateTimeFormat(pattern = "HH:mm:ss")
    @JsonSerialize(using = TimeSerializer.class)
    private Date examTime;

    private String deptNm;
    private String majorNm;
    private String examNm;
    private String headNm;
    private String bldgNm;
    private String hallNm;

    private String examineeCd;
    private String virtNo;

    private String scorerNm;
    private Long itemCnt;
    private Long scorerCnt;
    private Long scoredCnt;
}
