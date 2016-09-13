package com.humane.smps.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class EvalDto {
    public String evalCd;
    public String examineeCd;

    public String paperCd;

    public String scorerNm;
    public String virtNo;
    public String examCd;
    public String hallCd;

    public String score01;
}