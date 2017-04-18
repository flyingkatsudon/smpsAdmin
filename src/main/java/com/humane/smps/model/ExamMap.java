package com.humane.smps.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"examineeCd", "examCd"}))
@Data
public class ExamMap {
    @Id @GeneratedValue private Long _id;

    @ManyToOne @JoinColumn(name = "examCd", nullable = false) private Exam exam;
    @ManyToOne @JoinColumn(name = "examineeCd", nullable = false) private Examinee examinee;
    @ManyToOne @JoinColumn(name = "hallCd", nullable = false) private Hall hall;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date scanDttm;

    private String virtNo;
    private String memo;
    private String photoNm;
    private String evalCd;
    private String groupNm;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @Temporal(TemporalType.DATE)
    private Date hallDate;

    private Long orderby;
}
