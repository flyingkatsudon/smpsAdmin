package com.humane.smps.dto;

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
import java.util.List;

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