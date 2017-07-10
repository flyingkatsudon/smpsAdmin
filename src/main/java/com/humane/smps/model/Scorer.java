package com.humane.smps.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Scorer {
    @Id @GeneratedValue private Long _id;
    private String scorerId;
    private String scorerPw;
    private String examCd;
    private String hallCd;
}
