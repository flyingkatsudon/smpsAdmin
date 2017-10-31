package com.humane.smps.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.humane.util.jackson.PercentSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatusDto extends BasicDto implements Serializable {

    private BasicDto basicDto;

    private Long examineeCnt;
    private Long attendCnt;
    private Long absentCnt;

    @JsonSerialize(using = PercentSerializer.class)
    private BigDecimal attendPer;

    @JsonSerialize(using = PercentSerializer.class)
    private BigDecimal absentPer;

}