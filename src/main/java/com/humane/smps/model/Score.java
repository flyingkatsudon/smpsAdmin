package com.humane.smps.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"examCd", "virtNo", "scorerNm"}))
public class Score implements Serializable {
    @Id @GeneratedValue private Long _id;

    @Column(nullable = false) private long scoreSeq;
    @ManyToOne @JoinColumn(name = "examCd", nullable = false) private Exam exam;
    @ManyToOne @JoinColumn(name = "hallCd", nullable = false) private Hall hall;
    @Column(nullable = false) private String virtNo;
    @Column(nullable = false) private String scorerNm;

    private String score01;
    private String score02;
    private String score03;
    private String score04;
    private String score05;
    private String score06;
    private String score07;
    private String score08;
    private String score09;
    private String score10;
    private String totalScore;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date scoreDttm;

    private Long sheetNo;
}