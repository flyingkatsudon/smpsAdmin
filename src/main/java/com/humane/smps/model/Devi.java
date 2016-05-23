package com.humane.smps.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@Cacheable
public class Devi implements Serializable {
    @Id private String deviCd;
    @ManyToOne @JoinColumn(name = "fkDeviCd") private Devi devi;

    private String deviNm;
    @Column(columnDefinition = "int default 0") private int minScore;
    @Column(columnDefinition = "int default 100") private int maxScore;
    @Column(columnDefinition = "int default 0") private int orderby;

}