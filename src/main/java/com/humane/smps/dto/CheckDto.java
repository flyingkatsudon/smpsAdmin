package com.humane.smps.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CheckDto extends BasicDto{

    private String examineeCd;
    private String virtNo;

    private String scorerNm;
    private Long itemCnt;
    private Long scorerCnt;
    private Long scoredCnt;

    private Boolean isVirtNo;
}
