package com.humane.smps.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"examCd", "scorerNm", "sheetNo"}))
public class Sheet {
    @Id @GeneratedValue private Long _id;

    @ManyToOne @JoinColumn(name = "examCd", nullable = false) private Exam exam;
    @ManyToOne @JoinColumn(name = "hallCd", nullable = false) private Hall hall;
    @Column(nullable = false) private String scorerNm;
    @Column(nullable = true) private String scorerNm2;
    @Column(nullable = false) private Long sheetNo;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date regDttm;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date printDttm;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date cancelDttm;
}
