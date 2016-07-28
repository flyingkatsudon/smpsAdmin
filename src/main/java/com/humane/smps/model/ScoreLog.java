package com.humane.smps.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"examCd", "hallCd", "scorerNm", "logDttm"}))
public class ScoreLog implements Serializable {
    @Id @GeneratedValue public long _id;

    @ManyToOne @JoinColumn(name = "examCd", nullable = false) private Exam exam;
    @ManyToOne @JoinColumn(name = "hallCd", nullable = false) private Hall hall;
    @Column(nullable = false) public String scorerNm;
    @Column(nullable = false) public String logDttm;

    @Column public String virtNo;
    @Column public String totalScore;
    @Column public String score01;
    @Column public String score02;
    @Column public String score03;
    @Column public String score04;
    @Column public String score05;
    @Column public String score06;
    @Column public String score07;
    @Column public String score08;
    @Column public String score09;
    @Column public String score10;
    @Column public String scoreDttm;
    @Column public Long sheetNo;
}